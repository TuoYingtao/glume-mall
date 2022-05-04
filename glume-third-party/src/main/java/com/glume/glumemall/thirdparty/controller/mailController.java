package com.glume.glumemall.thirdparty.controller;

import com.glume.common.core.utils.R;
import com.glume.glumemall.thirdparty.component.MailComponent;
import com.glume.glumemall.thirdparty.entity.MailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tuoyingtao
 * @create 2022-03-26 16:25
 */
@RestController
public class mailController {

    @Autowired
    MailComponent mailComponent;

    @PostMapping("/send/mail")
    public R send(@RequestBody MailEntity mailEntity) {
        mailComponent.sendMail(mailEntity);
        return R.ok("邮件发送成功！");
    }
}
