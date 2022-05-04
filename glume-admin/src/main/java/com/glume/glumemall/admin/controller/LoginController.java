package com.glume.glumemall.admin.controller;

import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 在线用户
 * @author tuoyingtao
 * @create 2022-04-09 16:57
 */
@RestController
@RequestMapping("admin/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    /**
     * 列表
     */
    @GetMapping("/online/list")
    public R list(@RequestParam Map<String,Object> params) {
        PageUtils page = loginService.queryPage(params);
        return R.ok().put("data",page);
    }

    /**
     * 用户强制退出登录
     */
    @GetMapping("/online/forceKikeOut")
    public R forceKikeOut(@RequestParam("id") Integer id, HttpServletRequest request) {
        loginService.forceKikeOut(id, request);
        return R.ok("已踢出下线!");
    }
}
