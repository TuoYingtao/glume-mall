package com.glume.glumemall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.constant.CategoryConstant;
import com.glume.common.core.utils.RedisUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.dao.CategoryDao;
import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.CategoryBrandRelationService;
import com.glume.glumemall.product.service.CategoryService;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
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

    @Autowired
    RedissonClient redisson;

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
        //TODO 1.?????????????????????????????????????????????????????????????????????

        // ?????????????????????ID
        List<CategoryEntity> dleCatIds = new ArrayList<>();
        // ????????????????????????
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<>());
        for (Long catId : catIds) {
            List<CategoryEntity> collect = categoryEntities.stream().filter(categoryEntity -> catId.equals(categoryEntity.getCatId())).map(categoryEntity -> {
                dleCatIds.addAll(dleCatIdsHandler(categoryEntity.getCatId(), categoryEntities, dleCatIds));
                return categoryEntity;
            }).distinct().collect(Collectors.toList());
            dleCatIds.addAll(collect);
        }
        List<Long> collect = dleCatIds.stream().map(CategoryEntity::getCatId).collect(Collectors.toList());
        // ??????????????????
        baseMapper.deleteBatchIds(collect);
        categoryBrandRelationService.removeCategoryRelationById(catIds);
    }

    /**
     * ??????????????????????????????
     * @param cateId ?????????ID
     * @param categoryEntities ??????????????????
     * @param dleCatIds ????????????
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
         * 1.?????????????????? ??????????????????
         * 2.?????????????????????????????????????????????????????????
         * 3.???????????????????????????
         */
        Object allCatalogTree = SpringUtils.getBean(RedisUtils.class).get(CategoryConstant.ALL_CATALOG_TREE);
        if (StringUtils.isEmpty((String) allCatalogTree)) {
            System.out.println("??????????????????");
            List<CategoryEntity> catalogDB = getCatalogDBRedissonLock();
            return catalogDB;
        }
        List<CategoryEntity> entities = JSON.parseObject(allCatalogTree.toString(), new TypeReference<List<CategoryEntity>>() {});
        return entities;
    }

    /** ???????????????Redisson */
    private List<CategoryEntity> getCatalogDBRedissonLock() {
        RReadWriteLock lock = redisson.getReadWriteLock("product-catalog");
        RLock rLock = lock.readLock();
        List<CategoryEntity> catalogDB = null;
        try {
            rLock.lock();
            System.out.println("Redis ????????????????????????");
            // ????????????....????????????
            catalogDB = getCategoryDataFromDB();
        } finally {
            rLock.unlock();
        }
        return catalogDB;
    }

    /** ???????????? ????????? */
    private List<CategoryEntity> getCatalogDBRedisLock() {
        /** Redis ????????????????????????????????? */
        // ????????????????????????????????????????????????????????????????????????
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if (lock) {
            System.out.println("Redis ????????????????????????");
            // ????????????....????????????
            List<CategoryEntity> catalogDB;
            try {
                catalogDB = getCategoryDataFromDB();
            } finally {
                System.out.println("Redis ??????????????????");
                // ??????????????????lock?????????????????????????????????????????????????????????????????????????????????Lua??????????????????
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                Long execute = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }
            return catalogDB;
        } else {
            System.out.println("Redis ????????????????????????");
            // ????????????....??????
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogDBRedisLock();
        }
    }

    /** ????????? */
    private List<CategoryEntity> getCatalogDBLocalLock() {
        /** ??????????????????????????? */
        synchronized(this) {
            return getCategoryDataFromDB();
        }
    }

    private List<CategoryEntity> getCategoryDataFromDB() {
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        Object allCatalogTree = redisUtils.get(CategoryConstant.ALL_CATALOG_TREE);
        if (StringUtils.isNotEmpty((String) allCatalogTree)) {
            System.out.println("???????????????");
            return JSON.parseObject(allCatalogTree.toString(), new TypeReference<List<CategoryEntity>>() {
            });
        }
        System.out.println("??????DB???");
        // 1.??????????????????
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 2.????????????????????????
        // 2.1 ???????????????????????????
        Stream<CategoryEntity> levelMenus1 = entities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0);
        Stream<CategoryEntity> levelMenusMap1 = levelMenus1.map(categoryEntity -> {
            categoryEntity.setChildren(getChildrens(categoryEntity, entities));
            return categoryEntity;
        });
        Stream<CategoryEntity> sortedMenus = levelMenusMap1.sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()));
        List<CategoryEntity> catalogDB = sortedMenus.collect(Collectors.toList());
        String s = JSON.toJSONString(catalogDB);
        redisUtils.set(CategoryConstant.ALL_CATALOG_TREE, s, 60 * 60);
        return catalogDB;
    }

    /* ???????????????????????????????????? */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId())
                .map(categoryEntity -> {
                    // 1.???????????????
                    categoryEntity.setChildren(getChildrens(categoryEntity,all));
                    return categoryEntity;
                }).sorted((menu1,menu2) -> {
                    // 2.????????????
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
