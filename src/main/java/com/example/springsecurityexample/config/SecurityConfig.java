package com.example.springsecurityexample.config;

import com.example.springsecurityexample.model.User;
import com.example.springsecurityexample.repository.UserRepository;
import com.example.springsecurityexample.security.jwt.JwtAuthEntryPoint;
import com.example.springsecurityexample.security.jwt.JwtAuthTokenFilter;
import com.example.springsecurityexample.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static java.lang.String.format;

@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {
// @Autowired
  private UserRepository userRepository;
private static final String[] AUTH_WHITELIST = {
        "/authenticate",
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/webjars/**"
};
   @Autowired
   private UserDetailsServiceImpl userDetailsService;

//    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

//    public SecurityConfig(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        User user = userRepository
//                .findByUsername(username)
//                .orElseThrow(
//                        () -> new UsernameNotFoundException(
//                                format("User: %s, not found", username)
//                        )
//                );
        auth.userDetailsService( userDetailsService
//                username ->  userRepository
//                .findByUsername(username)
//                .orElseThrow(
//                        () -> new UsernameNotFoundException(
//                                format("User: %s, not found", username)
//                        )
//                )
        );
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler; // выбрасывает исключения в случае форс мажора

    @Bean// будет храниться в контексте спринга
    public JwtAuthTokenFilter authenticationJwtTokenFilter() { // проверяет токен
        return new JwtAuthTokenFilter();// создает обьект
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()// авторизировать все запросы!
                .antMatchers("/api/auth/**").permitAll() // по этому адресу пускать всех
                .antMatchers(    "/authenticate",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**" ).permitAll()
//                .antMatchers("/swagger-ui/**", "/javainuse-openapi/**").permitAll() // поэтому адресу пускать всех
                .anyRequest().authenticated() // пользователи по всем запросам должн проходить аутентиикацию
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() // кто обрабатвает исключения
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // как работать с ссесиями

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // какой фильтр подключить
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
    }


}