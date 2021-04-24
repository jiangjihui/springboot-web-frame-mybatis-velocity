package com.jjh.framework.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jjh.common.util.TimeUtils;
import com.jjh.framework.jwt.JwtUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 审计功能
 * 自动填充 创建时间/更新时间/创建者/更新者
 * https://mybatis.plus/guide/auto-fill-metainfo.html
 *
 * @author jjh
 * @date 2020/2/13
 **/
@Component
public class AuditorMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, TimeUtils.nowDate());
        this.strictInsertFill(metaObject, "createBy", String.class, JwtUtil.getUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", TimeUtils.nowDate(), metaObject);
        this.setFieldValByName("updateBy", JwtUtil.getUserId(), metaObject);
    }
}
