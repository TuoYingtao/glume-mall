/** Copyright 2020 bejson.com */
package com.glume.glumemall.product.vo;

import lombok.Data;

/**
 * @author TuoYingtao
 * @create 2021-11-19 21:15
 */

@Data
public class BaseAttrs {

  /**
   * 属性id
   */
  private Long attrId;
  /**
   * 属性值
   */
  private String attrValues;
  /**
   * 快速展示
   */
  private int showDesc;

}
