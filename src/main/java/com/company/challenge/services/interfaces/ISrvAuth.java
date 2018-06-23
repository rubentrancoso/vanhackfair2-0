package com.company.challenge.services.interfaces;

import javax.transaction.Transactional;

import com.company.challenge.userapi.inputs.Credentials;

@Transactional
public interface ISrvAuth {
	
	public Object login(Credentials credentials);

}
