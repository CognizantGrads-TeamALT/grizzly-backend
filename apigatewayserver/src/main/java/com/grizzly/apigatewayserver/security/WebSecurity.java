package com.grizzly.apigatewayserver.security;

import com.grizzly.apigatewayserver.filter.AuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.grizzly.apigatewayserver.auth.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;

    public WebSecurity(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().disable()
            .cors()
            .and()
            .authorizeRequests()
            //.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
            .antMatchers("/auth/**").permitAll()
            //.antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and()
            //.addFilter(new AuthenticationFilter(authenticationManager()))
           .addFilter(new AuthorizationFilter(authenticationManager()))
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}