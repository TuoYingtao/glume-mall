package com.glume.glumemall.admin.scheduled.asyncpool.factory;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.glume.glumemall.admin.entity.LoginEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 * @author tuoyingtao
 * @create 2022-03-10 17:16
 */
public class AsyncFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFactory.class);


    /**
     * 记录登录信息
     *
     * @param loginEntity 在线用户实体
     * @param args 可变形参
     * @return 任务task
     */
    public static TimerTask recordLoginInfo(final LoginEntity loginEntity, final Object... args) {
        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final UserAgent parse = UserAgentUtil.parse(servletRequestAttributes.getRequest().getHeader("User-Agent"));
        return new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("记录登录信息线程启动");
                LOGGER.info("获取浏览器类型：{}",parse.getBrowser());
                LOGGER.info("获取浏览器版本：{}",parse.getVersion());
                LOGGER.info("获取引擎类型：{}",parse.getEngine());
                LOGGER.info("获取引擎版本：{}",parse.getEngineVersion());
                LOGGER.info("获取系统类型：{}",parse.getOs());
                LOGGER.info("获取平台类型：{}",parse.getPlatform());
            }
        };
    }
}
