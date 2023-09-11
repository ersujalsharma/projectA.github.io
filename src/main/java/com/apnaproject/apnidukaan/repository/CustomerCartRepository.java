package com.apnaproject.apnidukaan.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.apnaproject.apnidukaan.entity.CustomerCart;


public interface CustomerCartRepository extends CrudRepository<CustomerCart, Integer> {
	Optional<CustomerCart> findByCustomerEmailId(String customerEmailId);
}