package com.ecommerce.microcommerce.dao;

import com.ecommerce.microcommerce.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository 
public interface ProductDao  extends JpaRepository<Product, Integer> {
	
	public abstract Product findById(int id);
	
	public abstract Product save(Product product);
	
	public abstract List<Product> findByPrixGreaterThan(int prixLimit);
	
	public abstract List<Product> findByPrixLessThan(int prixLimit);
	
	public abstract List<Product> findByNomLike(String recherche);
	
}
