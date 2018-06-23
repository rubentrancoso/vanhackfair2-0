package com.company.challenge.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.company.challenge.security.AntMatcher;

@Configuration
@ConfigurationProperties(prefix = "security")
public class AntMatcherConfig {
	
    private List<AntMatcher> antMatchers = new ArrayList<>();

	public List<AntMatcher> getAntMatchers() {
		return antMatchers;
	}

	public void setAntMatchers(List<AntMatcher> antMatchers) {
		this.antMatchers = antMatchers;
	}


    
}