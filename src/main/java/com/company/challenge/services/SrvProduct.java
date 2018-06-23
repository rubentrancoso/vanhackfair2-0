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

	@Override
	public Object list() {
		logger.info("listing product...");
		return productRepository.findAllByOrderByIdDesc();
	}

}
