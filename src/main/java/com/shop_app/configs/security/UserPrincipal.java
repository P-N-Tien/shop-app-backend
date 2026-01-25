package com.shop_app.configs.security;

import com.shop_app.user.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final User user;

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.getName())
        ).toList();
    }

    @Override
    public String getUsername() {
        return user.getPhoneNumber();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

