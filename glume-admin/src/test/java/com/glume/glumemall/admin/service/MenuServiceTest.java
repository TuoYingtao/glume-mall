package com.glume.glumemall.admin.service;

import com.glume.common.core.utils.SpringUtils;
import com.glume.glumemall.admin.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tuoyingtao
 * @create 2021-10-22 14:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuServiceTest {
    @Autowired
    MenuService menuService;

    @Test
    public void getMenuList() {
        menuService.getMenuList(1001L,true);
    }

    @Test
    public void userInfo() {
        UserEntity admin = SpringUtils.getBean(UserService.class).getByUserDetail("admin");
        System.out.println(admin);
    }
}
