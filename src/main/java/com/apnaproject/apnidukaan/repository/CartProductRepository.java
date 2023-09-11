package com.apnaproject.apnidukaan.repository;

import org.springframework.data.repository.CrudRepository;

import com.apnaproject.apnidukaan.entity.CartProduct;


public interface CartProductRepository extends CrudRepository<CartProduct, Integer> {

}