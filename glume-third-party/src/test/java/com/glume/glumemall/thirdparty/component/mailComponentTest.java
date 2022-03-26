package com.glume.glumemall.thirdparty.component;

import com.glume.glumemall.thirdparty.entity.MailEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

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
        mailEntity.setSubject("主题");
        List<String> strings = new ArrayList<>();
        strings.add("15207447965@163.com");
        strings.add("tuoyingtao@163.com");
        mailEntity.setMailNameTo(strings);
        mailEntity.setText("测试邮件");
        try {
            mailComponent.sendSimpleMail(mailEntity);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
