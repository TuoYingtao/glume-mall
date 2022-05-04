package com.glume.glumemall.coupon.service.impl;

import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.coupon.entity.SeckillSkuRelationEntity;
import com.glume.glumemall.coupon.service.SeckillSkuRelationService;
import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.coupon.dao.SeckillSessionDao;
import com.glume.glumemall.coupon.entity.SeckillSessionEntity;
import com.glume.glumemall.coupon.service.SeckillSessionService;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    @Autowired
    SeckillSkuRelationService seckillSkuRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SeckillSessionEntity> wrapper = new QueryWrapper<>();
        String name = (String) params.get("name");
        String status = (String) params.get("status");
        String dateTime = (String) params.get("dateTime");
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("name", name);
        }
        if (StringUtils.isNotEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (StringUtils.isNotEmpty(dateTime)) {
            String[] split = dateTime.split(",");
            wrapper.between("start_time", split[0], split[1]);
        }
        IPage<SeckillSessionEntity> page = this.page(new Query<SeckillSessionEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getLates3DaySession() {
        List<SeckillSessionEntity> list = baseMapper.selectList(new QueryWrapper<SeckillSessionEntity>()
                .between("start_time", startTime(), endTime()));
        if (StringUtils.isNotNull(list)) {
            List<SeckillSessionEntity> sessionEntityList = list.stream().map(seckillSessionEntity -> {
                List<SeckillSkuRelationEntity> relationEntities = seckillSkuRelationService.list(new QueryWrapper<SeckillSkuRelationEntity>()
                        .eq("promotion_session_id", seckillSessionEntity.getId()));
                seckillSessionEntity.setRelationSkus(relationEntities);
                return seckillSessionEntity;
            }).collect(Collectors.toList());
            return sessionEntityList;
        }
        return null;
    }

    @Override
    public void saveSeckillSession(SeckillSessionEntity seckillSession) {
        seckillSession.setStartTime(seckillSession.getDateTime()[0]);
        seckillSession.setEndTime(seckillSession.getDateTime()[1]);
        seckillSession.setCreateTime(new Date());
        baseMapper.insert(seckillSession);
    }

    private String startTime() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    private String endTime() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.MAX);
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    @Transactional
    public void removeSessionById(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        seckillSkuRelationService.deleteBatchSkuRelation("promotion_session_id",ids);
    }

}
