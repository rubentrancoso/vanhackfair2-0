package com.company.challenge.services.interfaces;

import javax.transaction.Transactional;

import com.company.challenge.entities.User;

@Transactional
public interface ISrvUser {

	public Object register(User user);

	public Object profile(String uuid);

}
