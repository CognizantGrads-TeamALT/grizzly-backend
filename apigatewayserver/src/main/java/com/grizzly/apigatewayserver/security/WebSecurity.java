package com.grizzly.apigatewayserver.security;

import com.grizzly.apigatewayserver.filter.AuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.grizzly.apigatewayserver.auth.UserDetailsServiceImpl;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Order(-10)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;

    public WebSecurity(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .authorizeRequests()
                // public calls available for everyone
                .antMatchers("/auth/**").permitAll() // permit auth calls

                // Category microservice
                .antMatchers( "/category/get/**" ).permitAll()
                .antMatchers( "/category/search/**" ).permitAll()
                .antMatchers( "/category/batchFetch/**" ).permitAll()

                // Product microservice
                .antMatchers( "/product/batchFetch/**" ).permitAll()
                .antMatchers( "/product/byCategory/**" ).permitAll()
                .antMatchers( "/product/get/**").permitAll()
                .antMatchers( "/product/getDetails/**" ).permitAll()
                .antMatchers( "/product/search/**").permitAll()

                // Vendor microservice
                .antMatchers( "/vendor/batchFetch/**" ).permitAll()
                .antMatchers( "/vendor/get/**" ).permitAll()
                .antMatchers( "/vendor/search/**" ).permitAll()

                // User microservice
                .antMatchers( "/user/**" ).permitAll()

            .anyRequest().authenticated().and()
            .addFilter(new AuthorizationFilter(authenticationManager()))
             //this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //@Bean
    //public AuthenticationFilter addAuthenticationStringFilter() {
    //    return new AuthenticationFilter();
    //}

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}