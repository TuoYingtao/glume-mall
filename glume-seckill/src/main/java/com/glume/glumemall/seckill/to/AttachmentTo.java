package com.glume.glumemall.seckill.to;

import lombok.Data;

import java.io.File;

/**
 * 附件To
 * @author tuoyingtao
 * @create 2022-03-28 15:10
 */
@Data
public class AttachmentTo {

    /**
     * 名称
     */
    private String name;
    /**
     * 文件
     */
    private File file;
}
