package com.founder.easy_route_assistant.security;

import lombok.Getter;
import com.founder.easy_route_assistant.Entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails, Serializable {
    private final UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(()->{return String.valueOf(userEntity.getRole());});

        return authorities;
    }

    @Override
    public String getPassword() { return userEntity.getPwd(); }

    @Override
    public String getUsername() {
        return userEntity.getUserID();
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
