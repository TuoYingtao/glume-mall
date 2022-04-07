package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.admin.dao.LoginDao;
import com.glume.glumemall.admin.entity.LoginEntity;
import com.glume.glumemall.admin.service.LoginService;
import org.springframework.stereotype.Service;

/**
 * 在线列表
 * @author tuoyingtao
 * @create 2022-04-06 15:27
 */
@Service("loginService")
public class LoginServiceImpl extends ServiceImpl<LoginDao, LoginEntity> implements LoginService {

}
