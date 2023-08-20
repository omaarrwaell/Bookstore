package com.example.bookstore.controllers;

import com.example.bookstore.config.JwtTokenUtil;
import com.example.bookstore.domain.LoginDto;
import com.example.bookstore.domain.User;
import com.example.bookstore.security.UserDetailsServiceImpl;
import com.example.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
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


    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(form.getUsernameOrEmail(), form.getPassword());
    Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        UserDetails userDetails = userDetailsService.loadUserByUsername(form.getUsernameOrEmail());


        final String token = jwtTokenUtil.generateToken(userDetails);

        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    return new ResponseEntity<>(token.toString(), HttpStatus.OK);
}


}
