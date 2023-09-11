package com.apnaproject.apnidukaan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apnaproject.apnidukaan.dto.CustomerDTO;
import com.apnaproject.apnidukaan.entity.Customer;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;
import com.apnaproject.apnidukaan.repository.CustomerRepository;


@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public String registerNewCustomer(CustomerDTO customerDTO) throws ApniDukaanException {
		String regiseterWithEmailId = null;
		boolean isEmailNotAvailable = customerRepository.findById(customerDTO.getEmailId().toLowerCase()).isEmpty();
		boolean isPhoneNumberNotAvailable = customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber()).isEmpty();
		if(isEmailNotAvailable) {
			if(isPhoneNumberNotAvailable) {
				Customer customer = new Customer();
				customer.setEmailId(customerDTO.getEmailId().toLowerCase());
				customer.setName(customerDTO.getName());
				customer.setPassword(customerDTO.getPassword());
				customer.setPhoneNumber(customerDTO.getPhoneNumber());
				customer.setAddress(customerDTO.getAddress());
				customerRepository.save(customer);
				regiseterWithEmailId = customer.getEmailId();
			}
			else {
				throw new ApniDukaanException("Phone Number Already in Use.");
			}
		}
		else {
			throw new ApniDukaanException("Email Already in Use");
		}
		return regiseterWithEmailId;
	}

	@Override
	public CustomerDTO authenticateCustomer(String emailId, String password) throws ApniDukaanException {
		CustomerDTO customerDTO = null;
		Optional<Customer> optionalCustomer = customerRepository.findById(emailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(()->new ApniDukaanException("Customer Not Found"));
		if(!password.equals(customer.getPassword()))
			throw new ApniDukaanException("Invalid Credentials");
		customerDTO = new CustomerDTO();
		customerDTO.setEmailId(customer.getEmailId());
		customerDTO.setName(customer.getName());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setPassword(customer.getPassword());
		customerDTO.setNewPassword(customer.getPassword());
		customerDTO.setAddress(customer.getAddress());
		return customerDTO;
	}

	@Override
	public CustomerDTO getCustomerByEmailId(String emailId) throws ApniDukaanException {
		Optional<Customer> optionalCustomer = customerRepository.findByEmailId(emailId);
		Customer customer = optionalCustomer
				.orElseThrow(() -> new ApniDukaanException("CustomerService.CUSTOMER_NOT_FOUND"));
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId(emailId);
		customerDTO.setName(customer.getName());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setPassword(customer.getPassword());
		customerDTO.setNewPassword(customer.getPassword());
		customerDTO.setAddress(customer.getAddress());
		return customerDTO;
	}

	@Override
	public void updateShippingAddress(String customerEmailId,String address) throws ApniDukaanException {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new ApniDukaanException("CustomerService.CUSTOMER_NOT_FOUND"));
		customer.setAddress(address);
		
	}

	@Override
	public void deleteShippingAddress(String customerEmailId) throws ApniDukaanException {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new ApniDukaanException("CustomerService.CUSTOMER_NOT_FOUND"));
		customer.setAddress(null);
	}

	
}
