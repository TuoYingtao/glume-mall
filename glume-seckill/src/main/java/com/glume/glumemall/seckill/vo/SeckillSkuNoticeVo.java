package com.glume.glumemall.seckill.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 秒杀活动通知Vo
 * @author tuoyingtao
 * @create 2022-03-24 16:33
 */
@Data
public class SeckillSkuNoticeVo {
    /**
     * id
     */
    private Long id;
    /**
     * member_id
     */
    private Long memberId;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * 活动场次id
     */
    private Long sessionId;
    /**
     * 订阅时间
     */
    private Date subcribeTime;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 通知方式[0-短信，1-邮件]
     */
    private Integer noticeType;
    /**
     * Sku 信息
     */
    private SkuInfoVo skuInfoVo;
    /**
     * 用户信息
     */
    private MemberVo memberVo;
}
