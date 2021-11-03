package com.glume.glumemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.Query;
import com.glume.glumemall.product.dao.CategoryDao;
import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
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

}