package com.glume.glumemall.thirdparty.entity;

import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * @author tuoyingtao
 * @create 2022-03-26 11:26
 */
@Data
public class MailEntity implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 邮箱主题
     */
    private String subject;
    /**
     * 接收者
     */
    private List<String> mailNameTo;
    /**
     * 邮件抄送人
     */
    private List<String> mailNameCcTo;
    /**
     * 隐秘抄送人
     */
    private List<String> mailNameBccTo;
    /**
     * 邮件发送日期
     */
    private Date sendDate;
    /**
     * 邮件的正文
     */
    private String text;
    /**
     * 邮件类型 1-普通邮件 2-文件邮件 3-图片邮件 4-模板邮件
     */
    private Integer mailType = 1;
    /**
     * 文件
     */
    private List<AttachmentEntity> fileData;
    /**
     * 模板名
     */
    private String variableName;
    /**
     * 模板参数
     */
    private Map<String,String> variable;
}
