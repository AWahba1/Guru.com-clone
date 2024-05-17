package com.gurutest.OAuth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserOAuth implements OAuth2User {
    private OAuth2User oauth2User;
    //private String ClientName;

    public UserOAuth(OAuth2User oauth2User) {
        this.oauth2User = oauth2User;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String fullName() {
        return oauth2User.getAttribute("name");
    }
//    public String getClientName() {
//        return this.ClientName;
//    }
}
