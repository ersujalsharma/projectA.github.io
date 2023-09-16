package com.apnaproject.apnidukaan.service;

import java.util.List;
import java.util.Optional;

import com.apnaproject.apnidukaan.dto.ProductDTO;
import com.apnaproject.apnidukaan.entity.Product;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;
import com.apnaproject.apnidukaan.repository.ProductRepository;


//add the missing annotations

public class CustomerProductServiceImpl implements CustomerProductService {

	private ProductRepository productRepository;

	
	// Get all the product details from the database
    // And return the same
	
	@Override
	public List<ProductDTO> getAllProducts() throws ApniDukaanException {
		
		// write your logic here
		return null;
		
	}

	@Override
	public ProductDTO getProductById(Integer productId) throws ApniDukaanException {

		Optional<Product> productOp = productRepository.findById(productId);
		Product product = productOp.orElseThrow(() -> new ApniDukaanException("ProductService.PRODUCT_NOT_AVAILABLE"));

		ProductDTO productDTO = new ProductDTO();
		productDTO.setBrand(product.getBrand());
		productDTO.setCategory(product.getCategory());
		productDTO.setDescription(product.getDescription());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setProductId(product.getProductId());
		productDTO.setAvailableQuantity(product.getAvailableQuantity());

		return productDTO;
	}

	@Override
	public void reduceAvailableQuantity(Integer productId, Integer quantity) throws ApniDukaanException {
		Optional<Product> productOp = productRepository.findById(productId);
		Product product = productOp.orElseThrow(() -> new ApniDukaanException("ProductService.PRODUCT_NOT_AVAILABLE"));
		product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
	}
}