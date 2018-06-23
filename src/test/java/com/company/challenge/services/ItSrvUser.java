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
import com.company.challenge.entities.Phone;
import com.company.challenge.entities.User;
import com.company.challenge.helper.Json;
import com.company.challenge.repositories.UserRepository;
import com.company.challenge.userapi.message.Message;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
public class ItSrvUser {

	private static final Logger logger = LoggerFactory.getLogger(ItSrvUser.class);
	
	@Autowired
	private SrvUser userService;
	
	@Autowired
	private UserRepository userRepository;	

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Before
	public void setup() {
		userRepository.deleteAll();
	}	
	
	@Test
	public void registerUser() throws Exception {
		logger.info(">>> registerUser ");
		User user = new User("User Name", "username@email.com");
		user.setPassword(passwordEncoder.encode("1234"));	
		user.addPhone(new Phone(11,912341234));
		Object message = userService.register(user);
		logger.info("@@@ registerUser " + message.toString());
		then(message).isInstanceOf(User.class);
		logger.info(Json.prettyPrint(message));
		logger.info("<<< registerUser ");
	}
	
	@Test
	public void registerDuplicatedUser() throws Exception {
		logger.info(">>> registerDuplicatedUser ");
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");	
		user.addPhone(new Phone(11,912341234));
		Object message = userService.register(user);
		logger.info("@@@ registerDuplicatedUser " + message.toString());

		user = new User("User Name", "username@email.com");
		user.setPassword("1234");	
		user.addPhone(new Phone(11,912341234));
		message = userService.register(user);
		logger.info("@@@ registerDuplicatedUser " + message.toString());
		
		then(message).isInstanceOf(Message.class);
		then(((Message)message).getMessage()).isEqualTo(Message.EMAIL_ALREADY_TAKEN);
		logger.info(Json.prettyPrint(message));
		logger.info("<<< registerDuplicatedUser ");
	}
	
	@Test
	public void testPassword() throws Exception {
		logger.info(">>> testPassword ");
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");	
		Object message = userService.register(user);
		logger.info("@@@ testPassword " + message.toString());
		then(message).isInstanceOf(User.class);
		logger.info(Json.prettyPrint(message));
		user = userRepository.findByEmail(((User)message).getEmail());
		then(passwordEncoder.matches("1234", user.getPassword())).isTrue();
		logger.info("<<< testPassword ");
	}	
	
}
