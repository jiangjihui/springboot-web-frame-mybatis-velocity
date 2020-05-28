package com.jjh.business.common.gen.service;

import com.jjh.business.common.gen.controller.form.GenFileForm;
import com.jjh.business.common.gen.controller.form.GenEntityForm;
import com.jjh.business.common.gen.controller.form.GenEntitySqlForm;
import com.jjh.business.common.gen.controller.form.GenTargetPathForm;

/**
 * GenService
 *
 * @author jjh
 * @date 2019/11/18
 **/
public interface GenService {

    /**
     * 生成实体相关代码
     * @param dto 实体类信息
     */
    void generatorCodeForEntity(GenEntityForm dto);


    /**
     * 生成指定目录下的实体相关代码
     * @param form
     */
    void genCodeFromTargetPath(GenTargetPathForm form);

    /**
     * 生成指定文件
     * @param form
     */
    void genFile(GenFileForm form);

    /**
     * 生成实体SQL
     *aram form
     * @return
     */
    String createModelSql(GenEntitySqlForm form);
}
