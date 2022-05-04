package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.glumemall.admin.entity.BlackListEntity;

/**
 * 黑名单表
 * @author tuoyingtao
 * @create 2022-04-06 15:12
 */
public interface BlackListService extends IService<BlackListEntity> {

    void userTokenVerify(String token, String username);
}
