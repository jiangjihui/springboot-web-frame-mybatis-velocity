package com.jjh.business.system.support.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.support.mapper.SysProvincialMapper;
import com.jjh.business.system.support.model.SysProvincial;
import com.jjh.business.system.support.service.SysProvincialService;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.key.CacheConstants;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 地区省份 服务层实现
 *
 * @author jjh
 * @date 2019/12/13
 */
@Service
public class SysProvincialServiceImpl implements SysProvincialService {

    @Resource
    private SysProvincialMapper sysProvincialMapper;


    /**
     * 查询地区省份列表
     *
     * @param form 查询条件
     * @return 地区省份集合
     */
    @Override
    public List<SysProvincial> list(PageRequestForm<SysProvincial> form) {
        return sysProvincialMapper.selectList(form.pageWrapper());
    }

    /**
     * 新增地区省份对象
     *
     * @param entity 待新增对象
     * @return 地区省份对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysProvincial add(SysProvincial entity) {
        entity.setId(IdGenerateHelper.nextId());
        sysProvincialMapper.insert(entity);
        return entity;
    }

    /**
     * 更新地区省份对象
     *
     * @param entity 待更新对象
     * @return 地区省份对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysProvincial update(SysProvincial entity) {
        SysProvincial oldEntity = sysProvincialMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysProvincialMapper.updateById(oldEntity);
        return entity;
    }

    /**
     * 删除地区省份对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        sysProvincialMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
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
    public void importData(List<SysProvincial> list, boolean updateSupport) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("待导入数据为空");
        }
        for (SysProvincial entity : list) {
            SysProvincial oldEntity = sysProvincialMapper.selectById(entity.getId());
            if (oldEntity != null) {
                if (Boolean.TRUE.equals(updateSupport)) {
                    PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
                    sysProvincialMapper.updateById(oldEntity);
                }
                else {
                    throw new BusinessException("存在重复数据，请检查。");
                }
            }
            else {
                entity.setId(IdGenerateHelper.nextId());
                sysProvincialMapper.insert(entity);
            }
        }
    }

    /**
     * 获取地区省份根节点列表
     * @return
     */
    @Override
    public List<SysProvincial> rootList() {
        return sysProvincialMapper.selectList(Wrappers.<SysProvincial>lambdaQuery().isNull(SysProvincial::getParentCode));
    }

    /**
     * 获取子节点列表
     * @param parentCode  父节点ID
     * @return
     */
    @Override
    public List<SysProvincial> childrenList(Integer parentCode) {
        return sysProvincialMapper.selectList(Wrappers.<SysProvincial>lambdaQuery().eq(SysProvincial::getParentCode, parentCode));
    }

    /**
     * 获取父节点
     * @param code  节点ID
     * @return
     */
    @Override
    public SysProvincial parent(Integer code) {
        SysProvincial provincial = sysProvincialMapper.selectOne(Wrappers.<SysProvincial>lambdaQuery().eq(SysProvincial::getCode, code));
        SysProvincial rootProvincial = provincial;
        if (provincial.getParentCode() != null) {
            // 获取父节点
            SysProvincial parentProvincial = sysProvincialMapper.selectOne(Wrappers.<SysProvincial>lambdaQuery().eq(SysProvincial::getParentCode, provincial.getParentCode()));
            if (parentProvincial != null && parentProvincial.getParentCode() != null) {
                // 获取父节点的父节点
                SysProvincial superProvincial = sysProvincialMapper.selectOne(Wrappers.<SysProvincial>lambdaQuery().eq(SysProvincial::getParentCode, parentProvincial.getParentCode()));
                superProvincial.setChildren(parentProvincial);
                rootProvincial = superProvincial;
            }
            else {
                rootProvincial = parentProvincial;
            }
            parentProvincial.setChildren(provincial);
        }
        return rootProvincial;
    }

    /**
     * 获取节点
     * @param code  节点编号
     * @return  节点信息
     */
    @Cacheable(cacheNames= CacheConstants.SYS_BASE_DICT_PROVINCIAL_CODE, key="#code", unless = "#result == null")
    @Override
    public SysProvincial getByCode(Integer code) {
        return sysProvincialMapper.selectOne(Wrappers.<SysProvincial>lambdaQuery().eq(SysProvincial::getCode, code));
    }

    /**
     * 获取节点
     * @param codes  节点编号
     * @return  节点信息
     */
    @Cacheable(cacheNames= CacheConstants.SYS_BASE_DICT_PROVINCIAL_CODESTR, key="#codes", unless = "#result == null")
    @Override
    public String getByCodes(String codes) {
        if (StrUtil.isBlank(codes)) {
            return "";
        }
        String result = "";
        String[] codeList = codes.split(",");
        for (int i = 0; i < codeList.length; i++) {
            String codeStr = codeList[i];
            if (i > 0) {
                result += "/";
            }
            SysProvincial provincial = sysProvincialMapper.selectOne(Wrappers.<SysProvincial>lambdaQuery().eq(SysProvincial::getCode, Integer.valueOf(codeStr)));
            if (provincial != null) {
                result += provincial.getName();
            }

        }
        return result;
    }
}
