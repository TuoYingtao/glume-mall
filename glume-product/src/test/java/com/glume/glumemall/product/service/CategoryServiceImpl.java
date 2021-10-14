package com.glume.glumemall.product.service;

import com.glume.glumemall.product.dao.CategoryDao;
import com.glume.glumemall.product.entity.CategoryEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tuoyingtao
 * @create 2021-10-14 18:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImpl {

    @Autowired
    CategoryDao baseMapper;

    @Test
    public void listWithTree() {
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
    }
    /* 递归查找所有菜单的子菜单 */
    public List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all) {
        System.out.println(root);
        return null;
    }
}
