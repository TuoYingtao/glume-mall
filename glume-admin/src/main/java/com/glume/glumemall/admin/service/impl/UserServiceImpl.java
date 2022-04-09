package com.glume.glumemall.admin.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.constant.RedisConstant;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.RedisUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.dao.UserDao;
import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.security.AccountUser;
import com.glume.glumemall.admin.service.MenuService;
import com.glume.glumemall.admin.service.RoleMenuService;
import com.glume.glumemall.admin.service.RoleService;
import com.glume.glumemall.admin.service.UserService;
import com.glume.glumemall.admin.vo.UserInfoAndMenuVo;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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
    RoleService roleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RedisTemplate redisTemplate;

    private final long expire = 60 * 60 * 24 * 7;

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
    public UserInfoAndMenuVo getByUserInfoAndMenu(String username) {
        QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.select("user_id","username","email","mobile","status","create_time")
                .eq("username",username);
        UserEntity userEntity = baseMapper.selectOne(objectQueryWrapper);
        List<MenuEntity> menuList = menuService.getMenuList(userEntity.getUserId(),true);
        UserInfoAndMenuVo userInfoAndMenuVo = new UserInfoAndMenuVo(userEntity, menuList);
        return userInfoAndMenuVo;
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
        String roleTagKey = (String) jwtUtils.getTokenBody(AccountUser.UserFields.ROLE_TAG.getMessage(), token);
        Map<String, Object> map = new HashMap<>();
        BoundHashOperations hashOps = redisTemplate.boundHashOps(RedisConstant.USER_INFO);
        BoundHashOperations roleHashOps = redisTemplate.boundHashOps(RedisConstant.ROLE_MENU);
        if (hashOps.hasKey(userName) && roleHashOps.hasKey(roleTagKey)) {
            String userinfoResult = (String) hashOps.get(userName);
            String menuResult = (String) roleHashOps.get(roleTagKey);
            UserEntity userEntity = JSON.parseObject(userinfoResult, UserEntity.class);
            List<MenuEntity> menuEntityList = JSON.parseObject(menuResult, new TypeReference<List<MenuEntity>>() {
            });
            map.put("info",userEntity);
            map.put("menus",menuEntityList);
            LOGGER.info("Redis中获取用户信息：{}",map);
        } else {
            LOGGER.info("MySQL中获取用户信息");
            UserInfoAndMenuVo userInfoAndMenuVo = this.getByUserInfoAndMenu(userName);
            if (!hashOps.hasKey(userName)) {
                hashOps.put(userName,JSON.toJSONString(userInfoAndMenuVo.getInfo()));
                hashOps.expire(expire,TimeUnit.SECONDS);
            }
            if (!roleHashOps.hasKey(roleTagKey)) {
                roleHashOps.put(roleTagKey,JSON.toJSONString(userInfoAndMenuVo.getMenus()));
                roleHashOps.expire(expire,TimeUnit.SECONDS);
            }
            map.put("info",userInfoAndMenuVo.getInfo());
            map.put("menus",userInfoAndMenuVo.getMenus());
        }
        return map;
    }

}
