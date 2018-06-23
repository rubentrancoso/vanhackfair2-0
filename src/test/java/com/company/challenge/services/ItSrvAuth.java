package com.company.challenge.services;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.company.challenge.Application;
import com.company.challenge.entities.User;
import com.company.challenge.helper.Json;
import com.company.challenge.repositories.UserRepository;
import com.company.challenge.userapi.inputs.Credentials;
import com.company.challenge.userapi.message.Message;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
public class ItSrvAuth {

	private static final Logger logger = LoggerFactory.getLogger(ItSrvAuth.class);
	
	@Autowired
	private SrvUser userService;

	@Autowired
	private SrvAuth authService;
	
	@Autowired
	private UserRepository userRepository;	

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Before
	public void setup() {
		userRepository.deleteAll();
	}
	
	@Test
	public void testLoginSuccess() throws Exception {
		logger.info(">>> testLoginSuccess ");
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");	
		Object message = userService.register(user);
		logger.info("@@@ testLoginSuccess " + message.toString());
		then(message).isInstanceOf(User.class);
		logger.info(Json.prettyPrint(message));

		message = authService.login(new Credentials("username@email.com","1234"));
		then(message).isInstanceOf(User.class);
		logger.info(Json.prettyPrint(message));
		logger.info("<<< testLoginSuccess ");
	}	
	
	@Test
	public void testLoginInvalidPassword() throws Exception {
		logger.info(">>> testLoginInvalidPassword ");
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");	
		Object message = userService.register(user);
		logger.info("@@@ testLoginInvalidPassword " + message.toString());
		then(message).isInstanceOf(User.class);
		logger.info(Json.prettyPrint(message));

		message = authService.login(new Credentials("username@email.com","2345"));
		then(message).isInstanceOf(Message.class);
		then(((Message)message).getMessage()).isEqualTo(Message.INVALID_USERNAME_PASSWORD);
		logger.info(Json.prettyPrint(message));		
		logger.info("<<< testLoginInvalidPassword ");
	}	

	@Test
	public void testLoginInvalidUsername() throws Exception {
		logger.info(">>> testLoginInvalidUsername ");
		Object message = authService.login(new Credentials("username@email.com","2345"));
		then(message).isInstanceOf(Message.class);
		logger.info("@@@ testLoginInvalidUsername " + message.toString());
		then(((Message)message).getMessage()).isEqualTo(Message.INVALID_USERNAME_PASSWORD);
		logger.info(Json.prettyPrint(message));		
		logger.info("<<< testLoginInvalidUsername ");
	}	

	@Test
	public void testEncryptedToken() throws Exception {
		logger.info(">>> testEncryptedToken ");
		logger.info("@@@ testEncryptedToken");
		User user1 = new User("User Name", "username@email.com");
		user1.setPassword("1234");	
		Object message = userService.register(user1);
		then(message).isInstanceOf(User.class);
		logger.info(Json.prettyPrint(message));
		user1 = (User)message;
		String token = ((User)message).getToken();
		User user2 = userRepository.findByEmail(user1.getEmail());		
		logger.info(Json.prettyPrint(user2));
		String encryptedToken = user2.getToken();
		then(passwordEncoder.matches(token, encryptedToken)).isTrue();
		logger.info("<<< testEncryptedToken ");
	}	
	
	@Test
	public void testLoginNullPassword() throws Exception {
		logger.info(">>> testLoginInvalidPassword ");
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");	
		Object message = userService.register(user);
		logger.info("@@@ testLoginInvalidPassword " + message.toString());
		then(message).isInstanceOf(User.class);
		logger.info(Json.prettyPrint(message));

		message = authService.login(new Credentials("username@email.com",null));
		then(message).isInstanceOf(Message.class);
		then(((Message)message).getMessage()).isEqualTo(Message.INVALID_USERNAME_PASSWORD);
		logger.info(Json.prettyPrint(message));		
		logger.info("<<< testLoginInvalidPassword ");
	}	

	@Test
	public void testLoginEmptyPassword() throws Exception {
		logger.info(">>> testLoginInvalidPassword ");
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");	
		Object message = userService.register(user);
		logger.info("@@@ testLoginInvalidPassword " + message.toString());
		then(message).isInstanceOf(User.class);
		logger.info(Json.prettyPrint(message));

		message = authService.login(new Credentials("username@email.com", ""));
		then(message).isInstanceOf(Message.class);
		then(((Message)message).getMessage()).isEqualTo(Message.INVALID_USERNAME_PASSWORD);
		logger.info(Json.prettyPrint(message));		
		logger.info("<<< testLoginInvalidPassword ");
	}	

	
	
}