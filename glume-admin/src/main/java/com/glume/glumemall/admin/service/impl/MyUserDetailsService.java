package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.admin.dao.UserDao;
import com.glume.glumemall.admin.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tuoyingtao
 * @create 2021-10-18 10:13
 */
@Service("userDetailsService")
public class MyUserDetailsService extends ServiceImpl<UserDao,UserEntity> {
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
//        objectQueryWrapper.eq("username",username);
//        UserEntity userEntity = baseMapper.selectOne(objectQueryWrapper);
//        if (userEntity.getUsername() == null) {
//            throw new UsernameNotFoundException("用户不存在！");
//        }
//        List<GrantedAuthority> role = AuthorityUtils.commaSeparatedStringToAuthorityList("role");
//        return new User(userEntity.getUsername(),new BCryptPasswordEncoder().encode(userEntity.getPassword()),role);
//    }
}
