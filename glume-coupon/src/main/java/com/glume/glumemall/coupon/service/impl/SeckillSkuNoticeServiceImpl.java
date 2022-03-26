package com.glume.glumemall.coupon.service.impl;

import com.glume.common.core.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.coupon.dao.SeckillSkuNoticeDao;
import com.glume.glumemall.coupon.entity.SeckillSkuNoticeEntity;
import com.glume.glumemall.coupon.service.SeckillSkuNoticeService;


@Service("seckillSkuNoticeService")
public class SeckillSkuNoticeServiceImpl extends ServiceImpl<SeckillSkuNoticeDao, SeckillSkuNoticeEntity> implements SeckillSkuNoticeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSkuNoticeEntity> page = this.page(
                new Query<SeckillSkuNoticeEntity>().getPage(params),
                new QueryWrapper<SeckillSkuNoticeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSkuNoticeEntity> currentSendNotice() {
        QueryWrapper<SeckillSkuNoticeEntity> wrapper = new QueryWrapper<>();
        wrapper.between("send_time",startTime(),emdTime());
        List<SeckillSkuNoticeEntity> list = baseMapper.selectList(wrapper);
        return list;
    }

    private String emdTime() {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.MAX);
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String startTime() {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
