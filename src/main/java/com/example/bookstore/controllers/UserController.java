
package com.example.bookstore.controllers;

import com.example.bookstore.services.UserService;

import com.example.bookstore.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/Personal-info")
    public ResponseEntity<User> getUserInfo(@RequestHeader String Authorization){
        User currentUser =userService.getLoggedInUser(Authorization).get();
        if(currentUser == null ){
            return ResponseEntity.badRequest().body(currentUser);
        }else{
            return ResponseEntity.ok(currentUser);
        }

    }

    @PostMapping("/Personal-info/update-email")
    public ResponseEntity<User> UpdateEmail(@RequestHeader String Authorization , @RequestBody String email){
        com.example.bookstore.domain.User currentUser =userService.getLoggedInUser(Authorization).get();
        if(currentUser == null ){
            return ResponseEntity.badRequest().body(currentUser);
        }else{
            currentUser.setEmail(email);
            User saved = userService.save(currentUser);
            return ResponseEntity.ok(saved);
        }
    }

}