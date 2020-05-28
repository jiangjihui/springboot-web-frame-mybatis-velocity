package com.jjh.business.system.support.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.support.mapper.SysDictDataMapper;
import com.jjh.business.system.support.model.SysDictData;
import com.jjh.business.system.support.service.SysDictDataService;
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
 * 字典数据 服务层实现
 *
 * @author jjh
 * @date 2019/12/09
 */
@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    @Resource
    private SysDictDataMapper sysDictDataMapper;


    /**
     * 查询字典数据列表
     *
     * @param form 查询条件
     * @return 字典数据集合
     */
    @Override
    public List<SysDictData> list(PageRequestForm<SysDictData> form) {
        return sysDictDataMapper.selectList(form.pageWrapper());
    }

    /**
     * 新增字典数据对象
     *
     * @param entity 待新增对象
     * @return 字典数据对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_BASE_DICT_TYPE_DATA_LIST}, allEntries = true)
    public SysDictData add(SysDictData entity) {
        entity.setId(IdGenerateHelper.nextId());
        SysDictData dictData = sysDictDataMapper.selectOne(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictType, entity.getDictType()).eq(SysDictData::getCode, entity.getCode()));
        if (dictData != null) {
            throw new BusinessException("该字典码值已存在，请检查");
        }
        sysDictDataMapper.insert(entity);
        return entity;
    }

    /**
     * 更新字典数据对象
     *
     * @param entity 待更新对象
     * @return 字典数据对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_BASE_DICT_TYPE_DATA_LIST}, allEntries = true)
    public SysDictData update(SysDictData entity) {
        SysDictData oldEntity = sysDictDataMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        SysDictData dictData = sysDictDataMapper.selectOne(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictType, entity.getDictType()).eq(SysDictData::getCode, entity.getCode()));
        if (dictData != null && !dictData.getId().equals(oldEntity.getId())) {
            throw new BusinessException("该字典码值已存在，请检查");
        }
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysDictDataMapper.updateById(oldEntity);
        return entity;
    }

    /**
     * 删除字典数据对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_BASE_DICT_TYPE_DATA_LIST}, allEntries = true)
    public boolean del(String ids) {
        sysDictDataMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
        return true;
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType  字典类型
     * @return
     */
    @Override
    public List<SysDictData> listByDictType(String dictType) {
        return sysDictDataMapper.selectList(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictType, dictType).eq(SysDictData::getStatus, BaseConstants.STATUS_NOMAL));
    }

    /**
     * 根据字典值查询字典名称
     *
     * @param dictType  字典类型
     * @param code  字典值
     * @return
     */
    @Override
    public String getDictDataName(String dictType, String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        List<SysDictData> dataList = sysDictDataMapper.selectList(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictType, dictType));
        for (SysDictData dictData : dataList) {
            if (code.equals(dictData.getCode())) {
                return dictData.getName();
            }
        }
        return code;
    }

    /**
     * 根据字典名称查询字典值
     *
     * @param dictType  字典类型
     * @param name  字典名称
     * @return
     */
    @Override
    public String getDictDataCode(String dictType, String name) {
        if (StrUtil.isBlank(name)) {
            return null;
        }
        List<SysDictData> dataList = sysDictDataMapper.selectList(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictType, dictType));
        for (SysDictData dictData : dataList) {
            if (name.equals(dictData.getName())) {
                return dictData.getCode();
            }
        }
        return name;
    }

    /**
     * 导入数据
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(List<SysDictData> list, boolean updateSupport) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("待导入数据为空");
        }
        for (SysDictData dictData : list) {
            SysDictData oldDictData = sysDictDataMapper.selectOne(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictType, dictData.getDictType()).eq(SysDictData::getCode, dictData.getCode()));
            if (oldDictData != null) {
                if (Boolean.TRUE.equals(updateSupport)) {
                    PojoUtils.copyPropertiesIgnoreNull(dictData, oldDictData);
                    sysDictDataMapper.updateById(oldDictData);
                }
                else {
                    throw new BusinessException("存在重复数据，请检查。字典类型："+oldDictData.getDictType()+"，字典码值："+oldDictData.getCode());
                }
            }
            else {
                dictData.setId(IdGenerateHelper.nextId());
                sysDictDataMapper.insert(dictData);
            }
        }
    }

}
