package com.apnaproject.apnidukaan.api;

import java.security.NoSuchAlgorithmException;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apnaproject.apnidukaan.dto.CardDTO;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;
import com.apnaproject.apnidukaan.service.PaymentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

//add the missing annotations

@RequestMapping(value = "/payment-api")
public class PaymentAPI {

	private PaymentService paymentService;

	private Environment environment;

	//private RestTemplate template;

	Log logger = LogFactory.getLog(PaymentAPI.class);

	@PostMapping(value = "/customer/{customerEmailId:.+}/cards")
	public ResponseEntity<String> addNewCard(@RequestBody CardDTO cardDTO,
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws ApniDukaanException, NoSuchAlgorithmException {
		logger.info("Recieved request to add new  card for customer : " + customerEmailId);
		cardDTO.setCustomerEmailId(customerEmailId);

		int cardId;
		cardId = paymentService.addCustomerCard(customerEmailId, cardDTO);
		String message = environment.getProperty("PaymentAPI.NEW_CARD_ADDED_SUCCESS");
		String toReturn = message + cardId;
		toReturn = toReturn.trim();
		return new ResponseEntity<>(toReturn, HttpStatus.OK);

	}

	@PutMapping(value = "/update/card")
	public ResponseEntity<String> updateCustomerCard(@Valid @RequestBody CardDTO cardDTO)
			throws ApniDukaanException, NoSuchAlgorithmException {
		logger.info("Recieved request to update  card :" + cardDTO.getCardId() + " of customer : "
				+ cardDTO.getCustomerEmailId());

		paymentService.updateCustomerCard(cardDTO);
		String modificationSuccessMsg = environment.getProperty("PaymentAPI.UPDATE_CARD_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@DeleteMapping(value = "/customer/{customerEmailId:.+}/card/{cardID}/delete")
	public ResponseEntity<String> deleteCustomerCard(@PathVariable("cardID") Integer cardID,
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws ApniDukaanException {
		logger.info("Recieved request to delete  card :" + cardID + " of customer : " + customerEmailId);

		paymentService.deleteCustomerCard(customerEmailId, cardID);
		String modificationSuccessMsg = environment.getProperty("PaymentAPI.CUSTOMER_CARD_DELETED_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	
	// Get the customer cards details by calling getCustomerCardOfCardType()
    // method of PaymentService() and return the list of card details obtained
	
	@GetMapping(value = "/customer/{customerEmailId}/card-type/{cardType}")
	public ResponseEntity<List<CardDTO>> getCardsOfCustomer(@PathVariable String customerEmailId,
			@PathVariable String cardType) throws ApniDukaanException {
		logger.info("Recieved request to fetch  cards of customer : " + customerEmailId + " having card type as: "
				+ cardType);

		// write your logic here
		return null;
		
	}

	
	// Get the order details of Customer for the given orderId by calling respective
    // API
    // Update the Transaction details with the obtained Order details in above step,
    // along with transaction date and total price
    // Authenticate the transaction details for the given customer by calling
    // authenticatePayment() method of PaymentService
    // Add the transaction details to the database by calling addTransaction()
    // method of PaymentService
    // Update the order status by calling by calling respective API
    // Set the appropriate success message and return the same
	
	@PostMapping(value = "/customer/{customerEmailId}/order/{orderId}")
	public ResponseEntity<String> payForOrder(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId,
			@NotNull(message = "{orderId.absent") @PathVariable("orderId") Integer orderId,
			@Valid @RequestBody CardDTO cardDTO) throws NoSuchAlgorithmException, ApniDukaanException {
		
		// write your logic here
		return null;

	}

}