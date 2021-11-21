/** Copyright 2020 bejson.com */
package com.glume.glumemall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * sku信息
 * @author TuoYingtao
 * @create 2021-11-19 21:15
 */

@Data
public class Skus {

  private List<Attr> attr;
  /**
   * sku名称
   */
  private String skuName;
  /**
   * 价格
   */
  private BigDecimal price;
  /**
   * 标题
   */
  private String skuTitle;
  /**
   * 副标题
   */
  private String skuSubtitle;
  /**
   * 默认图片
   */
  private List<Images> images;
  private List<String> descar;
  private int fullCount;
  private BigDecimal discount;
  private int countStatus;
  private BigDecimal fullPrice;
  private BigDecimal reducePrice;
  private int priceStatus;
  private List<MemberPrice> memberPrice;


}
