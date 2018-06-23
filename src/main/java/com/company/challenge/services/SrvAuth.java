package com.company.challenge.services;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.challenge.config.JwtConfig;
import com.company.challenge.entities.User;
import com.company.challenge.repositories.UserRepository;
import com.company.challenge.services.interfaces.ISrvAuth;
import com.company.challenge.userapi.inputs.Credentials;
import com.company.challenge.userapi.message.Message;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SrvAuth implements ISrvAuth {

	private static final Logger logger = LoggerFactory.getLogger(SrvAuth.class);
	
	@Autowired
	JwtConfig jwtConfig;	
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	SrvAuth(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	@Transactional
	public Object login(Credentials credentials) {
		logger.info("performing login...");
		User user = userRepository.findByEmail(credentials.getUsername());
		if(
			user == null ||
			credentials.getPassword() == null ||
			credentials.getPassword().isEmpty() ||
			!passwordEncoder.matches(credentials.getPassword(), user.getPassword())
		) 
		{
			return new Message(Message.INVALID_USERNAME_PASSWORD);
		} else {
			user.setLast_login();
			String newToken = Jwts.builder().setSubject(user.getEmail())
					.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
					.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret()).compact();
			user.setToken(passwordEncoder.encode(newToken));
			user = userRepository.save(user);
			user.setToken(newToken);
			return user;
		}
	}

}
