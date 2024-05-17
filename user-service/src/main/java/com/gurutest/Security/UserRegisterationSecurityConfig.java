package com.gurutest.Security;

import com.gurutest.OAuth.OAuth2LoginSuccessHandler;
import com.gurutest.OAuth.UserOAuthService;
import com.gurutest.config.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class UserRegisterationSecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter;

    //private final  AuthenticationProvider authenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     return   http
            .csrf(csrf -> csrf.csrfTokenRepository
                (CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .disable())
             .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                       // .requestMatchers("/profile/**","/users").hasAnyAuthority("FREELANCER","EMPLOYER")
                        .requestMatchers("/signup/**","/login/**","/error","/signup/signup-form","/oauth2/**", "/","/users/token","/users/find").permitAll()
                        .anyRequest().authenticated()
                       // .requestMatchers("/").permitAll()
                )

//             .formLogin(Customizer.withDefaults())
//                .formLogin(login->login.loginPage("/login")
//                        .usernameParameter("email")
//                        .defaultSuccessUrl("/")
//                        .permitAll())

             .oauth2Login(Customizer.withDefaults())
             .oauth2Login(login->login
                             .loginPage("/login")
                             .userInfoEndpoint(userInfo -> userInfo
                                     .userService(userOAuthService)
                             )
                             .defaultSuccessUrl("/")
                             .successHandler(loginSuccessHandler)
                     )
//             .logout(Customizer.withDefaults())
                .logout(logout->logout.invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                )
            // .authenticationProvider(authenticationProvider)
             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

             .build();
       // return http.build();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.cors(Customizer.withDefaults())
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/signup")
//                        .permitAll()
//                        .requestMatchers("/api/users")
//                        .hasAnyAuthority("FREELANCER","EMPLOYER")
//                        .anyRequest()
//               )
//                .formLogin(Customizer.withDefaults())
//                .build();
//    }

    @Autowired
private UserOAuthService userOAuthService;

    @Autowired
    private OAuth2LoginSuccessHandler loginSuccessHandler;
}

