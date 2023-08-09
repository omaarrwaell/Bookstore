package com.example.bookstore.controllers;

import com.example.bookstore.domain.LoginDto;
import com.example.bookstore.domain.User;
import com.example.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    private UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ResponseEntity<User> register(){
        User user = new User();
        return ResponseEntity.ok(user);
}
    @PostMapping("/register")
    public ResponseEntity<User> register(@Validated @RequestBody User user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        List<ObjectError> validationErrors = bindingResult.getAllErrors();
        return ResponseEntity.badRequest().body( (User) validationErrors);
    }
    else {
        User newUser = userService.register(user);
        return ResponseEntity.ok(newUser);
    }
}

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.findAll();
}

//    @GetMapping("/login")
//    public ResponseEntity<String> login(){
//        return ResponseEntity.ok("Provide login credentials using a POST request.");
//}

@PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody LoginDto form, BindingResult bindingResult,
                      HttpServletRequest request){
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
            (form.getUsernameOrEmail(), form.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return new ResponseEntity<>("User signed-in successfully", HttpStatus.OK);
}


}
