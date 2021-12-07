package com.glume.glumemall.authserver.controller;

import com.glume.common.core.constant.AuthServerConstant;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.authserver.feign.MemberFeignService;
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

import java.util.HashMap;
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

    @Autowired
    MemberFeignService memberFeignService;

    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("mobile") String mobile) {
        if (StringUtils.isNotNull(redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + mobile))) {
            String redisMobileCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + mobile).toString();
            long redisMobileCodeTime = Long.parseLong(redisMobileCode.split("_")[1]);
            if (System.currentTimeMillis() - redisMobileCodeTime < (60 * 1000)) {
                return R.error(HttpStatus.BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), HttpStatus.BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        String code = UUID.randomUUID().toString().replaceAll("[a-zA-Z]","").substring(0, 5);
        System.out.println(code);
        String redisCode = code + "_" + System.currentTimeMillis();
        System.out.println(redisCode.getClass());
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + mobile,redisCode,60, TimeUnit.MINUTES);
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
        String code = userRegisterVo.getCode();
        if (StringUtils.isNotNull(redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getMobile()))) {
            String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getMobile()).toString();
            if (code.equals(s.split("_")[0])) {
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getMobile());
                R register = memberFeignService.register(userRegisterVo);
                if (register.getCode() == 200) {
                    return "redirect:http://auth.glumemall.com/login.html";
                } else {
                    Map<String, Object> errors = new HashMap<>();
                    errors.put("msg",register.get("msg").toString());
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.glumemall.com/register.html";
                }
            } else {
                Map<String, Object> errors = new HashMap<>();
                redirectAttributes.addFlashAttribute("errors",errors);
                return "redirect:http://auth.glumemall.com/register.html";
            }
        } else {
            Map<String, Object> errors = new HashMap<>();
            errors.put("msg","验证码不正确！");
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.glumemall.com/register.html";
        }
    }
}
