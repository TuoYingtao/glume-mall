package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.admin.entity.OssEntity;

import java.util.Map;

/**
 * 文件上传
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
public interface OssService extends IService<OssEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

