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

    private String password;

    private final String username;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public AccountUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, true, true, true, true, authorities);
    }

    public AccountUser(String username, String password, boolean enabled,
                boolean accountNonExpired, boolean credentialsNonExpired,
                boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        if (((username == null) || "".equals(username)) || (password == null)) {
            throw new IllegalArgumentException(
                    "Cannot pass null or empty values to constructor");
        }
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
