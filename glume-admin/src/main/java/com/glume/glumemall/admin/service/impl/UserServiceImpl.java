package com.glume.glumemall.admin.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.constant.RedisConstant;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.RedisUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.service.MenuService;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.admin.dao.UserDao;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.service.UserService;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    MenuService menuService;

    @Autowired
    Producer producer;

    @Value("${jwt.header}")
    String headerToken;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取用户信息以及它的菜单
     * @param username
     * @return
     */
    @Override
    public HashMap<String, Object> getByUserInfoAndMenu(String username) {
        QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.select("user_id","username","email","mobile","status","create_time")
                .eq("username",username);
        UserEntity userEntity = baseMapper.selectOne(objectQueryWrapper);
        List<MenuEntity> menuList = menuService.getMenuList(userEntity.getUserId(),true);
        HashMap<String, Object> map = new HashMap<>();
        map.put("info",userEntity);
        map.put("menus",menuList);
        return map;
    }

    /**
     * Security 通过用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public UserEntity getByUserDetail(String username) {
        QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("username",username);
        UserEntity userEntity = baseMapper.selectOne(objectQueryWrapper);
        return userEntity;
    }

    /**
     * 修改用户信息
     * @param userEntity
     * @return
     */
    @Override
    public void updateUserDetail(@NotNull UserEntity userEntity) {
        String password = userEntity.getPassword();
        String encodePassword = new BCryptPasswordEncoder().encode(password);
        userEntity.setPassword(encodePassword);
        Integer row = baseMapper.updateById(userEntity);
        if (row == 0) {
            throw new ServiceException("更新失败！");
        }
    }

    /** 生成图片验证码 */
    @Override
    public Map<String, String> createCaptcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        SpringUtils.getBean(RedisUtils.class).hset(RedisConstant.CAPTCHA_KEY,key,code,120);
        Map<String, String> map = new HashMap<>();
        map.put("key",key);
        map.put("image",base64Img);
        return map;
    }

    /** 获取用户信息 */
    @Override
    public Map<String, Object> info(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(headerToken);
        String userName = jwtUtils.getUserNameFromToken(token);
        Map<String, Object> byUserDetail = new HashMap<>();
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        if (redisUtils.hHasKey(RedisConstant.USER_INFO_AND_MENU,userName)) {
            String hget = redisUtils.hget(RedisConstant.USER_INFO_AND_MENU, userName);
            Map<String, Object> map = JSON.parseObject(hget, new TypeReference<Map<String, Object>>() {
            });
            byUserDetail.putAll(map);
            LOGGER.info("Redis中获取用户信息：{}",hget);
        } else {
            byUserDetail.putAll(this.getByUserInfoAndMenu(userName));
            long outTime = new Date(System.currentTimeMillis() + (60 * 60 * 2)).getTime() / 1000;
            String toJSONString = JSON.toJSONString(byUserDetail);
            redisUtils.hset(RedisConstant.USER_INFO_AND_MENU,userName,toJSONString,outTime);
            LOGGER.info("MySQL中获取用户信息");
        }
        return byUserDetail;
    }

}
