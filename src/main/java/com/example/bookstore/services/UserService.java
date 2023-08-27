package com.example.bookstore.services;

import com.example.bookstore.config.JwtTokenUtil;
import com.example.bookstore.domain.BrowsingHistory;
import com.example.bookstore.domain.ShoppingCart;
import com.example.bookstore.domain.User;
import com.example.bookstore.domain.WishList;
import com.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private  final BCryptPasswordEncoder encoder;
    private final RoleService roleService;

    private  final JwtTokenUtil jwtTokenUtil;
    private final ShoppingCartService shoppingCartService;
    private  final WishListService wishListService;
    private final BrowsingHistoryService browsingHistoryService;
    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService
            , JwtTokenUtil jwtTokenUtil,ShoppingCartService shoppingCartService,WishListService wishListService,
                       BrowsingHistoryService browsingHistoryService) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.encoder=new BCryptPasswordEncoder();
        this.roleService = roleService;
        this.shoppingCartService=shoppingCartService;
        this.wishListService=wishListService;
        this.browsingHistoryService=browsingHistoryService;
    }
    public User register( User user){
        String secret = "{bcrypt}"+encoder.encode(user.getPassword());
        user.setPassword(secret);
        user.setRole(roleService.findByName("ROLE_USER"));
        User savedUser= save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(savedUser);
        shoppingCartService.save(shoppingCart);
        WishList wishList = new WishList();
        wishList.setUserId(savedUser);
        wishListService.save(wishList);
        BrowsingHistory browsingHistory = new BrowsingHistory();
        browsingHistory.setUserId(savedUser);
        browsingHistoryService.save(browsingHistory);


        return user;
    }

public User save(User user){
        return userRepository.save(user);
}
public List<User> findAll(){
        return userRepository.findAll();

}
public Optional<User> getLoggedInUser(String authorization) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName=jwtTokenUtil.getUsernameFromToken(authorization);
   return getUser(userName);
}
public Optional<User> getUser(String username){
        return userRepository.findByEmail(username);
}

    public User getUserById(String id){
        return userRepository.findById(id).get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
