package com.glume.glumemall.authserver.controller;

import cn.hutool.http.HttpUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

/**
 * 社交登录
 * @author tuoyingtao
 * @create 2021-12-08 11:36
 */
public class OAuth2Controller {

    // TODO 微博社交登录待完成
    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@PathVariable("code") String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("client_id","");        // 应用ID
        map.put("client_secret","");    // 应用secret
        map.put("grant_type","");       // 授权方式
        map.put("redirect_uri","");     // 授权页回调地址
        map.put("code",code);           // 授权页回调地址
        // 根据code换取accessToken
        HttpUtil.post("http://api.weibo.com/oauth2/access_token",map);
        return "redirect:http://glumemall.com";
    }
}
