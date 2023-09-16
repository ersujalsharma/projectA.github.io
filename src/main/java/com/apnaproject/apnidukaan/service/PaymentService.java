package com.apnaproject.apnidukaan.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.apnaproject.apnidukaan.dto.CardDTO;
import com.apnaproject.apnidukaan.dto.TransactionDTO;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;


public interface PaymentService {

	Integer addCustomerCard(String customerEmailId, CardDTO cardDTO) throws ApniDukaanException, NoSuchAlgorithmException;

	void updateCustomerCard(CardDTO cardDTO) throws ApniDukaanException, NoSuchAlgorithmException;

	void deleteCustomerCard(String customerEmailId, Integer cardId) throws ApniDukaanException;

	CardDTO getCard(Integer cardId) throws ApniDukaanException;

	List<CardDTO> getCustomerCardOfCardType(String customerEmailId, String cardType) throws ApniDukaanException;

	Integer addTransaction(TransactionDTO transactionDTO) throws ApniDukaanException;

	TransactionDTO authenticatePayment(String customerEmailId, TransactionDTO transactionDTO)
			throws ApniDukaanException, NoSuchAlgorithmException;

	List<CardDTO> getCardsOfCustomer(String customerEmailId, String cardType) throws ApniDukaanException;

}