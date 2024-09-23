package com.study.basicboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import com.study.basicboard.config.auth.MyAccessDeniedHandler;
import com.study.basicboard.config.auth.MyAuthenticationEntryPoint;
import com.study.basicboard.config.auth.MyLoginSuccessHandler;
import com.study.basicboard.config.auth.MyLogoutSuccessHandler;
import com.study.basicboard.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    // 로그인하지 않은 유저들만 접근 가능한 URL
    private static final String[] ANONYMOUS_USER_URLS = {"/users/login", "/users/join"};

    // 로그인한 유저들만 접근 가능한 URL
    private static final String[] AUTHENTICATED_USER_URLS = {
        "/boards/*/edit", 
        "/boards/*/delete", 
        "/likes/*", 
        "/users/myPage/*", 
        "/users/edit", 
        "/users/delete"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests(authorize -> authorize
                    //.requestMatchers(ANONYMOUS_USER_URLS).anonymous()
                    .requestMatchers(AUTHENTICATED_USER_URLS).authenticated()
                    
                    .requestMatchers("/boards/greeting/write").hasAnyAuthority("BRONZE", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/boards/greeting").hasAnyAuthority("BRONZE", "ADMIN")
                    .requestMatchers("/boards/free/write").hasAnyAuthority("SILVER", "GOLD", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/boards/free").hasAnyAuthority("SILVER", "GOLD", "ADMIN")
                    .requestMatchers("/boards/gold/**").hasAnyAuthority("GOLD", "ADMIN")
                    .requestMatchers("/users/admin/**").hasAuthority("ADMIN")
                    .requestMatchers("/comments/**").hasAnyAuthority("BRONZE", "SILVER", "GOLD", "ADMIN")
                    .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                    .accessDeniedHandler(new MyAccessDeniedHandler(userRepository))
                    .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                )
                .formLogin(formLogin -> formLogin
                    .loginPage("/users/login")
                    .usernameParameter("loginId")
                    .passwordParameter("password")
                    .failureUrl("/users/login?fail")
                    .successHandler(new MyLoginSuccessHandler(userRepository))
                )
                .logout(logout -> logout
                	    .logoutSuccessHandler(new MyLogoutSuccessHandler())
                	    .invalidateHttpSession(true)
                	    .deleteCookies("JSESSIONID")
                	    .logoutRequestMatcher(new OrRequestMatcher(
                	        new AntPathRequestMatcher("/users/logout"),
                	        new AntPathRequestMatcher("/users/myPage/logout")
                	    ))
                	)

                .build();
    }
}

