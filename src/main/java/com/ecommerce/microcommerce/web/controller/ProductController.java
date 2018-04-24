package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


import java.util.List;

import javax.validation.Valid;


@RestController
public class ProductController {
	
	@Autowired
	private ProductDao productDao;


    //Récupérer la liste des produits sans filtre 
    // at RequestMapping(value="/Produits", method=RequestMethod.GET)
	/*
    public List<Product> listeProduits() {
        return productDao.findAll();
    } 
    */
    
    @RequestMapping(value="/Produits", method=RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
    	
    	// List<Product> produits = productDao.findAll();
    	
    	Iterable<Product> produits = productDao.findAll();
    	
    	SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
    	
    	FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
    	
    	MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
    	
    	produitsFiltres.setFilters(listDeNosFiltres); 
    	
    	return produitsFiltres;
    	
    }
    
    

    //Récupérer un produit par son Id
    @GetMapping(value="/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
    	
    	Product product = productDao.findById(id);
    	
    	if(product == null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
    			
    	return product;
    	
    }
    
    @GetMapping(value="produits/prix_greater_than/{prixLimit}")
    public List<Product> afficherUnProduitAvecPrixSupp(@PathVariable int prixLimit) {
    	
    	return productDao.findByPrixGreaterThan(prixLimit);
    	
    }
    
    @GetMapping(value="produits/prix_lower_than/{prixLimit}")
    public List<Product> afficherUnProduitAvecPrixInf(@PathVariable int prixLimit) {
    	
    	return productDao.findByPrixLessThan(prixLimit);
    	
    }
    
    @GetMapping(value = "produits/rechercher/{mot}")
    public List<Product> testeDeRequetes(@PathVariable String mot) {
        return productDao.findByNomLike("%"+mot+"%");
    }
    
    
    @PostMapping(value="/Produits")
    public Product ajouterProduit(@Valid @RequestBody Product product){
    	return productDao.save(product);
    }
    
    @DeleteMapping (value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {

        productDao.deleteById(id);
        
    }
    
    
    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {

        productDao.save(product);
        
    }

    
}
