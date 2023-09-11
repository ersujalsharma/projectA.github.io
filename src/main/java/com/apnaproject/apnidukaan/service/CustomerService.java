package com.apnaproject.apnidukaan.service;

import com.apnaproject.apnidukaan.dto.CustomerDTO;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;

public interface CustomerService {
	String registerNewCustomer(CustomerDTO customerDTO) throws ApniDukaanException;
	CustomerDTO authenticateCustomer(String emailId, String password) throws ApniDukaanException;
	CustomerDTO getCustomerByEmailId(String emailId) throws ApniDukaanException; 
	void updateShippingAddress(String customerEmailId, String address) throws ApniDukaanException;
	void deleteShippingAddress(String customerEmailId) throws ApniDukaanException;
}
