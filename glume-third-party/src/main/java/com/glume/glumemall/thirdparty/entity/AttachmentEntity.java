package com.glume.glumemall.thirdparty.entity;

import lombok.Data;

import java.io.File;

/**
 * @author tuoyingtao
 * @create 2022-03-26 17:27
 */
@Data
public class AttachmentEntity {

    /**
     * 名称
     */
    private String name;
    /**
     * 文件
     */
    private File file;
}
