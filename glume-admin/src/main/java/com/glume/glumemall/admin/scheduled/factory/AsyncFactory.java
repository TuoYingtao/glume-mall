package com.glume.glumemall.admin.scheduled.factory;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.glume.common.core.utils.ServletUtils;
import com.glume.glumemall.admin.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 * @author tuoyingtao
 * @create 2022-03-10 17:16
 */
public class AsyncFactory {

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLoginInfo(final String username, final String status, final String message, final Object... args) {
        final UserAgent parse = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        return new TimerTask() {
            @Override
            public void run() {

            }
        };
    }
}
