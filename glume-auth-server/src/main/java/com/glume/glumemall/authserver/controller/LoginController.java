package com.glume.glumemall.authserver.controller;

import com.glume.common.core.constant.AuthServerConstant;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.authserver.feign.ThirdPartFeignService;
import com.glume.glumemall.authserver.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author tuoyingtao
 * @create 2021-12-06 10:18
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartFeignService thirdPartFeignService;

    @Autowired
    RedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("mobile") String mobile) {
        String redisMobileCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + mobile).toString();
        if (!StringUtils.isNotNull(redisMobileCode)) {
            long redisMobileCodeTime = Long.parseLong(redisMobileCode.split("_")[1]);
            if (System.currentTimeMillis() - redisMobileCodeTime < (60 * 1000)) {
                return R.error(HttpStatus.BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), HttpStatus.BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        String code = UUID.randomUUID().toString().substring(0, 5) + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + mobile,code,5, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(mobile,code);
        return R.ok();
    }

    /**
     *
     * @param userRegisterVo
     * @param result
     * @param redirectAttributes 重定向传递数据
     * @return
     */
    @PostMapping("/register")
    public String register(@Validated UserRegisterVo userRegisterVo, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.glumemall.com/register.html";
        }
        return "redirect:/login.html";
    }
}
