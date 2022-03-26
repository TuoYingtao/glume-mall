package com.glume.glumemall.thirdparty.component;

import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.thirdparty.entity.AttachmentEntity;
import com.glume.glumemall.thirdparty.entity.MailEntity;
import com.sun.xml.internal.ws.api.message.Attachment;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tuoyingtao
 * @create 2022-03-25 14:53
 */
@Component
public class MailComponent {

    @Value("${spring.mail.username}")
    private static String SENDER;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    /**
     * 邮件信息构建
     * @param messageHelper 邮件对象
     * @param mailEntity 邮件实体信息
     * @throws MessagingException
     */
    private void buildModle(MimeMessageHelper messageHelper,MailEntity mailEntity) throws MessagingException {
        // 设置邮件发送者
        messageHelper.setFrom(MailComponent.SENDER);
        // 设置邮件主题
        messageHelper.setSubject(mailEntity.getSubject());
        // 设置邮件发送日期
        messageHelper.setSentDate(new Date());
        // 设置邮件的正文
        messageHelper.setText(mailEntity.getText());
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似：message.setTo("10*****16@qq.com","12****32*qq.com");
        if (StringUtils.isNotEmpty(mailEntity.getMailNameTo())) {
            messageHelper.setTo(mailEntity.getMailNameTo().toArray(new String[mailEntity.getMailNameTo().size()]));
        }
        // 设置邮件抄送人，可以有多个抄送人
        if (StringUtils.isNotEmpty(mailEntity.getMailNameCcTo())) {
            messageHelper.setCc(mailEntity.getMailNameCcTo().toArray(new String[mailEntity.getMailNameCcTo().size()]));
        }
        // 设置隐秘抄送人，可以有多个
        if (StringUtils.isNotEmpty(mailEntity.getMailNameBccTo())) {
            messageHelper.setBcc(mailEntity.getMailNameBccTo().toArray(new String[mailEntity.getMailNameBccTo().size()]));
        }
        // 设置带有附件的
        if (mailEntity.getMailType() != 1 && (StringUtils.isNotEmpty(mailEntity.getFileData()) || StringUtils.isNotEmpty(mailEntity.getVariable()))) {
            addFileTypeHandler(messageHelper,mailEntity);
        }
    }

    /**
     * 邮件附件信息处理
     * @param messageHelper 邮件对象
     * @param mailEntity 邮件实体信息
     * @throws MessagingException
     */
    private void addFileTypeHandler(MimeMessageHelper messageHelper, MailEntity mailEntity) throws MessagingException {
        switch (mailEntity.getMailType()) {
            case 2: attachmentHandler(messageHelper, mailEntity.getFileData()); break;
            case 3: inlineHandler(messageHelper, mailEntity.getFileData()); break;
            case 4: templateHandler(messageHelper,mailEntity.getVariableName(), mailEntity.getVariable()); break;
        }
    }

