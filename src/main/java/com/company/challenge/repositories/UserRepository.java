package com.company.challenge.repositories;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.company.challenge.entities.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
	@Transactional
	public User findById(String UUID);
	@Transactional
	public User findByEmail(String email); 
}
