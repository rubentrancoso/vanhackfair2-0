package com.company.challenge.userapi.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.challenge.userapi.message.Message;

@Controller
public class Hello {

	private static final Logger logger = LoggerFactory.getLogger(Hello.class);
	
    @RequestMapping(path="/hello", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
    public @ResponseBody Object hello() {
    	logger.info("/hello endpoing requested.");
        return new Message("hello");
    }

}