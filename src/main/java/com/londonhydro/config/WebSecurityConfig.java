package com.londonhydro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	CustomAuthProvider authProvider;
	
	@Autowired
	AuthEntryPoint authEntryPoint;
	
	@Autowired
	AuthenticationTokenFilter authenticatonTokenFilter;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
        .csrf().disable()
        .cors()
 //       .and()
 //       .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/login","/","/css/**","/js/**","/images/**","/webjars/**").permitAll()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .antMatchers(HttpMethod.POST,"/login").permitAll()
        .antMatchers("/profile").hasAuthority("Admin")
        .anyRequest().fullyAuthenticated()
        .and()
        .exceptionHandling().authenticationEntryPoint(authEntryPoint).accessDeniedPage("/login")
        .and()
        .addFilterBefore(authenticatonTokenFilter,UsernamePasswordAuthenticationFilter.class)
        .headers().frameOptions().sameOrigin().cacheControl();
	}
}
