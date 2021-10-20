package com.glume.glumemall.admin.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.admin.dao.UserDao;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.service.RoleMenuService;
import com.glume.glumemall.admin.service.UserRoleService;
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
public class UserDetailsServiceImpl extends ServiceImpl<UserDao,UserEntity> implements UserDetailsService {

    @Autowired
    UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("username",username);
        UserEntity userEntity = baseMapper.selectOne(objectQueryWrapper);
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
