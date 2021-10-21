package com.glume.glumemall.admin.security;

import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.service.UserRoleService;
import com.glume.glumemall.admin.service.UserService;
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

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getByUserDetail(username);
        if (userEntity.getUsername() == null) {
            throw new UsernameNotFoundException("用户不存在！");
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
