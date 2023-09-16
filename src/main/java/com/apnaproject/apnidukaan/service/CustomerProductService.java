package com.apnaproject.apnidukaan.service;

import java.util.List;

import com.apnaproject.apnidukaan.dto.ProductDTO;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;


public interface CustomerProductService {
	List<ProductDTO> getAllProducts() throws ApniDukaanException;

	ProductDTO getProductById(Integer productId) throws ApniDukaanException;

	void reduceAvailableQuantity(Integer productId, Integer quantity) throws ApniDukaanException;
}