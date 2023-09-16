package com.apnaproject.apnidukaan.service;

import java.util.Set;

import com.apnaproject.apnidukaan.dto.CartProductDTO;
import com.apnaproject.apnidukaan.dto.CustomerCartDTO;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;


public interface CustomerCartService {

	Integer addProductToCart(CustomerCartDTO customerCart) throws ApniDukaanException;

	Set<CartProductDTO> getProductsFromCart(String customerEmailId) throws ApniDukaanException;

	void modifyQuantityOfProductInCart(String customerEmailId, Integer productId, Integer quantity)
			throws ApniDukaanException;

	void deleteProductFromCart(String customerEmailId, Integer productId) throws ApniDukaanException;

	void deleteAllProductsFromCart(String customerEmailId) throws ApniDukaanException;

}