package com.myworkspace.security.security;

import com.myworkspace.security.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private UserEntity userEntity;

    public UserPrincipal(UserEntity userEntity){
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        //Extract Permissions
        this.userEntity.getPermissionsList().forEach(
                p -> {GrantedAuthority
        authority = new SimpleGrantedAuthority(p);
        authorities.add(authority);
                }
        );

        //Extract Roles
        this.userEntity.getRoleList().forEach(
                r -> {GrantedAuthority
                        authority = new SimpleGrantedAuthority("ROLE_"+ r);
                    authorities.add(authority);
                }
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getUsername();
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
        return this.userEntity.getActive() == 1;
    }
}
