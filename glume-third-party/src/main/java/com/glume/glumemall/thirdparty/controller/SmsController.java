package com.glume.glumemall.thirdparty.controller;

import com.glume.common.core.utils.R;
import com.glume.glumemall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方短信服务
 * @author tuoyingtao
 * @create 2021-12-06 13:42
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsComponent smsComponent;

    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("mobile") String mobile,@RequestParam("code") String code) {
        smsComponent.sendSmsCode(mobile,code);
        return R.ok();
    }
}
