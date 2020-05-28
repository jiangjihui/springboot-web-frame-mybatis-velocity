package com.jjh.business.system.dept.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jjh.business.system.dept.mapper.SysDeptMapper;
import com.jjh.business.system.dept.model.SysDept;
import com.jjh.business.system.dept.service.SysDeptService;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 部门 服务层实现
 *
 * @author jjh
 * @date 2020/04/21
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;


    /**
     * 查询部门列表
     *
     * @param form 查询条件
     * @return 部门集合
     */
    @Override
    public List<SysDept> list(PageRequestForm<SysDept> form) {
        return sysDeptMapper.selectList(form.pageWrapper());
    }

    /**
     * 新增部门对象
     *
     * @param entity 待新增对象
     * @return 部门对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysDept add(SysDept entity) {
        entity.setId(IdGenerateHelper.nextId());
        if (entity.getStatus() == null) {
            entity.setStatus(BaseConstants.STATUS_NOMAL);
        }
        sysDeptMapper.insert(entity);
        return entity;
    }

    /**
     * 更新部门对象
     *
     * @param entity 待更新对象
     * @return 部门对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysDept update(SysDept entity) {
        SysDept oldEntity = sysDeptMapper.selectById(entity.getId());
        if (Objects.isNull(oldEntity)) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysDeptMapper.updateById(oldEntity);
        return entity;
    }

    /**
     * 删除部门对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        sysDeptMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
        return true;
    }

    /**
     * 导入数据
     *
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(List<SysDept> list, boolean updateSupport) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("待导入数据为空");
        }
        for (SysDept entity : list) {
            SysDept oldEntity = sysDeptMapper.selectById(entity.getId());
            if (oldEntity != null) {
                if (Boolean.TRUE.equals(updateSupport)) {
                    PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
                    sysDeptMapper.updateById(oldEntity);
                }
                else {
                    throw new BusinessException("存在重复数据，请检查。");
                }
            }
            else {
                entity.setId(IdGenerateHelper.nextId());
                sysDeptMapper.insert(entity);
            }
        }
    }

    /**
     * 查询树结构列表
     * @return
     */
    @Override
    public List<SysDept> selectTreeList() {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDept::getStatus, BaseConstants.STATUS_NOMAL).orderByAsc(SysDept::getSortNo);
        List<SysDept> list = sysDeptMapper.selectList(queryWrapper);
        return linkTreeNode(list);
    }

    /**
     * 查询所有树结构列表
     * @return
     */
    @Override
    public List<SysDept> allTreeList(PageRequestForm<SysDept> form) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        // 如果有查询条件的话，返回所有查询到的结果
        if (form.getFilter() != null) {
            return sysDeptMapper.selectList(form.queryWrapper());
        }
        queryWrapper.lambda().orderByAsc(SysDept::getSortNo);
        List<SysDept> list = sysDeptMapper.selectList(queryWrapper);
        return linkTreeNode(list);
    }

    /**
     * 查询子部门列表
     * @param deptId 部门ID
     * @return
     */
    @Override
    public List<SysDept> childrenList(String deptId) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDept::getParentId, deptId).eq(SysDept::getStatus, BaseConstants.STATUS_NOMAL);
        List<SysDept> deptList = sysDeptMapper.selectList(queryWrapper);
        for (SysDept dept : deptList) {
            List<SysDept> childrenList = childrenList(dept.getId());    //递归查询
            if (CollectionUtil.isNotEmpty(childrenList)) {
                dept.setChildren(childrenList);
            }
        }
        return deptList;
    }

    /**
     * 查询子部门ID列表
     * @param deptId    部门ID
     * @return
     */
    @Override
    public List<String> childrenIdList(String deptId) {
        LinkedList<String> deptIdList = new LinkedList<>();
        List<SysDept> deptList = this.childrenList(deptId);
        return childrenIdList(deptList);
    }

    /**
     * 递归获取子部门ID列表
     * @param deptList    子部门列表
     * @return
     */
    public List<String> childrenIdList(List<SysDept> deptList) {
        LinkedList<String> deptIdList = new LinkedList<>();
        if (CollectionUtil.isNotEmpty(deptList)) {
            for (SysDept dept : deptList) {
                deptIdList.add(dept.getId());
                deptIdList.addAll(childrenIdList(dept.getChildren()));
            }
        }
        return deptIdList;
    }


    /**
     * 串联树结构
     * @param dataList    待串联的列表
     */
    public static List<SysDept> linkTreeNode(List<SysDept> dataList) {
        List<SysDept> result = new LinkedList<>();
        for (SysDept dept : dataList) {
            // 顶级权限
            if (StrUtil.isBlank(dept.getParentId())) {
                result.add(dept);
            }
        }
        // 串联树结构
        HashMap<String, SysDept> map = new HashMap<>();
        for (SysDept dept : dataList) {
            map.put(dept.getId(), dept);
        }
        for (SysDept dept : dataList) {
            if (StrUtil.isNotBlank(dept.getParentId())) {
                SysDept parent = map.get(dept.getParentId());
                if (parent != null) {
                    List<SysDept> children = parent.getChildren();
                    if (children == null) {
                        children = new LinkedList<>();
                        parent.setChildren(children);
                    }
                    children.add(dept);
                }
            }
        }
        return result;
    }
}
