package com.glume.glumemall.admin.security;

import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.service.UserRoleService;
import com.glume.glumemall.admin.service.UserService;
import com.glume.glumemall.common.enums.UserStatus;
import com.glume.glumemall.common.exception.servlet.ServiceException;
import com.glume.glumemall.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tuoyingtao
 * @create 2021-10-18 10:13
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getByUserDetail(username);
        if (StringUtils.isNull(userEntity)) {
            LOGGER.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：不存在！");
        } else if (UserStatus.DELETED.getCode().equals(userEntity.getStatus())){
            LOGGER.info("登录用户：{} 已被删除.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(userEntity.getStatus())) {
            LOGGER.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        return new AccountUser(userEntity.getUsername(), userEntity.getPassword(),getUserAuthority(userEntity.getUserId()));
    }

    /**
     * 获取用户权限信息
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        //角色菜单权限
        String userAuthorityInfo = userRoleService.getUserAuthorityInfo(userId);
        List<GrantedAuthority> role = AuthorityUtils.commaSeparatedStringToAuthorityList(userAuthorityInfo);
        return role;
    }
}
