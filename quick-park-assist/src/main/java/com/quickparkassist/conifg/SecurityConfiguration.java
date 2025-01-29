package com.quickparkassist.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.quickparkassist.service.UserService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
//@EnableWebMvc
public class
SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public SecurityConfiguration(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF protection is disabled
                .authorizeRequests()
                .antMatchers(
                        "/registration**",// Permit registration
                        "/searchspots",
                        "/css/**",          // Allow CSS files
                        "/js/**",           // Allow JS files
                        "/images/**",       // Allow image files
                        "/index**",         // Allow index page
                        "/static/**"  ,   // Allow static resources (optional, not typically needed)
                        "/reset-password",
                        "/forgotPassword","/resetPassword/**",
                        "/api/v1/auth/**",
                        "/v3/api-docs/**",
                        "/v2/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/view-Booking-Details"
//                    "/SwaggerConfig/**",
//                    "/AppConfig/**"
                ).permitAll()           // Allow everyone to access these paths
                .antMatchers("/admin/**").hasRole("ADMIN") // Restrict admin pages to ROLE_ADMIN
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Restrict user pages to ROLE_USER or ROLE_ADMIN
                .antMatchers( "/viewBookingByNumber", "/add").permitAll()
                //  .antMatchers("/view-Booking-Details", "/api/view-Booking-Details").permitAll()
                //  .antMatchers("/available", "/api/available").permitAll()

                .anyRequest().authenticated() // All other URLs require authentication
                .and()
                .formLogin()
                .loginPage("/login") // Customize login page
                .permitAll()         // Allow everyone to access the login page
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/") // Redirect after logout
                .permitAll();         // Allow logout for everyone


    }

}