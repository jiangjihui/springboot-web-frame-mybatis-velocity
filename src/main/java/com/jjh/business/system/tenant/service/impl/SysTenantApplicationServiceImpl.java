package com.jjh.business.system.tenant.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.jjh.business.system.tenant.mapper.SysTenantApplicationMapper;
import com.jjh.business.system.tenant.model.SysTenantApplication;
import com.jjh.business.system.tenant.service.SysTenantApplicationService;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 租户子系统关联 服务层实现
 *
 * @author jjh
 * @date 2020/02/18
 */
@Service
public class SysTenantApplicationServiceImpl implements SysTenantApplicationService {

    @Resource
    private SysTenantApplicationMapper sysTenantApplicationMapper;


    /**
     * 查询租户子系统关联列表
     *
     * @param form 查询条件
     * @return 租户子系统关联集合
     */
    @Override
    public List<SysTenantApplication> list(PageRequestForm<SysTenantApplication> form) {
        return sysTenantApplicationMapper.selectList(form.pageWrapper());
    }

    /**
     * 新增租户子系统关联对象
     *
     * @param entity 待新增对象
     * @return 租户子系统关联对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysTenantApplication add(SysTenantApplication entity) {
        entity.setId(IdGenerateHelper.nextId());
        sysTenantApplicationMapper.insert(entity);
        return entity;
    }

    /**
     * 更新租户子系统关联对象
     *
     * @param entity 待更新对象
     * @return 租户子系统关联对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysTenantApplication update(SysTenantApplication entity) {
        SysTenantApplication oldEntity = sysTenantApplicationMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysTenantApplicationMapper.updateById(oldEntity);
        return entity;
    }

    /**
     * 删除租户子系统关联对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        sysTenantApplicationMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
        return true;
    }
}
