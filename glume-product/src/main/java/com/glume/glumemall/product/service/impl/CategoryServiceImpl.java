package com.glume.glumemall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.utils.RedisUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.dao.CategoryDao;
import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.CategoryBrandRelationService;
import com.glume.glumemall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryServiceById(CategoryEntity category) {
        baseMapper.updateById(category);
        if (StringUtils.isNotEmpty(category.getName())) {
            categoryBrandRelationService.updateCategoryName(category.getCatId(),category.getName());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        categoryBrandRelationService.removeCategoryRelationById(catIds);
    }

    /**
     * 查询子分类的处理方法
     * @param cateId 父分类ID
     * @param categoryEntities 所有分类集合
     * @param dleCatIds 分类容器
     * @return
     */
    private List<CategoryEntity> dleCatIdsHandler(Long cateId,List<CategoryEntity> categoryEntities,List<CategoryEntity> dleCatIds) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(categoryEntity -> cateId.equals(categoryEntity.getParentCid())).map(categoryEntity -> {
            dleCatIds.addAll(dleCatIdsHandler(categoryEntity.getCatId(), categoryEntities,dleCatIds));
            return categoryEntity;
        }).distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        /**
         * 1.空结果缓存： 解决缓存穿透
         * 2.设置过期时间（加随机值）：解决缓存雪崩
         * 3.加锁：解决缓存击穿
         */
        Object allCatalogTree = SpringUtils.getBean(RedisUtils.class).get("allCatalogTree");
        if (StringUtils.isEmpty((String) allCatalogTree)) {
            System.out.println("缓存不命中！");
            List<CategoryEntity> catalogDB = getCatalogDBRedisLock();
            return catalogDB;
        }
        List<CategoryEntity> entities = JSON.parseObject(allCatalogTree.toString(), new TypeReference<List<CategoryEntity>>() {});
        return entities;
    }

    /** 分布式锁 */
    private List<CategoryEntity> getCatalogDBRedisLock() {
        /** Redis “占坑”：解决缓存击穿 */
        // 设置锁的同时添加过期时间保证原子性，避免死锁问题
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if (lock) {
            System.out.println("Redis 分布式加锁成功！");
            // 加锁成功....执行业务
            List<CategoryEntity> catalogDB;
            try {
                catalogDB = getCategoryDataFromDB();
            } finally {
                System.out.println("Redis 分布式删除锁");
                // 删除锁：获取lock锁值进行对比，相同则删除，为了保证原子性使用官方提供的Lua脚本进行解锁
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                Long execute = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }
            return catalogDB;
        } else {
            System.out.println("Redis 分布式加锁失败！");
            // 加锁失败....重试
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogDBRedisLock();
        }
    }

    /** 本地锁 */
    private List<CategoryEntity> getCatalogDBLocalLock() {
        /** 加锁：解决缓存击穿 */
        synchronized(this) {
            return getCategoryDataFromDB();
        }
    }

    private List<CategoryEntity> getCategoryDataFromDB() {
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        Object allCatalogTree = redisUtils.get("allCatalogTree");
        if (StringUtils.isNotEmpty((String) allCatalogTree)) {
            System.out.println("缓存命中！");
            return JSON.parseObject(allCatalogTree.toString(), new TypeReference<List<CategoryEntity>>() {
            });
        }
        System.out.println("查询DB！");
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
        List<CategoryEntity> catalogDB = sortedMenus.collect(Collectors.toList());
        String s = JSON.toJSONString(catalogDB);
        redisUtils.set("allCatalogTree", s, 60 * 60);
        return catalogDB;
    }

    /* 递归查找所有菜单的子菜单 */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all) {
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