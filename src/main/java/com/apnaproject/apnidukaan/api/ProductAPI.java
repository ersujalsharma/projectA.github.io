package com.apnaproject.apnidukaan.api;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apnaproject.apnidukaan.dto.ProductDTO;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;
import com.apnaproject.apnidukaan.service.CustomerProductService;

//add the missing annotations

@RequestMapping(value = "/product-api")
public class ProductAPI {

	private CustomerProductService customerProductService;

	private Environment environment;

	Log logger = LogFactory.getLog(ProductAPI.class);

	
	// Get all the product details by calling getAllProducts() of
    // CustomerProductService and return the same
	
	@GetMapping(value = "/products")
	public ResponseEntity<List<ProductDTO>> getAllProducts() throws ApniDukaanException {
		
		// write your logic here
		return null;

	}

	@GetMapping(value = "/product/{productId}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer productId) throws ApniDukaanException {

		logger.info("Received a request to get product details for product with productId as " + productId);
		ProductDTO product = customerProductService.getProductById(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@PutMapping(value = "/update/{productId}")
	public ResponseEntity<String> reduceAvailableQuantity(@PathVariable Integer productId, @RequestBody String quantity)
			throws ApniDukaanException {

		logger.info("Received a request to update the available quantity  for product with productId as " + productId);
		customerProductService.reduceAvailableQuantity(productId, Integer.parseInt(quantity));
		return new ResponseEntity<>(environment.getProperty("ProductAPI.REDUCE_QUANTITY_SUCCESSFULL"), HttpStatus.OK);

	}
}