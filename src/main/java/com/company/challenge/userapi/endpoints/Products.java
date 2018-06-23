package com.company.challenge.userapi.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.challenge.entities.Product;
import com.company.challenge.services.interfaces.ISrvProduct;
import com.company.challenge.userapi.message.Message;

@Controller
@RequestMapping(value = "/products")
public class Products {

	private static final Logger logger = LoggerFactory.getLogger(Products.class);

	@Autowired
	ISrvProduct productService;

	@RequestMapping(value = "/add", method = RequestMethod.OPTIONS)
    public ResponseEntity<Object> handle() {
        return new ResponseEntity<Object>(HttpStatus.OK);
    }	
	
	@RequestMapping(path = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object add(@RequestBody Product product) {
		HttpStatus responseCode = HttpStatus.OK;
		logger.info(String.format("/adding product  with name: %s", product.getName()));
		Object message = productService.add(product);
		if (message instanceof Message) {
			String textMessage = ((Message) message).getMessage();
			switch (textMessage) {
			case Message.PRODUCT_ALREADY_EXISTS:
				responseCode = HttpStatus.CONFLICT;
				break;
			case Message.INVALID_PRODUCT:
				responseCode = HttpStatus.BAD_REQUEST;
				break;
			default:
				responseCode = HttpStatus.BAD_REQUEST;
			}
		}
		return new ResponseEntity<Object>(message, responseCode);
	}
	
	@RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object listAll() {
		HttpStatus responseCode = HttpStatus.OK;
		logger.info(String.format("/listing products"));
		Object message = productService.list();
		return new ResponseEntity<Object>(message, responseCode);
	}	

}
