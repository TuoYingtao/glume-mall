package com.glume.glumemall.admin.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author tuoyingtao
 * @create 2021-10-19 11:06
 */
public class AccountUser implements UserDetails {
    private static final Log logger = LogFactory.getLog(AccountUser.class);

    /** 表示获取密码 */
    private String password;

    /** 表示获取用户名 */
    private final String username;

    /** 用户ID */
    private final Long userId;

    /** 用户角色 */
    private final String roleTag;

    /** 表示获取登录用户所有权限 */
    private final Collection<? extends GrantedAuthority> authorities;

    /** 表示判断账户是否过期 */
    private final boolean accountNonExpired;

    /** 表示判断账户是否被锁定 */
    private final boolean accountNonLocked;

    /** 表示凭证{密码}是否过期 */
    private final boolean credentialsNonExpired;

    /** 表示当前用户是否可用 */
    private final boolean enabled;

    public AccountUser(Long userId, String roleTag, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(userId, roleTag, username, password, true, true, true, true, authorities);
    }

    public AccountUser(Long userId, String roleTag, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        if (((username == null) || "".equals(username)) || (password == null)) {
            throw new IllegalArgumentException("用户名与密码不能为空！");
        }
        this.userId = userId;
        this.roleTag = roleTag;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }

    /** 表示获取登录用户所有权限 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /** 用户ID */
    public Long getUserId() {
        return this.userId;
    }

    /** 用户角色 */
    public String getRoleTag() {
        return this.roleTag;
    }

    /** 表示获取密码 */
    @Override
    public String getPassword() {
        return this.password;
    }

    /** 表示获取用户名 */
    @Override
    public String getUsername() {
        return this.username;
    }

    /** 表示判断账户是否过期 */
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    /** 表示判断账户是否被锁定 */
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    /** 表示凭证{密码}是否过期 */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    /** 表示当前用户是否可用 */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public enum UserFields {

        USERNAME("username"),
        USER_ID("userId"),
        ROLE_TAG("roleTag"),
        PASSWORD("password"),
        AUTHORITIES("authorities"),
        ACCOUNT_NON_EXPIRED("accountNonExpired"),
        ACCOUNT_NON_LOCKED("accountNonLocked"),
        CREDENTIALS_NON_EXPIRED("credentialsNonExpired"),
        ENABLED("enabled");

        private String message;

        UserFields(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
