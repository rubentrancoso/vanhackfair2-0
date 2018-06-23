package com.company.challenge.repositories;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.company.challenge.Application;
import com.company.challenge.entities.Phone;
import com.company.challenge.entities.User;
import com.company.challenge.helper.UUIDGen;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
public class ItUserRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(ItUserRepository.class);
	
	private final String UUID_MASK = "([a-f]|\\d){32}"; // 32 alphanumeric chars

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setup() {
		userRepository.deleteAll();
	}	
	
	@Test
	public void saveSingleUser() throws Exception {
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");
		userRepository.save(user);
		
		then(userRepository.findByEmail("username@email.com")).isNotNull();
		logger.info("@@@ saveSingleUser: " + user.toString());	
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void saveDuplicatedUser() throws Exception {
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");
		userRepository.save(user);
		user = userRepository.findByEmail("username@email.com");
		logger.info("@@@ saveDuplicatedUser: " + user.toString());	

		user = new User("User Name", "username@email.com");
		user.setPassword("1234");
		userRepository.save(user);
		user = userRepository.findByEmail("username@email.com");
		logger.info("@@@ saveDuplicatedUser: " + user.toString());	
	}

	@Test
	public void deleteSingleUser() throws Exception {
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");
		userRepository.save(user);
		userRepository.delete(user);	
		user = userRepository.findByEmail("username@email.com");
		then(user).isNull();
		logger.info("@@@ deleteSingleUser: " + user);
	}

	@Test
	public void testUserUUIDGenerated() throws Exception {
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");
		userRepository.save(user);
		
		assertTrue(user.getId().matches(UUID_MASK));
		logger.info("@@@ testUserUUID: " + user.toString());
	}

	@Test
	public void testUserUUIDPersist() throws Exception {
		String uuid1;
		String uuid2;
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");
		user = userRepository.save(user);
		uuid1 = user.getId();
		user = userRepository.findByEmail("username@email.com");
		user.setPassword("2345");
		user = userRepository.save(user);
		uuid2 = user.getId();

		assertEquals(uuid1, uuid2);
		logger.info("@@@ testUserUUID: " + user.toString());
	}

	@Test
	public void testUserCreatedDate() throws Exception {
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");
		userRepository.save(user);
		
		assertNotNull(user.getCreated());
		logger.info("@@@ testUserCreatedDate: " + user.toString());
	}

	@Test
	public void testUserModifiedDate() throws Exception {
		User user = new User("User Name", "username@email.com");
		user.setPassword("1234");
		userRepository.save(user);
		
		assertNotNull(user.getModified());
		logger.info("@@@ testUserModifiedDate: " + user.toString());
	}

	@Test
	public void testUserCreatedVsModifiedDate() throws Exception {
		User user = new User("User Name", "username@email.com");
		
		user.setPassword("1234");
		userRepository.save(user);
		user = userRepository.findByEmail("username@email.com");
		logger.info(String.format("@@@ testUserCreatedVsModifiedDate - [created=%s] [modified=%s]", user.getCreated(), user.getModified()));
		
		user.setPassword("2345");
		userRepository.save(user);
		user = userRepository.findByEmail("username@email.com");
		logger.info(String.format("@@@ testUserCreatedVsModifiedDate - [created=%s] [modified=%s]", user.getCreated(), user.getModified()));
		
		assertNotEquals(user.getCreated(), user.getModified());
		logger.info("@@@ testUserCreatedVsModifiedDate: " + user.toString());
	}

	
	@Test
	public void testNoColisionUserUUID() throws Exception {
		User user;
		for(int i=0;i<5000;i++) {
			String emailbox = UUIDGen.getUUID();
			user = new User("User Name", emailbox + "@email.com");
			user.setPassword("1234");
			userRepository.save(user);
		}
	}

	@Test
	public void saveUserWithSinglePhone() throws Exception {
		User user = new User("User Name", "user@email.com");
		user.setPassword("1234");
		
		Set<Phone> phonesList = new HashSet<Phone>();
		phonesList.add(new Phone(11,991231234));   
		
		user.addPhone(new Phone(11,991231234));
		userRepository.save(user);
		user = userRepository.findByEmail("user@email.com");
		
		then(user).isNotNull();
		then(new HashSet<Phone>(user.getPhones())).isEqualTo(phonesList);
		logger.info("@@@ saveUserWithSinglePhone: " + user.toString());
	}

	@Test
	public void saveUserWithMultiplePhones() throws Exception {
		User user = new User("User Name", "user@email.com");
		user.setPassword("1234");
		
		Set<Phone> phonesList = new HashSet<Phone>();
		phonesList.add(new Phone(11,991231234));
		phonesList.add(new Phone(11,991232345));
		
		user.addPhone(new Phone(11,991231234));
		user.addPhone(new Phone(11,991232345));
		
		userRepository.save(user);
		user = userRepository.findByEmail("user@email.com");
		
		then(user).isNotNull();
		then(new HashSet<Phone>(user.getPhones())).isEqualTo(phonesList);
		logger.info("@@@ saveUserWithMultiplePhones: " + user.toString());
	}
	
	@Test
	public void saveUserWithDuplicatedPhone() throws Exception {
		User user = new User("User Name", "user@email.com");
		user.setPassword("1234");

		Set<Phone> phonesList = new HashSet<Phone>();
		phonesList.add(new Phone(11,991231234));
		
		user.addPhone(new Phone(11,991231234));
		user.addPhone(new Phone(11,991231234));

		userRepository.save(user);
		user = userRepository.findByEmail("user@email.com");
		then(user).isNotNull();
		logger.info("@@@ saveUserWithDuplicatedPhone: " + user.toString());
		then(new HashSet<Phone>(user.getPhones())).isEqualTo(phonesList);
	}

	@Test
	public void deletePhonesFromUser() throws Exception {
		Set<Phone> phonesList1 = new HashSet<Phone>();
		phonesList1.add(new Phone(11,991231234));
		phonesList1.add(new Phone(11,991232345));

		User user1 = new User("User Name", "user@email.com");
		user1.setPassword("1234");
		
		user1.addPhone(new Phone(11,991231234));
		user1.addPhone(new Phone(11,991232345));

		userRepository.save(user1);
		
		User user2 = userRepository.findByEmail("user@email.com");
		then(user2).isNotNull();
		then(user2.getPhones()).isEqualTo(phonesList1);
		
		user2.setPhones(null);
		userRepository.save(user2);
		User user3 = userRepository.findByEmail("user@email.com");
		
		then(user3).isNotNull();
		then(user3.getPhones()).isEmpty();;
		logger.info("@@@ deletePhonesFromUser: " + user3.toString());
	}

	@Test
	public void deleteSinglePhoneFromUser() throws Exception {
		User user = new User("User Name", "user@email.com");
		user.setPassword("1234");
		
		Set<Phone> phonesList = new HashSet<Phone>();
		phonesList.add(new Phone(11,991231234));
		phonesList.add(new Phone(11,991232345));

		Set<Phone> newPhonesList = new HashSet<Phone>();
		newPhonesList.add(new Phone(11,991231234));
		
		user.addPhone(new Phone(11,991231234));
		user.addPhone(new Phone(11,991232345));
		
		userRepository.save(user);
		User user2 = userRepository.findByEmail("user@email.com");
		
		then(user2).isNotNull();
		then(user2.getPhones()).isEqualTo(phonesList);
		
		logger.info("@@@ deleteSinglePhoneFromUser: " + user2.toString());
		
		user2.removePhone(new Phone(11,991232345));
		userRepository.save(user2);
		User user3 = userRepository.findByEmail("user@email.com");
		then(user).isNotNull();
		logger.info("@@@ deleteSinglePhoneFromUser: " + user3.toString());
		then(user3.getPhones()).isEqualTo(newPhonesList);
	}
	
	@Test
	public void updateUserPhoneList() throws Exception {
		User user = new User("User Name", "user@email.com");
		user.setPassword("1234");
		
		Set<Phone> phonesList = new HashSet<Phone>();
		phonesList.add(new Phone(11,991231234));
		phonesList.add(new Phone(11,991232345));

		Set<Phone> newPhonesList = new HashSet<Phone>();
		newPhonesList.add(new Phone(11,991231234));
		newPhonesList.add(new Phone(11,991233456));
		
		user.addPhone(new Phone(11,991231234));
		user.addPhone(new Phone(11,991232345));
		
		userRepository.save(user);
		user = userRepository.findByEmail("user@email.com");
		
		then(user).isNotNull();
		then(user.getPhones()).isEqualTo(phonesList);
		
		logger.info("@@@ updateUserPhoneList: " + user.toString());
		
		user.removePhone(new Phone(11,991232345));
		user.addPhone(new Phone(11,991233456));
		userRepository.save(user);
		user = userRepository.findByEmail("user@email.com");
		then(user).isNotNull();
		logger.info("@@@ updateUserPhoneList: " + user.toString());
		then(user.getPhones()).isEqualTo(newPhonesList);
	}	

	@Test
	public void twoUsersWithSamePhoneExclusiveRecords() throws Exception {
		User user1 = new User("User Name", "user1@email.com");
		user1.setPassword("1234");
		User user2 = new User("User Name", "user2@email.com");
		user2.setPassword("1234");
		
		Set<Phone> phonesList = new HashSet<Phone>();
		phonesList.add(new Phone(11,991231234));
		
		user1.addPhone(new Phone(11,991231234));
		user2.addPhone(new Phone(11,991231234));
		
		userRepository.save(user1);
		userRepository.save(user2);
		
		user1 = userRepository.findByEmail("user1@email.com");
		user2 = userRepository.findByEmail("user2@email.com");

		Set<Phone> phones1 = user1.getPhones();
		Phone phone1 = phones1.toArray(new Phone[phones1.size()])[0];

		Set<Phone> phones2 = user2.getPhones();
		Phone phone2 = phones2.toArray(new Phone[phones2.size()])[0];
		
		then(phone1.getId()).isNotEqualTo(phone2.getId());
	}	
	
	
}