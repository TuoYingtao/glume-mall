package com.glume.glumemall.product.service.impl;


import com.glume.glumemall.common.utils.mybatis.Query;
import com.glume.glumemall.common.utils.mybatis.PageUtils;
import com.glume.glumemall.product.dao.AttrAttrgroupRelationDao;
import com.glume.glumemall.product.entity.AttrAttrgroupRelationEntity;
import com.glume.glumemall.product.service.AttrAttrgroupRelationService;
import org.springframework.stereotype.Service;
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

}