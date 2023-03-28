package com.example.demo.config;

import com.example.demo.security.user.UserDetailsServiceImpl;
import com.example.demo.security.endpoint.Oauth2RequestEntityConverter;
import com.example.demo.security.authentication.BasicAuthenticationFailureHandler;
import com.example.demo.security.authentication.BasicAuthenticationSuccessHandler;
import com.example.demo.security.authentication.Oauth2AuthenticationProvider;
import com.example.demo.security.authentication.OidcAuthenticationProvider;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserService userService;
    private final ObjectPostProcessor<Object> objectPostProcessor;
    private final Oauth2MetaProperties oauth2MetaProperties;

    @Bean
    public UserDetailsService userDetailsService() {
        userService.setPasswordEncoder(passwordEncoder());
        return new UserDetailsServiceImpl(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers()
                .frameOptions().disable()
            .and()
            .authorizeHttpRequests()
                .requestMatchers("/sign-up").permitAll()
                .requestMatchers("/api/sign-in").permitAll()
                .requestMatchers("/h2/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
            .and()
                .formLogin()
                .loginPage("/sign-in")
                .loginProcessingUrl("/api/sign-in")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll()
            .and()
                .oauth2Login()
                .loginPage("/sign-in")
                .tokenEndpoint(endPoint -> endPoint
                        .accessTokenResponseClient(accessTokenResponseClient())
                )
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll()
            .and()
                .authenticationManager(authenticationManager())
        ;
        return http.build();
    }

    AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        builder.userDetailsService(userDetailsService());
        builder.authenticationProvider(userAuthenticationProvider());
        builder.authenticationProvider(new Oauth2AuthenticationProvider());
        builder.authenticationProvider(new OidcAuthenticationProvider());
        return builder.build();
    }

    public AuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new BasicAuthenticationSuccessHandler();
    }

    AuthenticationFailureHandler authenticationFailureHandler() {
        return new BasicAuthenticationFailureHandler();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRequestEntityConverter(new Oauth2RequestEntityConverter(oauth2MetaProperties));
        return client;
    }

}