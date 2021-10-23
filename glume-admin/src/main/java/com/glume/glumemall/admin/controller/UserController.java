package com.glume.glumemall.admin.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.lang.UUID;
import com.glume.glumemall.admin.util.JwtUtils;
import com.glume.glumemall.admin.util.RedisUtils;
import com.glume.glumemall.common.constant.RedisConstant;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.service.UserService;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.R;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;


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

    @Value("${jwt.header}")
    private String headerToken;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private Producer producer;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R userInfo(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(headerToken);
        String userName = jwtUtils.getUserNameFromToken(token);
        HashMap<String, Object> byUserDetail = new HashMap<>();
        byUserDetail.putAll(userService.getByUserInfoAndMenu(userName));
        return R.ok().put("code",200)
                .put("data",byUserDetail);
    }

    @ApiOperation(value = "登录验证码",notes = "")
    @GetMapping("/captcha")
    public R captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        key = "123123";
        code = "12345";

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisUtils.hset(RedisConstant.CAPTCHA_KEY,key,code,120);
        HashMap<String, Object> map = new HashMap<>();
        map.put("key",key);
        map.put("image",base64Img);
        return R.ok()
                .put("code",200)
                .put("data",map);
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
        return R.ok()
                .put("data",map)
                .put("code",200);
    }

    /**
     * 列表
     */
    @PreAuthorize("hasRole('admin')")
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    public R info(@PathVariable("userId") Long userId){
		UserEntity user = userService.getById(userId);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user){
		userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user){
		userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] userIds){
		userService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }

}
