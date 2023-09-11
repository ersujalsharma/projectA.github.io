package com.apnaproject.apnidukaan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.apnaproject.apnidukaan.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String>{
	List<Customer> findByPhoneNumber(String phoneNumber);
	Optional<Customer> findByEmailId(String emailId);
}
