package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.dao.LoginDao;
import com.glume.glumemall.admin.entity.BlackListEntity;
import com.glume.glumemall.admin.entity.LoginEntity;
import com.glume.glumemall.admin.security.AccountUser;
import com.glume.glumemall.admin.service.BlackListService;
import com.glume.glumemall.admin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * 在线列表
 * @author tuoyingtao
 * @create 2022-04-06 15:27
 */
@Service("loginService")
public class LoginServiceImpl extends ServiceImpl<LoginDao, LoginEntity> implements LoginService {

    @Value("${jwt.header}")
    private String headerToken;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BlackListService blackListService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<LoginEntity> wrapper = new QueryWrapper<>();
        String username = (String) params.get("username");
        if (StringUtils.isNotNull(username)) {
            wrapper.eq("username", username);
        }
        IPage<LoginEntity> page = this.page(new Query<LoginEntity>().getPage(params), wrapper);


        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forceKikeOut(Integer id, HttpServletRequest request) {
        String token = request.getHeader(headerToken);
        Integer operateUserId = (Integer) jwtUtils.getTokenBody(AccountUser.UserFields.USER_ID.getMessage(), token);
        LoginEntity loginEntity = baseMapper.selectById(id);
        BlackListEntity blackListEntity = new BlackListEntity();
        blackListEntity.setKickOutId(operateUserId);
        blackListEntity.setToken(loginEntity.getToken());
        blackListEntity.setIp(loginEntity.getIp());
        blackListEntity.setUsername(loginEntity.getUsername());
        blackListEntity.setLoginOutTime(new Date());
        blackListService.save(blackListEntity);
        baseMapper.deleteById(id);
    }
}
