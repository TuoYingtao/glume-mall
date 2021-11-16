package com.glume.glumemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.dao.AttrAttrgroupRelationDao;
import com.glume.glumemall.product.dao.AttrGroupDao;
import com.glume.glumemall.product.dao.CategoryDao;
import com.glume.glumemall.product.entity.AttrAttrgroupRelationEntity;
import com.glume.glumemall.product.entity.AttrGroupEntity;
import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.AttrAttrgroupRelationService;
import com.glume.glumemall.product.service.CategoryService;
import com.glume.glumemall.product.vo.AttrGroupRelationVo;
import com.glume.glumemall.product.vo.AttrRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.AttrDao;
import com.glume.glumemall.product.entity.AttrEntity;
import com.glume.glumemall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>();
        if (catelogId != 0) attrEntityQueryWrapper.eq("catelog_id", catelogId);
        if (StringUtils.isNotNull(params.get("attrType"))) {
            String attrType = params.get("attrType").toString();
            attrEntityQueryWrapper.eq("attr_type",attrType);
        }
        if (StringUtils.isNotNull(params.get("key"))) {
            String key = params.get("key").toString();
            attrEntityQueryWrapper.and(obj -> {
                obj.like("attr_name",key).or().like("value_select",key).or().like("icon",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params),attrEntityQueryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> list = records.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            /** 设置分类分组信息 */
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity =
                    relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
            if (StringUtils.isNotNull(attrAttrgroupRelationEntity)) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                attrRespVo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (StringUtils.isNotNull(categoryEntity)) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(list);
        return pageUtils;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAttrByIds(List<Long> attrIds) {
        baseMapper.deleteBatchIds(attrIds);
        attrAttrgroupRelationService.deleteAttrBatchIds(attrIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttrRespVo getInfoById(Long attrId) {
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity,attrRespVo);
        // 设置分组信息
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity =
                relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
        if (StringUtils.isNotNull(attrAttrgroupRelationEntity)) {
            attrRespVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
            attrRespVo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
        }

        // 设置分类信息
        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if (StringUtils.isNotNull(categoryEntity)) {
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        return attrRespVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttrById(AttrRespVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        baseMapper.updateById(attrEntity);
        if (StringUtils.isNotNull(attrVo.getAttrGroupId())) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrVo.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attrEntity.getAttrId()));
            if (count > 0) {
                relationDao.update(attrAttrgroupRelationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id",attrEntity.getAttrId()));
            } else {
                relationDao.insert(attrAttrgroupRelationEntity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void AttrSave(AttrRespVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrEntity,attrVo);
        baseMapper.insert(attrEntity);
        if (StringUtils.isNotNull(attrVo.getAttrGroupId())) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrVo.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    /**
     * 更具分组ID查询所有关联数据
     * @param attrGroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> attrgroupRelationEntities =
                relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIds = attrgroupRelationEntities.stream().map(attrAttrgroupRelationEntity ->
                attrAttrgroupRelationEntity.getAttrId()).collect(Collectors.toList());
        List<AttrEntity> entityList = this.listByIds(attrIds);
        return entityList;
    }

    /**
     * 删除关联关系
     * @param attrGroupVo
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] attrGroupVo) {
        List<AttrAttrgroupRelationEntity> relationEntities = Arrays.asList(attrGroupVo).stream().map(item -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(relationEntities);
    }

}