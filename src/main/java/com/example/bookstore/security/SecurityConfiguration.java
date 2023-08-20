//package com.example.bookstore.security;
//
//import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
//@EnableMethodSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    private UserDetailsServiceImpl userDetailsService;
//    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService){
//        this.userDetailsService = userDetailsService;
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http
////                .authorizeRequests()
////                .requestMatchers(EndpointRequest.to("info")).permitAll()
//////                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
//////                .antMatchers("/actuator/").hasRole("ADMIN")
////                .antMatchers("/").permitAll()
//////                .antMatchers("/admin").hasRole("ADMIN")
////                .and().formLogin().loginPage("/login").permitAll().usernameParameter("email")
////                .and().logout()
////                .and().rememberMe()
////                .and()
////                .csrf().disable()
////                .headers().frameOptions().disable();
//
//        http.authorizeRequests().anyRequest().permitAll().and().csrf().disable().headers().frameOptions().disable();
//    }
////    @Bean
////    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////
////        http.csrf().disable()
////                .authorizeHttpRequests((authorize) ->
////                        //authorize.anyRequest().authenticated()
////                        authorize.antMatchers("/**").permitAll()
////                                .antMatchers("/auth/**").permitAll()
////                                .anyRequest().authenticated()
////
////                );
////
////        return http.build();
////    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }
//}