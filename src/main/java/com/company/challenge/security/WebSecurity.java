package com.company.challenge.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.company.challenge.config.AntMatcherConfig;
import com.company.challenge.config.JwtConfig;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private UserDetailsService userDetailsService;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	AntMatcherConfig antMatcherConfig;

	@Autowired
	JwtConfig jwtConfig;
	
	public WebSecurity(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		List<AntMatcher> antMatchers = antMatcherConfig.getAntMatchers();
		
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http
				.csrf().disable().authorizeRequests();
		for(AntMatcher antMatcher : antMatchers) {
			expressionInterceptUrlRegistry = expressionInterceptUrlRegistry.antMatchers(antMatcher.getMethod(), antMatcher.getUrl())
					.permitAll();
		}
		expressionInterceptUrlRegistry.anyRequest().authenticated()
				.and()
				//.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtConfig))
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtConfig));
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

}
