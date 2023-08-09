package com.example.bookstore.services;

import com.example.bookstore.domain.Role;
import com.example.bookstore.domain.User;
import com.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private  final BCryptPasswordEncoder encoder;
    private final RoleService roleService;
    public UserService(UserRepository userRepository,RoleService roleService) {
        this.userRepository = userRepository;
        this.encoder=new BCryptPasswordEncoder();
        this.roleService = roleService;
    }
public User register( User user){
        String secret = "{bcrypt}"+encoder.encode(user.getPassword());
        user.setPassword(secret);
        user.setRole(roleService.findByName("ROLE_USER"));
        save(user);
        return user;
}

public User save(User user){
        return userRepository.save(user);
}
public List<User> findAll(){
        return userRepository.findAll();
}
}
