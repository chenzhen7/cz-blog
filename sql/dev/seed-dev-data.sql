-- ============================================================================
-- cz-blog 本地开发数据补缺
--
-- ⚠️ 仅用于本地开发，禁止在生产执行。
--
-- 背景：本地 blog 库已经有真实数据（47 篇文章 / 16 个标签 / 48 条评论 /
--      99 条留言 / 21 个友链），覆盖了绝大部分渲染分支，不需要再造一份假的。
--      这个文件只补真实数据里缺的那几个分支。
--
-- 已核对：真实数据已覆盖的分支（不需要再补）
--   ✓ 文章列表与分页        47 篇
--   ✓ 无封面文章            13 篇 first_picture 为空
--   ✓ description 截断      38 篇超过 100 字
--   ✓ 归档跨年份            2022(14) / 2023(13) / 2024(20)
--   ✓ 评论两级嵌套          48 条含 7 条回复
--   ✓ 管理员评论标识        6 条
--   ✓ 评论分页              48 条，每页 10 条
--   ✓ 留言与嵌套            99 条
--   ✓ 正文含 emoji          11 篇
--
-- 幂等：可重复执行。id 落在 9100+ 区间，不与真实数据冲突。
-- ============================================================================

SET NAMES utf8mb4;

-- ---------------------------------------------------------------------------
-- 缺口 1：友链的网络状态色块有两个分支验不到
--
-- 现有数据只覆盖 net_status = 0（良好，16 条）和 3（异常，4 条）。
-- 前台按 net_status 渲染四种状态色，缺 1（状态一般）和 2（网络超时）两个分支。
--   NetStatus: 0=良好 1=一般 2=超时 3=异常     Status: 1=审核通过
-- ---------------------------------------------------------------------------
DELETE FROM `t_friend` WHERE `id` IN (9101, 9102);

INSERT INTO `t_friend`
(`id`, `blog_name`, `blog_address`, `blog_description`, `picture_address`,
 `email`, `net_status`, `status`, `reason`, `create_time`, `update_time`)
VALUES
(9101, '开发用样例友链·状态一般',
 'https://dev-sample-slow.example.com/',
 '仅用于本地开发，验证 net_status=1 时状态色块的渲染。真实数据里缺这个分支，不要删。',
 '/images/me.jpg', 'dev@example.com', 1, 1, NULL, NOW(), NOW()),

(9102, '开发用样例友链·网络超时',
 'https://dev-sample-timeout.example.com/',
 '仅用于本地开发，验证 net_status=2 时状态色块的渲染。真实数据里缺这个分支，不要删。',
 '/images/me.jpg', 'dev@example.com', 2, 1, NULL, NOW(), NOW());

-- ---------------------------------------------------------------------------
-- 缺口 2：标题里没有 emoji
--
-- 正文有 11 篇含 emoji，但 47 篇标题一个都没有。
-- 标题的 emoji 走的是另一条渲染路径（列表页、归档页、浏览器标签页），单独覆盖不到。
-- 这里只给现有的一篇文章标题追加 emoji，不新造文章。
--
-- 幂等：用 LIKE '​%' 判断是否已经加过，避免重复追加。
-- 还原：把标题末尾的 ' 🎉' 去掉即可。
-- ---------------------------------------------------------------------------
UPDATE `t_blog`
SET `title` = CONCAT(`title`, ' 🎉')
WHERE `id` = (SELECT `id` FROM (SELECT MIN(`id`) AS `id` FROM `t_blog`) AS t)
  AND `title` NOT LIKE '%🎉';

-- ---------------------------------------------------------------------------
-- 缺口 3：没有 description 为 NULL 的文章
--
-- ArchivesController 用 getDescription().length() 且无判空，
-- 一旦出现 NULL 描述，/archives 整页 500。
--
-- 这里不插假文章（会出现在正式列表里），而是留一段手工验证步骤：
--
--   1) 备份并置空：
--      UPDATE t_blog SET description = NULL WHERE id = <任意已发布文章id>;
--   2) 打开 /archives，修复前应当 500，修复后应正常渲染且该篇描述为空；
--   3) 还原：
--      UPDATE t_blog SET description = '<原值>' WHERE id = <同一个id>;
--
-- 取原值：
--   SELECT id, description FROM t_blog WHERE description IS NOT NULL LIMIT 1;
-- ---------------------------------------------------------------------------

-- ---------------------------------------------------------------------------
-- 完成提示
-- ---------------------------------------------------------------------------
SELECT '开发数据补缺完成' AS msg,
       (SELECT COUNT(*) FROM t_friend)                       AS 友链数,
       (SELECT COUNT(*) FROM t_blog WHERE title LIKE '%🎉')   AS 标题含emoji,
       (SELECT COUNT(*) FROM t_friend WHERE net_status = 1)  AS 状态一般,
       (SELECT COUNT(*) FROM t_friend WHERE net_status = 2)  AS 网络超时;
