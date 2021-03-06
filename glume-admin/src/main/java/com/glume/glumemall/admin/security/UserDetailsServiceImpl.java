package com.glume.glumemall.admin.security;

import com.glume.common.core.enums.UserStatus;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import com.glume.glumemall.admin.service.RoleService;
import com.glume.glumemall.admin.service.UserRoleService;
import com.glume.glumemall.admin.service.UserService;
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

    @Autowired
    RoleService roleService;

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
        UserRoleEntity userRoleEntity = userRoleService.getUserById(userEntity.getUserId());
        RoleEntity roleDetail = roleService.getRoleDetail(userRoleEntity.getRoleId());
        return new AccountUser(
                userEntity.getUserId(),
                roleDetail.getRoleTag(),
                username,
                userEntity.getPassword(),
                getUserAuthority(userEntity.getUserId()));
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
