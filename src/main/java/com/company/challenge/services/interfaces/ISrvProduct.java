package com.company.challenge.services.interfaces;

import javax.transaction.Transactional;

import com.company.challenge.entities.Product;

@Transactional
public interface ISrvProduct {

	public Object add(Product product);

	public Object list();

}
