package com.glume.common.core.constant;

/**
 * redis 常量
 * @author tuoyingtao
 * @create 2021-10-22 13:38
 */
public interface RedisConstant {
    /** 用户信息 */
    String USER_INFO = "admin:userinfo:";

    /** 角色菜单 */
    String ROLE_MENU = "admin:role:menu:";

    /** 用户权限 */
    String REDIS_AUTHORITY_KEY = "admin:authorit:key:";

    /** code验证码 */
    String CAPTCHA_KEY = "admin:captcha:";

    /** 用户黑名单 */
    String BLACKLIST_KEY = "admin:blacklist:";
}
