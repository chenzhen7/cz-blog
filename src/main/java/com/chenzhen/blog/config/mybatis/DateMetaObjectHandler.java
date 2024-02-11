package com.chenzhen.blog.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/9/16 11:57
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Component//以组件的形式把这个处理器注册到IOC容器中
public class DateMetaObjectHandler implements MetaObjectHandler {

    //日期自动填充
    //插入时启动  第三个参数 LocalDateTime 一定要和 createTime成员变量的值的类型一致，不然是null 如果是date就都设置date
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
    }

    //更新日期自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

}