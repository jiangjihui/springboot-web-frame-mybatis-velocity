package com.jjh.business.system.dept.service;

import com.jjh.business.system.dept.model.SysDept;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门 服务层
 *
 * @author jjh
 * @date 2020/04/21
 */
public interface SysDeptService {

    /**
     * 查询部门列表
     *
     * @param form 查询条件
     * @return 部门集合
     */
    List<SysDept> list(PageRequestForm<SysDept> form);

    /**
     * 新增部门对象
     *
     * @param entity 待新增对象
     * @return 部门对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysDept add(SysDept entity);


    /**
     * 更新部门对象
     *
     * @param entity 待更新对象
     * @return 部门对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysDept update(SysDept entity);


    /**
     * 删除部门对象
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
    void importData(List<SysDept> list, boolean updateSupport);

    /**
     * 查询树结构列表
     * @return
     */
    List<SysDept> selectTreeList();

    /**
     * 查询所有树结构列表
     * @return
     */
    List<SysDept> allTreeList(PageRequestForm<SysDept> form);

    /**
     * 查询子部门列表
     * @param deptId 部门ID
     * @return
     */
    List<SysDept> childrenList(String deptId);

    /**
     * 查询子部门ID列表
     * @param deptId    部门ID
     * @return
     */
    List<String> childrenIdList(String deptId);
}
