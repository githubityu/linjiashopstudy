package com.ityu.security;


import com.ityu.service.system.ManagerService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AuthDetailsService implements UserDetailsService {

    public final ManagerService userRepository;

    public AuthDetailsService(ManagerService userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserService.me().adminUser(username);
    }
}
