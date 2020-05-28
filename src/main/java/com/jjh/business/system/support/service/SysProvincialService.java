package com.jjh.business.system.support.service;

import com.jjh.business.system.support.model.SysProvincial;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地区省份 服务层
 *
 * @author jjh
 * @date 2019/12/13
 */
public interface SysProvincialService {

    /**
     * 查询地区省份列表
     *
     * @param form 查询条件
     * @return 地区省份集合
     */
    List<SysProvincial> list(PageRequestForm<SysProvincial> form);

    /**
     * 新增地区省份对象
     *
     * @param entity 待新增对象
     * @return 地区省份对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysProvincial add(SysProvincial entity);


    /**
     * 更新地区省份对象
     *
     * @param entity 待更新对象
     * @return 地区省份对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysProvincial update(SysProvincial entity);


    /**
     * 删除地区省份对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);

    /**
     * 导入数据
     *
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    void importData(List<SysProvincial> list, boolean updateSupport);

    /**
     * 获取地区省份根节点列表
     * @return
     */
    List<SysProvincial> rootList();

    /**
     * 获取子节点列表
     * @param parentCode  父节点编号
     * @return
     */
    List<SysProvincial> childrenList(Integer parentCode);

    /**
     * 获取父节点
     * @param code  节点编号
     * @return
     */
    SysProvincial parent(Integer code);

    /**
     * 获取节点
     * @param code  节点编号
     * @return  节点信息
     */
    SysProvincial getByCode(Integer code);

    /**
     * 获取节点
     * @param codes  节点编号
     * @return  节点信息
     */
    String getByCodes(String codes);
}
