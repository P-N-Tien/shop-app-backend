package com.shop_app.configs.security;

import com.shop_app.user.UserRepository;
import com.shop_app.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipleDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNo) {
        User user = userRepository.findUserByPhoneNumber(phoneNo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username or password is incorrect")
                );
        
        return new UserPrincipal(user);
    }
}
