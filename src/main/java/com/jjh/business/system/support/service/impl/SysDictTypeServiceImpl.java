package com.jjh.business.system.support.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.support.controller.form.SysDictTypeQueryForm;
import com.jjh.business.system.support.mapper.SysDictDataMapper;
import com.jjh.business.system.support.mapper.SysDictTypeMapper;
import com.jjh.business.system.support.model.SysDictData;
import com.jjh.business.system.support.model.SysDictType;
import com.jjh.business.system.support.service.SysDictTypeService;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.key.CacheConstants;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典类型 服务层实现
 *
 * @author jjh
 * @date 2019/12/09
 */
@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;
    @Resource
    private SysDictDataMapper sysDictDataMapper;


    /**
     * 查询字典类型列表
     *
     * @param form 查询条件
     * @return 字典类型集合
     */
    @Override
    public List<SysDictType> list(PageRequestForm<SysDictTypeQueryForm> form) {
        return sysDictTypeMapper.selectList(form.pageWrapper(SysDictType.class));
    }

    /**
     * 新增字典类型对象
     *
     * @param entity 待新增对象
     * @return 字典类型对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysDictType add(SysDictType entity) {
        if (sysDictTypeMapper.selectOne(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getCode, entity.getCode())) != null) {
            throw new BusinessException("该字典code已存在，请检查。冲突项："+ entity.getName());
        }
        entity.setId(IdGenerateHelper.nextId());
        sysDictTypeMapper.insert(entity);
        return entity;
    }

    /**
     * 更新字典类型对象
     *
     * @param entity 待更新对象
     * @return 字典类型对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_BASE_DICT_TYPE_DATA_LIST}, allEntries = true)
    public SysDictType update(SysDictType entity) {
        SysDictType oldEntity = sysDictTypeMapper.selectOne(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getId, entity.getId()));
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        SysDictType dictType = sysDictTypeMapper.selectOne(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getCode, entity.getCode()));
        if (dictType != null && !dictType.getId().equals(oldEntity.getId())) {
            throw new BusinessException("该字典code已存在，请检查。冲突项："+ entity.getName());
        }

        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysDictTypeMapper.updateById(oldEntity);
        return oldEntity;
    }

    /**
     * 删除字典类型对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_BASE_DICT_TYPE_DATA_LIST}, allEntries = true)
    public boolean del(String ids) {
        sysDictTypeMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
        return true;
    }

    /**
     * 导入数据
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(List<SysDictType> list, boolean updateSupport) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("待导入数据为空");
        }
        for (SysDictType entity : list) {
            SysDictType oldEntity = sysDictTypeMapper.selectOne(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getCode, entity.getCode()));
            if (oldEntity != null) {
                if (Boolean.TRUE.equals(updateSupport)) {
                    PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
                    sysDictTypeMapper.updateById(oldEntity);
                }
                else {
                    throw new BusinessException("存在重复数据，请检查。code：" + entity.getCode());
                }
            }
            else {
                entity.setId(IdGenerateHelper.nextId());
                // 使用数据库事务保证重复判断
                sysDictTypeMapper.insert(entity);
            }
        }
    }


    /**
     * 查询所有字典类型以及其对应的字典值
     * @return
     */
    @Override
    public List<SysDictType> listAllTypeAndData() {
        List<SysDictType> dictTypeList = sysDictTypeMapper.selectList(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getStatus, BaseConstants.STATUS_NOMAL));
        if (CollectionUtil.isNotEmpty(dictTypeList)) {
            // 回显字典值
            for (SysDictType sysDictType : dictTypeList) {
                List<SysDictData> sysDictDataList = sysDictDataMapper.selectList(Wrappers.<SysDictData>lambdaQuery()
                        .eq(SysDictData::getCode, sysDictType.getCode())
                        .eq(SysDictData::getStatus, BaseConstants.STATUS_NOMAL));
                sysDictType.setDictData(sysDictDataList);
            }
        }
        return dictTypeList;
    }
}