    /**
     * thymeleaf 邮件
     * @param messageHelper 邮件对象
     * @param variableName 模板名称
     * @param variable 模板数据
     * @throws MessagingException
     */
    private void templateHandler(MimeMessageHelper messageHelper, String variableName, Map<String,String> variable) throws MessagingException {
        Context context = new Context();
        Set<Map.Entry<String, String>> entries = variable.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            context.setVariable(entry.getKey(),entry.getValue());
        }
        String process = templateEngine.process(variableName, context);
        messageHelper.setText(process,true);
    }

    /**
     * 图片邮件
     * @param messageHelper 邮件对象
     * @param fileData 附件数据
     * @throws MessagingException
     */
    private void inlineHandler(MimeMessageHelper messageHelper, List<AttachmentEntity> fileData) throws MessagingException {
        for (AttachmentEntity entry : fileData) {
            messageHelper.addInline(entry.getName(),new FileSystemResource(entry.getFile()));
        }
    }

    /**
     * 附件邮件
     * @param messageHelper 邮件对象
     * @param fileData 附件数据
     * @throws MessagingException
     */
    private void attachmentHandler(MimeMessageHelper messageHelper, List<AttachmentEntity> fileData) throws MessagingException {
        for (AttachmentEntity entry : fileData) {
            messageHelper.addAttachment(entry.getName(),entry.getFile());
        }
    }

    /**
     * 发送邮件
     * @param mailEntity 邮件实体
     */
    public void sendMail(MailEntity mailEntity) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = mailEntity.getMailType() == 1
                    ? new MimeMessageHelper(mimeMessage)
                    : new MimeMessageHelper(mimeMessage,true);
            buildModle(messageHelper,mailEntity);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通邮件
     * @param mailEntity 邮件实体
     * @throws MessagingException
     */
    @Deprecated
    public void sendSimpleMail(MailEntity mailEntity) throws MessagingException {
        // 构建一个邮箱对象
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置邮件主题
        simpleMailMessage.setSubject("这是一封测试邮件");
        // 设置邮件发送者
        simpleMailMessage.setFrom(MailComponent.SENDER);
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // message.setTo("10*****16@qq.com","12****32*qq.com");
        simpleMailMessage.setTo(mailEntity.getMailNameTo().toArray(new String[mailEntity.getMailNameTo().size()]));
        // 设置邮件抄送人，可以有多个抄送人
        simpleMailMessage.setCc(mailEntity.getMailNameCcTo().toArray(new String[mailEntity.getMailNameCcTo().size()]));
        // 设置隐秘抄送人，可以有多个
        simpleMailMessage.setBcc(mailEntity.getMailNameBccTo().toArray(new String[mailEntity.getMailNameBccTo().size()]));
        // 设置邮件发送日期
        simpleMailMessage.setSentDate(new Date());
        // 设置邮件的正文
        simpleMailMessage.setText(mailEntity.getText());
        // 发送邮件
        javaMailSender.send(simpleMailMessage);
    }

    /**
     * 附件邮件
     * @param mailEntity 邮件实体
     * @throws MessagingException
     */
    @Deprecated
    public void sendAttachFileMail(MailEntity mailEntity) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // true表示构建一个可以带附件的邮件对象
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        // 设置邮件主题
        messageHelper.setSubject("这是一封测试邮件");
        // 设置邮件发送者
        messageHelper.setFrom(MailComponent.SENDER);
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        messageHelper.setTo(MailComponent.SENDER);
        // 设置邮件发送日期
        messageHelper.setSentDate(new Date());
        // 设置邮件的正文
        messageHelper.setText("这是测试邮件的正文");
        // 第一个参数是自定义的名称，后缀需要加上，第二个参数是文件的位置
        messageHelper.addAttachment("资料.xlsx",new File("/Users/gamedev/Desktop/测试数据 2.xlsx"));
        javaMailSender.send(mimeMessage);
    }

    /**
     * 图片邮件
     * @param mailEntity 邮件实体
     * @throws MessagingException
     */
    @Deprecated
    public void sendImgResMail(MailEntity mailEntity) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        // 设置邮件主题
        messageHelper.setSubject("这是一封测试邮件");
        // 设置邮件发送者
        messageHelper.setFrom(MailComponent.SENDER);
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        messageHelper.setTo(MailComponent.SENDER);
        // 设置邮件发送日期
        messageHelper.setSentDate(new Date());
        // src='cid:p01' 占位符写法 ，第二个参数true表示这是一个html文本
        messageHelper.setText("<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:p01'/><p>第二张图片：</p><img src='cid:p02'/>",true);
        // 第一个参数指的是html中占位符的名字，第二个参数就是文件的位置
        messageHelper.addInline("p01",new FileSystemResource(new File("/Users/gamedev/Desktop/压缩.jpeg")));
        messageHelper.addInline("p02",new FileSystemResource(new File("/Users/gamedev/Desktop/瑞文.jpg")));
        javaMailSender.send(mimeMessage);
    }

    /**
     * 模板邮件
     * @param mailEntity 邮件实体
     * @throws MessagingException
     */
    @Deprecated
    public void sendThymeleafMail(MailEntity mailEntity) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom(MailComponent.SENDER);
        helper.setTo(MailComponent.SENDER);
        helper.setSentDate(new Date());
        // 这里引入的是Template的Context
        Context context = new Context();
        // 设置模板中的变量
        context.setVariable("title", "iPhone");
        // 第一个参数为模板的名称
        String process = templateEngine.process("mail.html", context);
        // 第二个参数true表示这是一个html文本
        helper.setText(process,true);
        javaMailSender.send(mimeMessage);
    }
}
