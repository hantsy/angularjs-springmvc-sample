package com.hantsylabs.restexample.springmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
            .ignoring()
                    .antMatchers("/**/*.html", //
                     "/css/**", //
                     "/js/**", //
                     "/i18n/**",// 
                     "/libs/**",//
                     "/img/**", //
                     "/webjars/**",//
                     "/ico/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
            http   
                .authorizeRequests()   
                    .antMatchers("/api/ping")
                    .permitAll()
    		.and()
                    .authorizeRequests()   
                    .antMatchers("/api/**")
                    .authenticated()
    		.and()
                    .authorizeRequests()   
                    .anyRequest()
                    .permitAll()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .httpBasic()
                .and()
                    .csrf()
                    .disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder())
                    .withUser("admin").password("test123").authorities("ROLE_ADMIN")
                    .and()
                        .withUser("test").password("test123").authorities("ROLE_USER");
                
                //auth.userDetailsService(new SimpleUserDetailsServiceImpl(userRepository))
		//.passwordEncoder(passwordEncoder);
	}


	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}

	@Bean
	public PlaintextPasswordEncoder passwordEncoder() {
		return new PlaintextPasswordEncoder();
	}

}
