package com.chubeo.DebtZero.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {
    User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

    }

    public UUID getId() {
        return user.getId();
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        return user.getUsername();
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
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
