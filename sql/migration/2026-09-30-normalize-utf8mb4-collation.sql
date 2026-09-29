/*
 * 迁移：统一全库字符集与排序规则为 utf8mb4 / utf8mb4_general_ci
 * ---------------------------------------------------------------------------
 * 背景（先说清楚「现在没坏」这件事，避免后来者误判成紧急故障）
 *
 *   线上库 t_comment / t_message / t_friend 的**表级默认值**是 3 字节的
 *   utf8（即 utf8mb3），但它们的**文本列**都显式声明了 utf8mb4，所以 emoji
 *   今天存得进去、也取得出来。这不是一个「已经坏了」的修复脚本。
 *
 *   迁移前实测（可复现，见文件末尾「验收」）：
 *     INSERT 派对🎉 狗🐶 → HEX = 7061727479F09F8E8920646F67F09F90B6
 *   4 字节序列 F09F8E89 / F09F90B6 完整落库，没有被截断成 3F('?')。
 *
 * 那么这个脚本修什么？三件「现在不疼、以后必疼」的事：
 *
 *   1) 表级默认值是 3 字节 utf8
 *      以后任何人给这些表新增一个字符列、且没显式写 CHARACTER SET，
 *      该列会静默继承成 3 字节 → 从那一刻起 emoji 才真的存不进去。
 *      这类 bug 不会在开发时暴露（列类型是对的，值是后来才写坏的），
 *      只在某个用户发了带 emoji 的评论时表现为截断或报错。
 *
 *   2) 列级 collation 混用，同表内都不一致
 *      最典型的是 t_blog：title 是 utf8mb4_0900_ai_ci，而 content /
 *      description 是 utf8mb4_general_ci。两张不同 collation 的列放在
 *      比较、UNION、ORDER BY、JOIN ON 的同一侧，MySQL 会直接抛
 *      「Illegal mix of collations」。现在没踩到只是恰好没有这样的 SQL，
 *      一旦有人写出来就是运行时 500。
 *
 *   3) init.sql 把这些都固化进了新部署
 *      每个新装的实例都会继承同样的坑，所以这个脚本要和 init.sql 一起改。
 *
 * 目标 collation 为什么选 utf8mb4_general_ci，而不是 MySQL 8.0 的默认值
 * utf8mb4_0900_ai_ci？
 *
 *   utf8mb4_0900_ai_ci 是 MySQL 8.0 专属，5.7 上不存在。生产实例的版本
 *   未经确认，选在 5.7 和 8.0 上都存在的 utf8mb4_general_ci 更稳。
 *   本库 t_blog 的表级 collation 本来也就是 utf8mb4_general_ci。
 *
 *   代价（已知并接受）：general_ci 是「简单」排序规则，对部分语言的
 *   等价判断比 0900 粗糙。本库没有任何唯一索引落在字符列上（只有主键），
 *   排序规则只影响标签/名称/邮箱的等值比较与标题排序，不涉及业务正确性，
 *   因此这个代价可以接受。
 *
 * 安全性
 *
 *   - 脚本幂等：重复执行结果相同，中途失败可以修完直接重跑。
 *     （ALTER TABLE 是 DDL，MySQL 里不可回滚，所以幂等是唯一的补救手段。）
 *   - 全库只有 PRIMARY 一个唯一索引，没有字符列唯一约束，
 *     因此收紧/更换 collation 不会撞出 duplicate key。
 *   - 17 张表合计行数在千级，表重建是秒级，不需要 pt-online-schema-change。
 *   - 缺表会**报错中断**（不是被跳过），报错信息里带表名。
 *     这是刻意的：宁可在这里响亮地失败，也不要静默跳过一张表、
 *     留下一个「以为迁完了其实没有」的库。
 *
 * 执行前请务必备份：
 *   mysqldump -uroot -p --single-transaction --databases blog > backup.sql
 */

-- ---------------------------------------------------------------------------
-- 1. 库级默认
-- ---------------------------------------------------------------------------
-- 不写库名，作用于当前选中的库，避免把库名硬编码进脚本。
-- 这一条保证的是「以后新建的表」默认就是 utf8mb4。
ALTER DATABASE CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;


-- ---------------------------------------------------------------------------
-- 2. 表级 + 列级
-- ---------------------------------------------------------------------------
-- CONVERT TO CHARACTER SET 会同时改掉「表默认值」和「该表所有字符列」，
-- 所以下面每条 ALTER 一步到位，不需要再逐列 MODIFY。
-- 注意它**不**影响非字符列（int / datetime / bit 等），那些原样保留。

-- 2.1 表级默认是 3 字节 utf8 的四张表 —— 本次迁移的主要目标。
--     列本身已经是 utf8mb4，这里主要修的是表默认值，顺带把列 collation
--     从 0900_ai_ci 统一到 general_ci。
ALTER TABLE `t_comment` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_friend`  CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_message` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_user`    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 2.2 表内列 collation 混用的两张表 —— 修掉「Illegal mix of collations」隐患。
--     t_blog:  title=0900_ai_ci 而 content/description=general_ci
--     t_prompt: content=0900_ai_ci 而 original_content=general_ci
ALTER TABLE `t_blog`   CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_prompt` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 2.3 其余表：表级与列级都已经是 utf8mb4_0900_ai_ci，本身自洽、没有隐患。
--     仍然一并转换，理由是全库只留一种 collation 才不会有下一次
--     「这张表和那张表对不上」的问题；混着两种 collation 的库迟早要再迁一次。
ALTER TABLE `t_blog_tag`       CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_music`          CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_prompt_category` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_site`           CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_site_category`  CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_sys_config`     CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_sys_log`        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_tag`            CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_type`           CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_views`          CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `t_white_list`     CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- ---------------------------------------------------------------------------
-- 验收（迁移后跑，用来证明真的生效了，而不是「看起来没报错」）
-- ---------------------------------------------------------------------------
-- a) 不该再有 3 字节 utf8 的表：
--    SELECT TABLE_NAME, TABLE_COLLATION FROM information_schema.TABLES
--     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_COLLATION <> 'utf8mb4_general_ci';
--    → 期望 0 行
--
-- b) 不该再有非 utf8mb4 的字符列，且 collation 应当只剩一种：
--    SELECT DISTINCT CHARACTER_SET_NAME, COLLATION_NAME
--      FROM information_schema.COLUMNS
--     WHERE TABLE_SCHEMA = DATABASE() AND CHARACTER_SET_NAME IS NOT NULL;
--    → 期望只有一行 utf8mb4 / utf8mb4_general_ci
--
-- c) emoji 往返（事务内回滚，不留数据）—— 4 字节序列必须完整出现：
--    START TRANSACTION;
--    INSERT INTO t_comment (nickname, content, admin_comment, create_time)
--      VALUES ('probe', '派对🎉 狗🐶', 0, NOW());
--    SELECT HEX(content) FROM t_comment WHERE nickname = 'probe';
--    → 期望含 F09F8E89（🎉）与 F09F90B6（🐶）；若变成 3F 就是被截断了
--    ROLLBACK;
--
-- d) 新增列默认继承 utf8mb4（这是 2.1 真正要修的东西，值得单独验一次）：
--    CREATE TABLE `_collation_probe` (`c` varchar(10));
--    SELECT CHARACTER_SET_NAME FROM information_schema.COLUMNS
--     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '_collation_probe';
--    DROP TABLE `_collation_probe`;
--    → 期望 utf8mb4；迁移前在 t_comment 这类表上建会得到 utf8
