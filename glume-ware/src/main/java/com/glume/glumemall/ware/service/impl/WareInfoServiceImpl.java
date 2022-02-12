package com.glume.glumemall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.ware.feign.MemberFeignService;
import com.glume.glumemall.ware.vo.MemberAddressVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.ware.dao.WareInfoDao;
import com.glume.glumemall.ware.entity.WareInfoEntity;
import com.glume.glumemall.ware.service.WareInfoService;

import javax.annotation.Resource;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Resource
    MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotNull(params.get("key"))) {
            String key = params.get("key").toString();
            wrapper.eq("id",key)
                    .or().like("name",key)
                    .or().like("address",key)
                    .or().like("areacode",key);
        }
        IPage<WareInfoEntity> page = this.page(new Query<WareInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public BigDecimal getFare(Long addrId) {
        R info = memberFeignService.info(addrId);
        Map<String,MemberAddressVo> map = info.getData(new TypeReference<Map<String,MemberAddressVo>>(){});
        MemberAddressVo data = map.get("memberReceiveAddress");
        if (StringUtils.isNotNull(data)) {
            // TODO 使用第三方接口来计算运费 （临时处理：使用手机号最后一位做为运费）
            String phone = data.getPhone();
            String substring = phone.substring(phone.length() - 1, phone.length());
            return new BigDecimal(substring);
        }
        return null;
    }

}
