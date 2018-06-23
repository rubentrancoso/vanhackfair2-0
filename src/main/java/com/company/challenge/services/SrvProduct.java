package com.company.challenge.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.company.challenge.entities.Product;
import com.company.challenge.repositories.ProductRepository;
import com.company.challenge.services.interfaces.ISrvProduct;
import com.company.challenge.userapi.message.Message;

@Service
public class SrvProduct implements ISrvProduct {

	private static final Logger logger = LoggerFactory.getLogger(SrvProduct.class);
	
	private ProductRepository productRepository;

	SrvProduct(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Object add(Product product) {
		if(
				product == null || 
				product.getClass() == null ||
				product.getName().isEmpty()
				) {
			return new Message(Message.INVALID_PRODUCT);
		}
		Object message = addTransaction(product);
		return message;
	}
	
//	@Override
//	public Object register(User user) {
//		if(
//			user == null ||
//			user.getEmail() == null ||
//			user.getEmail().isEmpty() ||
//			user.getPassword() == null ||
//			user.getPassword().isEmpty()
//		) 
//		{
//			return new Message(Message.INVALID_USERNAME_PASSWORD);
//		}
//		
//		String newToken = Jwts.builder().setSubject(user.getEmail())
//				.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
//				.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret()).compact();
//		
//		User newUser = new User(user.getName(), user.getEmail(), user.getPassword());
//		newUser.setPhones(user.getPhones());
//		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
//		newUser.setToken(passwordEncoder.encode(newToken));
//		Object message = registerTransaction(newUser);
//		if(message instanceof User) {
//			// return the unencrypted token only once
//			((User)message).setToken(newToken);
//			// do not return the encrypted password
//			((User)message).setPassword(null);
//		}
//		return message;
//	}
//	
	@Transactional
	private Object addTransaction(Product product) {
		Object result;
		try {
			result = productRepository.save(product);
		} catch(DataIntegrityViolationException e) {
			logger.error(e.getMessage());
			result = new Message(Message.PRODUCT_ALREADY_EXISTS);
		}
		return result;
	}
//
//	@Override
//	public Object profile(String uuid) {
//		logger.info("requesting profile...");
//		return userRepository.findById(uuid);
//	}

	@Override
	public Object list() {
		logger.info("listing product...");
		return productRepository.findAll();
	}

}
