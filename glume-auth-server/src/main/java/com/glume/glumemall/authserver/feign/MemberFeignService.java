package com.glume.glumemall.authserver.feign;

import com.glume.common.core.utils.R;
import com.glume.glumemall.authserver.vo.UserLoginVo;
import com.glume.glumemall.authserver.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author tuoyingtao
 * @create 2021-12-07 11:38
 */
@FeignClient("glume-member")
public interface MemberFeignService {

    @PostMapping("/member/member/register")
    public R register(@RequestBody UserRegisterVo userRegisterVo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVo userLoginVo);
}
