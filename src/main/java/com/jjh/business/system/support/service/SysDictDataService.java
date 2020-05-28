package com.jjh.business.system.support.service;

import com.jjh.business.system.support.model.SysDictData;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典数据 服务层
 *
 * @author jjh
 * @date 2019/12/09
 */
public interface SysDictDataService {

    /**
     * 查询字典数据列表
     *
     * @param form 查询条件
     * @return 字典数据集合
     */
    List<SysDictData> list(PageRequestForm<SysDictData> form);

    /**
     * 新增字典数据对象
     *
     * @param entity 待新增对象
     * @return 字典数据对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysDictData add(SysDictData entity);


    /**
     * 更新字典数据对象
     *
     * @param entity 待更新对象
     * @return 字典数据对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysDictData update(SysDictData entity);


    /**
     * 删除字典数据对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType  字典类型
     * @return
     */
    List<SysDictData> listByDictType(String dictType);

    /**
     * 根据字典类型查询字典名称
     *
     * @param dictType  字典类型
     * @param code  字典值
     * @return
     */
    String getDictDataName(String dictType, String code);

    /**
     * 根据字典名称查询字典值
     *
     * @param dictType  字典类型
     * @param name  字典名称
     * @return
     */
    String getDictDataCode(String dictType, String name);

    /**
     * 导入数据
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    void importData(List<SysDictData> list, boolean updateSupport);
}
