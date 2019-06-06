package com.common.model;

import com.framework.mybatis.annotation.CreatedBy;
import com.framework.mybatis.annotation.UpdateBy;
import lombok.Data;

/**
 * 业务对象基础类（包含审计功能）
 *
 * JPA Audit
 * https://www.jianshu.com/p/14cb69646195
 * 首先申明实体类，需要在类上加上注解@EntityListeners(AuditingEntityListener.class)，
 * 其次在application启动类中加上注解EnableJpaAuditing，
 * 同时在需要的字段上加上@CreatedDate、@CreatedBy、@LastModifiedDate、@LastModifiedBy等注解。
 */
@Data
public abstract class AuditBaseEntity extends BaseEntity {

    protected static final String TABLE_PREFIX = "b_";

    @CreatedBy
    private String createBy;

    @UpdateBy
    private String updateBy;



}
