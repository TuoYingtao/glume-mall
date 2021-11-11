package com.glume.glumemall.product.service;

import com.glume.common.core.utils.SpringUtils;
import com.glume.glumemall.product.dao.CategoryDao;
import com.glume.glumemall.product.entity.CategoryEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Stream<CategoryEntity> categoryEntityStream = all.stream().filter(categoryEntity -> {
            System.out.println(root);
            System.out.println(categoryEntity.getParentCid() + "---" + root.getCatId());
            return categoryEntity.getParentCid().equals(root.getCatId());
        });
        Stream<CategoryEntity> entityStream = categoryEntityStream.map(menu -> {
            menu.setChildren(getChildrens(menu, all));
            return menu;
        });
        Stream<CategoryEntity> sorted = entityStream.sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()));
        List<CategoryEntity> collect = sorted.collect(Collectors.toList());
        return collect;
    }

    @Test
    public void test() {
        List<CategoryEntity> entities = SpringUtils.getBean(CategoryService.class).categoryPath(225L);
        System.out.println(entities);
    }
}
