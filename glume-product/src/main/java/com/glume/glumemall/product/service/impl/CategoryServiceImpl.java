package com.glume.glumemall.product.service.impl;

import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.CategoryDao;
import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.CategoryService;


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
        List<CategoryEntity> LevelMenus1 = entities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(menu -> {
                  menu.setChildren(getChildrens(menu,entities));
                  return menu;
                }).sorted((menu1,menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());
        return LevelMenus1;
    }
    /* 递归查找所有菜单的子菜单 */
    public List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all) {
                List<CategoryEntity> children = all.stream().filter(categoryEntity -> categoryEntity.getParentCid() == root.getParentCid())
                .map(menu -> {
                    // 1.找到子菜单
                    menu.setChildren(getChildrens(menu,all));
                    return menu;
                }).sorted((menu1,menu2) -> {
                    // 2.菜单排序
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());
        return null;
    }

}