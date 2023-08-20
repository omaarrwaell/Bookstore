package com.example.bookstore.security;

import com.example.bookstore.domain.User;
import com.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        if(!user.isPresent()){
            throw new UsernameNotFoundException(username);
        }else{
            GrantedAuthority authority= new SimpleGrantedAuthority(user.get().getRole().getName());
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(),
                    user.get().getPassword(),
                    Collections.singleton(authority));
        }

    }
}
