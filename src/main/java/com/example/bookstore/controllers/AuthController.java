package com.example.bookstore.controllers;

import com.example.bookstore.config.JwtTokenUtil;
import com.example.bookstore.domain.LoginDto;
import com.example.bookstore.domain.User;
import com.example.bookstore.security.UserDetailsServiceImpl;
import com.example.bookstore.services.MailService;
import com.example.bookstore.services.TokenBlackListService;
import com.example.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    private MailService mailService;

    private TokenBlackListService tokenBlackListService;
    @Autowired
    public AuthController(UserService userService,TokenBlackListService tokenBlackListService) {
        this.userService = userService;
        this.tokenBlackListService=tokenBlackListService;
    }

    @GetMapping("/register")
    public ResponseEntity<User> register(){
        User user = new User();
        return ResponseEntity.ok(user);
}
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
//    String token = jwtUtil.extractTokenFromHeader(authorizationHeader);
        if(jwtTokenUtil.isTokenBlacklisted(authorizationHeader))
            return ResponseEntity.badRequest().body("You're not logged in.");
        jwtTokenUtil.invalidateToken(authorizationHeader);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("You've logged out successfully.");
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
@GetMapping("/u")
public ResponseEntity<?> getUser(Authentication authentication){
        authentication= SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication.getPrincipal());
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
      //  HttpSession session = request.getSession(true);
        UserDetails userDetails = userDetailsService.loadUserByUsername(form.getUsernameOrEmail());


        final String token = jwtTokenUtil.generateToken(userDetails);

      //  session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        mailService.sendWelcomeEmail(userDetails);

    return new ResponseEntity<>(token.toString(), HttpStatus.OK);
}

//    @PutMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request,Authentication authentication,@RequestHeader String Authorization){
//    if (authentication != null) {
//        tokenBlackListService.addToBlacklist(Authorization);
//        new SecurityContextLogoutHandler().logout(request, null, authentication);
//        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
//    } else {
//        return new ResponseEntity<>("No authenticated user", HttpStatus.BAD_REQUEST);
//    }
//
//}

}
