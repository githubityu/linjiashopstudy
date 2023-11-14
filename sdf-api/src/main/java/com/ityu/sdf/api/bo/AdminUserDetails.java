package com.ityu.sdf.api.bo;

import com.ityu.common.bean.entity.system.User;

import com.ityu.common.bean.vo.node.RouterMenu;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户信息封装类
 * Created by macro on 2018/4/26.
 */
public class AdminUserDetails implements UserDetails {
    //后台用户
    private final User umsAdmin;
    //拥有资源列表
    private final Set<RouterMenu> resourceList;

    public AdminUserDetails(User umsAdmin, Set<RouterMenu> resourceList) {
        this.umsAdmin = umsAdmin;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户所拥有的资源
        List<SimpleGrantedAuthority> collect = resourceList.stream()
                .map(resource -> new SimpleGrantedAuthority(resource.getId() + ":" + resource.getName()))
                .collect(Collectors.toList());
        return  collect;
    }

    public User getUmsAdmin() {
        return umsAdmin;
    }

    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getAccount();
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
        return umsAdmin.getStatus().equals(1);
    }
}
