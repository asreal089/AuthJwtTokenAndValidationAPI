package com.mainapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mainapi.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public PasswordEncoder bCryptPasswordEncoder;
	@Autowired
	public CustomUserDetailsService userDetailsService;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests()
			.antMatchers("/home").permitAll()
			.antMatchers(HttpMethod.POST, "/register").permitAll()
			.antMatchers(HttpMethod.GET, "/confirm-account*").permitAll()
			.antMatchers(HttpMethod.GET, "/signup-sucess").permitAll()
			.anyRequest().authenticated()
			.and()
			
			.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
	                UsernamePasswordAuthenticationFilter.class)
			
			.addFilterBefore(new JWTAuthenticationFilter(),
	                UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		auth.inMemoryAuthentication()
		.withUser("admin")
		.password("$2a$10$G/.Uc8Jw6GtvX81FtlUWX.FD.2ONPMLVNWRgtajE0F9Q0H2E2j70C")
		.roles("ADMIN");
	}
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
}