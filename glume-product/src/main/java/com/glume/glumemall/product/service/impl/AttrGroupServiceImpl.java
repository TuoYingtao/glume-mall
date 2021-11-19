package com.glume.glumemall.product.service.impl;

import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.entity.AttrEntity;
import com.glume.glumemall.product.service.AttrService;
import com.glume.glumemall.product.vo.AttrGroupWithAttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.AttrGroupDao;
import com.glume.glumemall.product.entity.AttrGroupEntity;
import com.glume.glumemall.product.service.AttrGroupService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AttrGroupWithAttrVo> getAttrGroupWithAttrsBy(Long catelogId) {
        // 查询当前分类分组信息
        List<AttrGroupEntity> groupEntities = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        if (StringUtils.isNotEmpty(groupEntities) && groupEntities.size() > 0) {
            // 把当前分组信息设置到Vo实体里
            List<AttrGroupWithAttrVo> groupWithAttrVos = groupEntities.stream().map(attrGroupEntity -> {
                AttrGroupWithAttrVo attrGroupWithAttrVo = new AttrGroupWithAttrVo();
                BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrVo);
                // 根据分组ID查询它所有的属性
                List<AttrEntity> attrs = attrService.getRelationAttr(attrGroupWithAttrVo.getAttrGroupId());
                attrGroupWithAttrVo.setAttrs(attrs);
                return attrGroupWithAttrVo;
            }).collect(Collectors.toList());
            return groupWithAttrVos;
        }
        return null;
    }

}