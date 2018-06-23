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

import com.company.challenge.entities.User;
import com.company.challenge.services.interfaces.ISrvAuth;
import com.company.challenge.userapi.inputs.Credentials;
import com.company.challenge.userapi.message.Message;

@Controller
public class Auth {
	
	private static final Logger logger = LoggerFactory.getLogger(Auth.class);
	
	@Autowired
	ISrvAuth authService;
	
    @RequestMapping(path="/login", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE) 
    public @ResponseBody ResponseEntity<?> login(@RequestBody Credentials credentials) {
    	HttpStatus responseCode = HttpStatus.OK;
    	logger.info(String.format("/login reguest for email: %s", credentials.getUsername()));
    	Object message = authService.login(credentials);
    	if(message instanceof Message) {
    		// For security reasons it will ever respond with UNAUTHORIZED
    		responseCode = HttpStatus.UNAUTHORIZED;
    	} else {
    		((User)message).setPassword(null);
    	}
        return new ResponseEntity<Object>(message, responseCode);
    }
}

