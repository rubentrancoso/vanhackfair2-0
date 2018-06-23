package com.company.challenge.services;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.challenge.config.JwtConfig;
import com.company.challenge.entities.User;
import com.company.challenge.repositories.UserRepository;
import com.company.challenge.services.interfaces.ISrvUser;
import com.company.challenge.userapi.message.Message;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SrvUser implements ISrvUser {

	private static final Logger logger = LoggerFactory.getLogger(SrvUser.class);
	
	@Autowired
	JwtConfig jwtConfig;

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	SrvUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public Object register(User user) {
		if(
			user == null ||
			user.getEmail() == null ||
			user.getEmail().isEmpty() ||
			user.getPassword() == null ||
			user.getPassword().isEmpty()
		) 
		{
			return new Message(Message.INVALID_USERNAME_PASSWORD);
		}
		
		String newToken = Jwts.builder().setSubject(user.getEmail())
				.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
				.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret()).compact();
		
		User newUser = new User(user.getName(), user.getEmail(), user.getPassword());
		newUser.setPhones(user.getPhones());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setToken(passwordEncoder.encode(newToken));
		Object message = registerTransaction(newUser);
		if(message instanceof User) {
			// return the unencrypted token only once
			((User)message).setToken(newToken);
			// do not return the encrypted password
			((User)message).setPassword(null);
		}
		return message;
	}
	
	@Transactional
	private Object registerTransaction(User user) {
		Object result;
		try {
			result = userRepository.save(user);
		} catch(DataIntegrityViolationException e) {
			logger.error(e.getMessage());
			result = new Message(Message.EMAIL_ALREADY_TAKEN);
		}

		return result;
	}

	@Override
	public Object profile(String uuid) {
		logger.info("requesting profile...");
		return userRepository.findById(uuid);
	}

}
