package com.apnaproject.apnidukaan.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.apnaproject.apnidukaan.entity.Order;


public interface OrderRepository extends CrudRepository<Order, Integer> {

	List<Order> findByCustomerEmailId(String emailId);

}