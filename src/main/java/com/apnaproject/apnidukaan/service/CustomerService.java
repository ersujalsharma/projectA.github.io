package com.apnaproject.apnidukaan.service;

import com.apnaproject.apnidukaan.entity.CustomerDTO;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;

public interface CustomerService {
	String registerNewCustomer(CustomerDTO customerDTO) throws ApniDukaanException;
	CustomerDTO authenticateCustomer(String emailId, String password) throws ApniDukaanException;
}
