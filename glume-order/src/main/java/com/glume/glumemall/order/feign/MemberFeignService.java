package com.glume.glumemall.order.feign;

import com.glume.glumemall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author tuoyingtao
 * @create 2021-12-16 10:55
 */
@FeignClient("glume-member")
public interface MemberFeignService {

    @GetMapping("/member/memberreceiveaddress/{memberId}/address")
    List<MemberAddressVo> getMemberAddressList(@PathVariable("memberId") Long memberId);
}
