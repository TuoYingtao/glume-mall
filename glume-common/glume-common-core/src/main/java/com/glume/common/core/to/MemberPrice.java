/** Copyright 2020 bejson.com */
package com.glume.common.core.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author TuoYingtao
 * @create 2021-11-19 21:15
 */

@Data
public class MemberPrice {

  private Long id;
  private String name;
  private BigDecimal price;

}
