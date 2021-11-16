package com.glume.glumemall.product.service.impl;

import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.AttrGroupDao;
import com.glume.glumemall.product.entity.AttrGroupEntity;
import com.glume.glumemall.product.service.AttrGroupService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrGroupEntity> groupEntityQueryWrapper = new QueryWrapper<AttrGroupEntity>();
        if ( StringUtils.isNotNull(params.get("key"))) {
            String key = params.get("key").toString();
            groupEntityQueryWrapper.and((obj) -> {
                obj.eq("attr_group_name",key).or().like("descript",key).or().like("icon",key);
            });
        }
        if (catelogId != 0) groupEntityQueryWrapper.eq("catelog_id",catelogId);
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), groupEntityQueryWrapper);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAttrByIds(List<Long> attrGroupIds) {
        baseMapper.deleteBatchIds(attrGroupIds);
        SpringUtils.getBean(AttrAttrgroupRelationServiceImpl.class).deleteAttrgroupBatchIds(attrGroupIds);
    }

}