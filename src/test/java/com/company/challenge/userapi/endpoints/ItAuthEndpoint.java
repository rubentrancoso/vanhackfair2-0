package com.company.challenge.userapi.endpoints;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.company.challenge.Application;
import com.company.challenge.entities.Phone;
import com.company.challenge.entities.User;
import com.company.challenge.helper.Json;
import com.company.challenge.repositories.UserRepository;
import com.company.challenge.userapi.message.Message;

import net.minidev.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class ItAuthEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(ItAuthEndpoint.class);
	
	@LocalServerPort
	private int port;

	@Value("${local.management.port}")
	private int mgt;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void setup() {
		userRepository.deleteAll();
	}
	
	@Test
	public void testRestRegister() throws Exception {
		User user = new User("User Name", "email@domain.com");
		user.setPassword("123456");
		ResponseEntity<Object> response = this.testRestTemplate.postForEntity("/register", user, Object.class);
		@SuppressWarnings("unchecked")
		JSONObject responseBody = new JSONObject((Map<String, ?>) response.getBody());
		logger.info("@@@ testRestLogin - Register response: "+ Json.prettyPrint(response.getBody()));	
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(responseBody.get("email")).isEqualTo("email@domain.com");
	}

	@Test
	public void testRestRegisterTwice() throws Exception {
		User user = new User("User Name", "email@domain.com");
		user.setPassword("123456");
		user.setName("User Name");
		user.addPhone(new Phone(11,991231234));
		user.addPhone(new Phone(11,991232345));
		ResponseEntity<Object> response = this.testRestTemplate.postForEntity("/register", user, Object.class);
		response = this.testRestTemplate.postForEntity("/register", user, Object.class);
		@SuppressWarnings("unchecked")
		JSONObject responseBody = new JSONObject((Map<String, ?>) response.getBody());
		logger.info("@@@ testRestLogin - Register response: "+ Json.prettyPrint(response.getBody()));	
		then(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		then(responseBody.get("message")).isEqualTo(Message.EMAIL_ALREADY_TAKEN);
	}

	
}