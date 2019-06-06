package com.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.framework.mybatis.annotation.CreatedTime;
import com.framework.mybatis.annotation.UpdateTime;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Entity基类
 */
@Data
public abstract class BaseEntity implements Serializable {

    private String id = UUID.randomUUID().toString();

    @CreatedTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @UpdateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;


}
