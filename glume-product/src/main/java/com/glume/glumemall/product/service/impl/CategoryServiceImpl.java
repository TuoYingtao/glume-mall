package com.glume.glumemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.dao.CategoryDao;
import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.CategoryBrandRelationService;
import com.glume.glumemall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void removeMenuByIds(List<Long> catIds) {
        //TODO 1.检测当前删除的商品分类信息，是否被别的地方引用

        // 储存需要删除的ID
        List<CategoryEntity> dleCatIds = new ArrayList<>();
        // 获取所有分类数据
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<>());
        for (Long catId : catIds) {
            List<CategoryEntity> collect = categoryEntities.stream().filter(categoryEntity -> catId.equals(categoryEntity.getCatId())).map(categoryEntity -> {
                dleCatIds.addAll(dleCatIdsHandler(categoryEntity.getCatId(), categoryEntities, dleCatIds));
                return categoryEntity;
            }).distinct().collect(Collectors.toList());
            dleCatIds.addAll(collect);
        }
        List<Long> collect = dleCatIds.stream().map(CategoryEntity::getCatId).collect(Collectors.toList());
        // 批量删除分类
        baseMapper.deleteBatchIds(collect);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryServiceById(CategoryEntity category) {
        baseMapper.updateById(category);
        if (StringUtils.isNotEmpty(category.getName())) {
            categoryBrandRelationService.updateCategoryName(category.getCatId(),category.getName());
        }
    }

    /**
     * 查询子分类的处理方法
     * @param cateId 父分类ID
     * @param categoryEntities 所有分类集合
     * @param dleCatIds 分类容器
     * @return
     */
    private List<CategoryEntity> dleCatIdsHandler(Long cateId,List<CategoryEntity> categoryEntities,List<CategoryEntity> dleCatIds) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(categoryEntity -> cateId.equals(categoryEntity.getCatId())).map(categoryEntity -> {
            dleCatIds.addAll(dleCatIdsHandler(categoryEntity.getCatId(), categoryEntities,dleCatIds));
            return categoryEntity;
        }).distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 1.查询所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 2.组装父子树形结构
        // 2.1 找到所有的一级分类
        Stream<CategoryEntity> levelMenus1 = entities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0);
        Stream<CategoryEntity> levelMenusMap1 = levelMenus1.map(categoryEntity -> {
            categoryEntity.setChildren(getChildrens(categoryEntity, entities));
            return categoryEntity;
        });
        Stream<CategoryEntity> sortedMenus = levelMenusMap1.sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()));
        List<CategoryEntity> list = sortedMenus.collect(Collectors.toList());
        return list;
    }

    /* 递归查找所有菜单的子菜单 */
    public List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId())
                .map(categoryEntity -> {
                    // 1.找到子菜单
                    categoryEntity.setChildren(getChildrens(categoryEntity,all));
                    return categoryEntity;
                }).sorted((menu1,menu2) -> {
                    // 2.菜单排序
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());
        return children;
    }


    @Override
    public List<CategoryEntity> categoryPath(Long catelogId) {
        List<CategoryEntity> entities = SpringUtils.getBean(CategoryServiceImpl.class).getBaseMapper().selectList(new QueryWrapper<CategoryEntity>());
        List<CategoryEntity> list = new ArrayList<>();
        List<CategoryEntity> collect = entities.stream().filter(categoryEntity -> catelogId.equals(categoryEntity.getCatId())).map(categoryEntity -> {
            list.addAll(categoryPathHandler(categoryEntity, entities, list));
            return categoryEntity;
        }).collect(Collectors.toList());
        list.addAll(collect);
        return list;
    }

    private List<CategoryEntity> categoryPathHandler(CategoryEntity categoryEntity,List<CategoryEntity> entities,List<CategoryEntity> list) {
        if (categoryEntity.getParentCid() != 0) {
            List<CategoryEntity> collect = entities.stream().filter(f -> categoryEntity.getParentCid().equals(f.getCatId())).map(item -> {
                list.addAll(categoryPathHandler(item, entities, list));
                return item;
            }).collect(Collectors.toList());
            return collect;
        }
        return list;
    }

}