package com.gurutest.Security;

import com.gurutest.Users.UserRole;
import com.gurutest.Users.UserStatus;
import com.gurutest.Users.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserRegestirationDetails implements UserDetails {
    private String username;
    private String password;
    //private UserStatus status;
    private boolean isEnabled;
    private List<GrantedAuthority> authorities;

    public UserRegestirationDetails(Users user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        //this.status = user.getStatus();
        this.isEnabled= user.isEnabled();
        //this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        this.authorities = Arrays.stream(user.getRole()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        //return Users.isUserEnabled(this.status);
        return isEnabled;
    }
}
