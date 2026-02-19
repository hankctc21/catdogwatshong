package com.demo.cdmall1;

import com.demo.cdmall1.security.AdminLoginFailureHandler;
import com.demo.cdmall1.security.AdminLoginSuccessHandler;
import com.demo.cdmall1.security.CdmallAccessDeniedHandler;
import com.demo.cdmall1.security.CustomUserDetailsService;
import com.demo.cdmall1.security.LoginFailureHandler;
import com.demo.cdmall1.security.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final AdminLoginSuccessHandler adminLoginSuccessHandler;
    private final AdminLoginFailureHandler adminLoginFailureHandler;
    private final LoginSuccessHandler memberLoginSuccessHandler;
    private final LoginFailureHandler memberLoginFailureHandler;
    private final CdmallAccessDeniedHandler accessDeniedHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if ("SYSTEM01".equals(username)) {
                UserDetails adminUser = User.withUsername("SYSTEM01")
                    .password(passwordEncoder.encode("12341234!"))
                    .roles("ADMIN")
                    .build();
                return adminUser;
            }
            return customUserDetailsService.loadUserByUsername(username);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login").permitAll()
                .anyRequest().hasRole("ADMIN")
            )
            .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .successHandler(adminLoginSuccessHandler)
                .failureHandler(adminLoginFailureHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
            );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain memberSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .formLogin(form -> form
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(memberLoginSuccessHandler)
                .failureHandler(memberLoginFailureHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
            );

        return http.build();
    }
}
