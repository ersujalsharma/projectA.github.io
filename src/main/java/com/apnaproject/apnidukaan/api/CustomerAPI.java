package com.apnaproject.apnidukaan.api;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apnaproject.apnidukaan.dto.CustomerCredDTO;
import com.apnaproject.apnidukaan.dto.CustomerDTO;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;
import com.apnaproject.apnidukaan.service.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

//add the missing annotations

@RequestMapping(value = "/customer-api")
public class CustomerAPI {

	private CustomerService customerService;

	private Environment environment;

	static Log logger = LogFactory.getLog(CustomerAPI.class);

	@PostMapping(value = "/login")
	public ResponseEntity<CustomerDTO> authenticateCustomer(@Valid @RequestBody CustomerCredDTO custCredDTO)
			throws ApniDukaanException {

		logger.info("CUSTOMER TRYING TO LOGIN, VALIDATING CREDENTIALS. CUSTOMER EMAIL ID: " + custCredDTO.getEmailId());
		CustomerDTO customerDTOFromDB = customerService.authenticateCustomer(custCredDTO.getEmailId(),
				custCredDTO.getPassword());
		logger.info("CUSTOMER LOGIN SUCCESS, CUSTOMER EMAIL : " + customerDTOFromDB.getEmailId());
		return new ResponseEntity<>(customerDTOFromDB, HttpStatus.OK);
	}

	@PostMapping(value = "/register")
	public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws ApniDukaanException {

		logger.info("CUSTOMER TRYING TO REGISTER. CUSTOMER EMAIL ID: " + customerDTO.getEmailId());
		String registeredWithEmailID = customerService.registerNewCustomer(customerDTO);
		registeredWithEmailID = environment.getProperty("CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS")
				+ registeredWithEmailID;
		return new ResponseEntity<>(registeredWithEmailID, HttpStatus.OK);
	}

	@PutMapping(value = "/customer/{customerEmailId:.+}/address/")
	public ResponseEntity<String> updateShippingAddress(
			@PathVariable @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") String customerEmailId,
			@RequestBody String address) throws ApniDukaanException {

		customerService.updateShippingAddress(customerEmailId, address);
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.UPDATE_ADDRESS_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@DeleteMapping(value = "/customer/{customerEmailId:.+}")
	public ResponseEntity<String> deleteShippingAddress(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws ApniDukaanException {

		customerService.deleteShippingAddress(customerEmailId);
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_ADDRESS_DELETED_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

}