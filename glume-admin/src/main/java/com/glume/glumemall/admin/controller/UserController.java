package com.glume.glumemall.admin.controller;

import com.glume.common.core.annotation.valid.UpdateGroup;
import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 系统用户
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@RestController
@RequestMapping("admin/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R userInfo(HttpServletRequest httpServletRequest) {
        Map<String,Object> map = userService.info(httpServletRequest);

        return R.ok().put("data",map);
    }

    @ApiOperation(value = "登录验证码",notes = "")
    @GetMapping("/captcha")
    public R captcha() throws IOException {
        Map<String,String> map = userService.createCaptcha();
        return R.ok().put("data",map);
    }

    @ApiOperation(value = "测试密码加密",notes = "开发测试")
    @ApiImplicitParam(name = "password",value = "测试密码",required = true,dataType = "String")
    @GetMapping("/test/password")
    public R createdPassword(@RequestParam("password") String password) {
        String paw = bCryptPasswordEncoder.encode(password);
        boolean matches = bCryptPasswordEncoder.matches(password,paw);
        System.out.println("密码匹配结果：" + matches);
        HashMap<String, Object> map = new HashMap<>();
        map.put("password",paw);
        return R.ok().put("data",map);
    }

    /**
     * 用户名查找用户信息
     */
    @GetMapping("/byUserDetail")
    public R getByUserDetail(@RequestParam("username") String username) {
        UserEntity userEntity = userService.getByUserDetail(username);
        return R.ok().put("data",userEntity);
    }

    @PostMapping("/reset/{userId}/password")
    public R resetPassword(@PathVariable("userId") Long userId, String password) {
        userService.resetPassword(userId, password);
        return R.ok("密码已重置！");
    }

    /**
     * 列表
     */
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{userId}")
    public R info(@PathVariable("userId") Long userId){
		UserEntity user = userService.getById(userId);

        return R.ok().put("data", user);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(UserEntity user, HttpServletRequest request){
		userService.saveUserInfo(user,request);

        return R.ok("保存成功！");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户ID",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "username",value = "用户名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "email",value = "邮箱",required = true,dataType = "String"),
            @ApiImplicitParam(name = "mobile",value = "手机号",required = true,dataType = "String"),
            @ApiImplicitParam(name = "status",value = "状态",dataType = "Integer"),
    })
    public R update(@Validated(UpdateGroup.class) UserEntity userEntity){
        userService.updateUserDetail(userEntity);
        return R.ok("更新成功！");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{userIds}")
    public R delete(@PathVariable("userIds") Long[] userIds){
		userService.removeByIds(Arrays.asList(userIds));

        return R.ok("删除成功！");
    }

}
