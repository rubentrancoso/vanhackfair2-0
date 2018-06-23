package com.company.challenge.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.challenge.repositories.UserRepository;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		com.company.challenge.entities.User applicationUser = userRepository.findByEmail(email);
		
		if (applicationUser == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new User(applicationUser.getEmail(), applicationUser.getPassword(), emptyList());
	}
}