package com.glume.glumemall.admin.scheduled.quartz.test;

import com.glume.common.core.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务调度测试
 * @author tuoyingtao
 * @create 2022-04-27 11:31
 */
@Component("jobTest")
public class JobTest {

    public void jobMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void jobParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void jobNoParams() {
        System.out.println(LocalDateTime.now() + "执行无参方法");
    }
}
