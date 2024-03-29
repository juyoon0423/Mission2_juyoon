package com.example.shoppingmall.config;

import com.example.shoppingmall.jwt.JwtTokenFilter;
import com.example.shoppingmall.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/register",
                                "/users/login",
                                "/items",
                                "/shops"
                        )
                        .permitAll()

                        .requestMatchers("/users/updateUser")
                        .hasRole("INACTIVE")

                        .requestMatchers(
                                "users/updateBusiness/status",
                                "shops/readList",
                                "shops/{shopId}/update"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/users/updateBusiness"
                        )
                        .hasAnyRole("USER", "SELLER", "OFFER")
                        .requestMatchers(
                                "/items/{itemId}",
                                "/items/create"
                        )
                        .hasAnyRole( "OFFER", "USER", "BUSINESS","ADMIN")

                        .requestMatchers(
                                "/items/{itemId}",
                                "/items/{itemId}/update",
                                "/items/{itemId}/delete"
                        )
                        .hasRole("SELLER")
                        .requestMatchers(
                                "/shops/{shopId}/update",
                                "/shops/createShop"
                        )
                        .hasRole("BUSINESS")
                        .anyRequest()
                        .authenticated()

                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenUtils, manager),
                        AuthorizationFilter.class
                )
        ;
        return http.build();
    }
}