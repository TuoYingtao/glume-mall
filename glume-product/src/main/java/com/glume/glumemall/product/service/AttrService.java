package com.glume.glumemall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.product.entity.AttrEntity;
import com.glume.glumemall.product.vo.AttrGroupRelationVo;
import com.glume.glumemall.product.vo.AttrRespVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    void removeAttrByIds(List<Long> attrIds);

    AttrRespVo getInfoById(Long attrId);

    void updateAttrById(AttrRespVo attrVo);

    void AttrSave(AttrRespVo attrVo);

    List<AttrEntity> getRelationAttr(Long attrGroupId);

    void deleteRelation(AttrGroupRelationVo[] attrGroupVo);
}

