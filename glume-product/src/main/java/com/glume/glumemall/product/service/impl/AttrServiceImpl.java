package com.glume.glumemall.product.service.impl;

import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.AttrDao;
import com.glume.glumemall.product.entity.AttrEntity;
import com.glume.glumemall.product.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if (catelogId == 0) {
            IPage<AttrEntity> page = this.page(
                    new Query<AttrEntity>().getPage(params),
                    new QueryWrapper<AttrEntity>());
            return new PageUtils(page);
        } else {
            QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId);
            if (StringUtils.isNotNull(params.get("key"))) {
                String key = params.get("key").toString();
                attrEntityQueryWrapper.and(obj -> {
                    obj.like("attr_name",key).or().like("value_select",key).or().like("icon",key);
                });
            }
            IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params),attrEntityQueryWrapper);
            return new PageUtils(page);
        }
    }

}