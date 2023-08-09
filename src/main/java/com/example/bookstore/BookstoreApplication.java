package com.example.bookstore;

import com.example.bookstore.domain.Role;
import com.example.bookstore.domain.User;
import com.example.bookstore.repository.RoleRepository;
import com.example.bookstore.repository.UserRepository;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication  implements CommandLineRunner{


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public BookstoreApplication(UserRepository userRepository,RoleRepository roleRepository) {
        this.roleRepository=roleRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);

    }


    @Override
    public void run(String... args) throws Exception {
        Role userRole = new Role("ROLE_USER");
        if(roleRepository.findByName("ROLE_USER")==null){
        roleRepository.save(userRole);}

                for(User user : userRepository.findAll()){
                    System.out.println(user);
                }
    }
}
