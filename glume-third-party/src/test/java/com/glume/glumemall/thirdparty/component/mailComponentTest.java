package com.glume.glumemall.thirdparty.component;

import com.glume.glumemall.thirdparty.entity.AttachmentEntity;
import com.glume.glumemall.thirdparty.entity.MailEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tuoyingtao
 * @create 2022-03-25 14:58
 */
@SpringBootTest
class mailComponentTest {

    @Autowired
    MailComponent mailComponent;

    @Test
    void sendSimpleMail() {
        MailEntity mailEntity = new MailEntity();
        mailEntity.setSubject("测试邮箱");
        mailEntity.setMailType(3);
        mailEntity.setMailNameTo(Arrays.asList("15207447965@163.com"));
        mailEntity.setMailNameCcTo(Arrays.asList("15207447965@163.com"));
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setName("p01");
        attachmentEntity.setFile(new File("C:/Users/Administrator/Desktop/logo-11-05.png"));
        AttachmentEntity attachmentEntity2 = new AttachmentEntity();
        attachmentEntity2.setName("p02");
        attachmentEntity2.setFile(new File("C:/Users/Administrator/Desktop/微信图片_20210928142357.png"));
        List<AttachmentEntity> attachmentEntities = new ArrayList<>();
        attachmentEntities.add(attachmentEntity);
        attachmentEntities.add(attachmentEntity2);
        mailEntity.setFileData(attachmentEntities);
        mailEntity.setText("<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:p01'/><p>第二张图片：</p><img src='cid:p02'/>");
        mailComponent.sendMail(mailEntity);
    }
}
