package com.glume.glumemall.product.service.impl;


import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.mybatis.Query;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.product.dao.AttrAttrgroupRelationDao;
import com.glume.glumemall.product.entity.AttrAttrgroupRelationEntity;
import com.glume.glumemall.product.service.AttrAttrgroupRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteAttrgroupBatchIds(List<Long> attrGroupIds) {
        SqlHelper.retBool(baseMapper.delete(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .in("attr_group_id", attrGroupIds)));
    }

}