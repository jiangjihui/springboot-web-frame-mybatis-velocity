package com.business.common.gen.service;

public interface IGenSerivce {

    /**
     * 生成代码
     *
     * @param tableName 表名称
     * @return 数据
     */
    byte[] generatorCode(String tableName);

}
