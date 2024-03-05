package com.example.shoppingmall.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    @Getter
    private String nickname;
    @Getter
    private String email;
    @Getter
    private String name;
    @Getter
    private String phone;
    @Getter
    private String age;
    @Getter
    private String businessNum;
    private String authorities;

    public boolean inactiveToUser() {
        return nickname != null && name != null
                && age != null && email != null && phone != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities  = new ArrayList<>();
        if (this.authorities != null) {
            String[] rawAuthorities = authorities.split(",");
            for (String rawAuthority : rawAuthorities) {
                grantedAuthorities.add(new SimpleGrantedAuthority(rawAuthority));
            }
        }
        return grantedAuthorities;
    }
    public boolean userToBusiness() {
        return businessNum != null;
    }

    public String getRawAuthorities() {
        return this.authorities;
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
