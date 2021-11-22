/** Copyright 2020 bejson.com */
package com.glume.glumemall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author TuoYingtao
 * @create 2021-11-19 21:15
 */

@Data
public class SpuSaveVo {

  /**
   * 商品名称
   */
  private String spuName;
  /**
   * 商品描述
   */
  private String spuDescription;
  /**
   * 所属分类id
   */
  private Long catalogId;
  /**
   * 品牌id
   */
  private Long brandId;
  /**
   * 重量
   */
  private BigDecimal weight;
  /**
   * 上架状态[0 - 下架，1 - 上架]
   */
  private int publishStatus;
  /**
   * 商品介绍
   */
  private List<String> decript;
  /**
   * spu图片
   */
  private List<String> images;

  /**
   * 积分
   */
  private Bounds bounds;
  /**
   * spu属性值
   */
  private List<BaseAttrs> baseAttrs;
  /**
   * sku基本信息
   */
  private List<Skus> skus;


}
