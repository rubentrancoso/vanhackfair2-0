package com.company.challenge.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.company.challenge.entities.Product;
import com.company.challenge.entities.User;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, String> {
	@Transactional
	public User findById(int id);
	@Transactional
	public User findByName(String name); 
	
	public List<Product> findAllByOrderByIdDesc();
	
}
