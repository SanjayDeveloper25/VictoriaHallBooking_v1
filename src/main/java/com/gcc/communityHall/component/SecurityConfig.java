//package com.gcc.communityHall.component;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.gcc.communityHall.service.Impl.UserDetailsServiceImpl;
//
//@EnableWebSecurity
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig  {
//
//    @Autowired
//    JwtAuthenticationEntryPoint unauthorizedHandler;
//
//    @Autowired
//    JwtAuthenticationFilter jwtAuthenticationFiler;
//
//    @Autowired
//    UserDetailsServiceImpl userDetailsService;
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public PasswordEncoder passwordEncoder(){
////        return NoOpPasswordEncoder.getInstance();
////    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
//        return builder.getAuthenticationManager();
//    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeRequests().
//                requestMatchers("/login").authenticated().requestMatchers("/login","/caretaker/","/careTaker/","/admin/","/user/").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.addFilterBefore(jwtAuthenticationFiler, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
////    @Bean
////    public DaoAuthenticationProvider daoAuthenticationProvider(){
////        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
////        provider.setUserDetailsService(userDetailsService);
////        return provider;
////    }
//
//}