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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.company.challenge.Application;
import com.company.challenge.entities.Phone;
import com.company.challenge.entities.User;
import com.company.challenge.helper.Json;
import com.company.challenge.repositories.UserRepository;
import com.company.challenge.services.SrvUser;
import com.company.challenge.userapi.inputs.Credentials;
import com.company.challenge.userapi.message.Message;

import net.minidev.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class ItUserEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(ItUserEndpoint.class);
	
	@LocalServerPort
	private int port;

	@Value("${local.management.port}")
	private int mgt;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SrvUser userService;
	
	@Before
	public void setup() {
		userRepository.deleteAll();
	}
	
	private Object registerUser(String email, String password) throws Exception {
		User user = new User("User Name", email);
		user.setPassword(password);	
		user.addPhone(new Phone(11,912341234));
		return userService.register(user);
	}
	
	@Test
	public void testRestLogin() throws Exception {
		Credentials credentials = new Credentials("email@domain.com", "123456");
		Object message = registerUser(credentials.getUsername(), credentials.getPassword());
		logger.info("@@@ testRestLogin - User registered: "+ Json.prettyPrint((User)message));
		ResponseEntity<Object> response = this.testRestTemplate.postForEntity("/login", credentials, Object.class);
		@SuppressWarnings("unchecked")
		JSONObject responseBody = new JSONObject((Map<String, ?>) response.getBody());
		logger.info("@@@ testRestLogin - Login response: "+ Json.prettyPrint(response.getBody()));	
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(responseBody.get("email")).isEqualTo("email@domain.com");
	}

	@Test
	public void testRestLoginWrongPass() throws Exception {
		Credentials credentials = new Credentials("email@domain.com", "123456");
		Object message = registerUser(credentials.getUsername(), credentials.getPassword());
		logger.info("@@@ testRestLoginWrongPass - User registered: "+ Json.prettyPrint((User)message));
		credentials.setPassword("234567");
		ResponseEntity<Object> response = this.testRestTemplate.postForEntity("/login", credentials, Object.class);
		@SuppressWarnings("unchecked")
		JSONObject responseBody = new JSONObject((Map<String, ?>) response.getBody());
		logger.info("@@@ testRestLoginWrongPass - Login response: "+ Json.prettyPrint(response.getBody()));	
		then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		then(responseBody.get("message")).isEqualTo(Message.INVALID_USERNAME_PASSWORD);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testProfileWithLogin() throws Exception {
		Credentials credentials = new Credentials("email@domain.com", "123456");
		Object message = registerUser(credentials.getUsername(), credentials.getPassword());
		logger.info("@@@ testProfileWithLogin - User registered: "+ Json.prettyPrint((User)message));
		ResponseEntity<Object> response = this.testRestTemplate.postForEntity("/login", credentials, Object.class);
		logger.info("@@@ testProfileWithLogin - Login response: "+ Json.prettyPrint(response.getBody()));	
		JSONObject responseBody = new JSONObject((Map<String, ?>) response.getBody());
		String uuid = (String)responseBody.get("id");
		String token = (String)responseBody.get("token");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		@SuppressWarnings("rawtypes")
		HttpEntity entity = new HttpEntity(headers);
		response = this.testRestTemplate.exchange(
				String.format("/profile/%s",uuid), HttpMethod.GET, entity, Object.class);
		logger.info("@@@ testProfileWithLogin - Profile response: "+ Json.prettyPrint(response.getBody()));	
		responseBody = new JSONObject((Map<String, ?>) response.getBody());
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(responseBody.get("id")).isEqualTo(uuid);
	}
	
}