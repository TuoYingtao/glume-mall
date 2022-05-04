package com.glume.glumemall.admin.vo;

import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.entity.UserEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 用户信息以及它的菜单
 * @author tuoyingtao
 * @create 2022-04-09 11:32
 */
public class UserInfoAndMenuVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private UserEntity info;

    private List<MenuEntity> menus;

    public UserInfoAndMenuVo() {
    }

    public UserInfoAndMenuVo(UserEntity info, List<MenuEntity> menus) {
        this.info = info;
        this.menus = menus;
    }

    public UserEntity getInfo() {
        return info;
    }

    public void setInfo(UserEntity info) {
        this.info = info;
    }

    public List<MenuEntity> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuEntity> menus) {
        this.menus = menus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoAndMenuVo that = (UserInfoAndMenuVo) o;
        return Objects.equals(info, that.info) && Objects.equals(menus, that.menus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(info, menus);
    }

    @Override
    public String toString() {
        return "UserInfoAndMenuVo{" +
                "userEntity=" + info +
                ", menuEntities=" + menus +
                '}';
    }
}
