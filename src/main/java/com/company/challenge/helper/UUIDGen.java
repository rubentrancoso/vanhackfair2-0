package com.company.challenge.helper;

import java.util.UUID;

public class UUIDGen {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
