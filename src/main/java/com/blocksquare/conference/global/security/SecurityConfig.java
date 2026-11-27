package com.blocksquare.conference.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                    //관리자페이지는 로그인 필요
                    .requestMatchers("/admin/login").permitAll()
                    .requestMatchers("/admin/**").authenticated()
                    //나머지는 일단 공개
                    .anyRequest().permitAll()
            )
                .formLogin(form -> form
                        .defaultSuccessUrl("/admin/dashboard", false)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/admin/logout").logoutSuccessUrl("/"));
        return http.build();
    }

}
