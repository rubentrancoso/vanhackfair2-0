package com.company.challenge.helper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtUUID {
	
	private final String UUID_MASK = "([a-f]|\\d){32}"; // 32 alphanumeric chars
	private final String UUID_WRONGMASK = "([b-f]|\\d){32}"; // 32 alphanumeric chars

	@Test
	public void UUIDFormatIsValid() {
		String uuid = UUIDGen.getUUID();
		assertTrue(uuid.matches(UUID_MASK));
	}

	@Test
	public void UUIDFormatIsConsistent() {
		for(int i=0;i<100;i++) {
			String uuid = UUIDGen.getUUID();
			assertTrue(uuid.matches(UUID_MASK));
		}
	}

	@Test
	public void UUIDWrongFormatIsRejected() {
		boolean matches = true;
		for(int i=0;i<100;i++) {
			String uuid = UUIDGen.getUUID();
			matches = uuid.matches(UUID_WRONGMASK);
			if(!matches) {
				break;
			}
		}
		assertTrue(!matches);
	}

	@Test
	public void UUIDIsRandom() {
		String uuid1 = UUIDGen.getUUID();
		String uuid2 = UUIDGen.getUUID();
		assertNotNull(uuid1);
		assertTrue(uuid1.matches(UUID_MASK));
		assertNotNull(uuid2);
		assertTrue(uuid2.matches(UUID_MASK));
		assertThat(uuid1,not(equalTo(uuid2)));
	}
	
}
