package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


import java.util.List;

import javax.validation.Valid;

@Api( description = "API pour es opérations CRUD sur les produits.")
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
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value="/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
    	
    	Product product = productDao.findById(id);
    	
    	if(product == null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
    			
    	return product;
    	
    }
    
    @ApiOperation(value = "Récupère un produit grâce à son PRIX à condition que celui-ci soit superieur au montant demandé !")
    @GetMapping(value="Produits/prix_greater_than/{prixLimit}")
    public List<Product> afficherUnProduitAvecPrixSupp(@PathVariable int prixLimit) {
    	
    	return productDao.findByPrixGreaterThan(prixLimit);
    	
    }
    
    @ApiOperation(value = "Récupère un produit grâce à son PRIX à condition que celui-ci soit inferieur au montant demandé !")
    @GetMapping(value="Produits/prix_lower_than/{prixLimit}")
    public List<Product> afficherUnProduitAvecPrixInf(@PathVariable int prixLimit) {
    	
    	return productDao.findByPrixLessThan(prixLimit);
    	
    }
    
    @GetMapping(value = "Produits/rechercher/{mot}")
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
