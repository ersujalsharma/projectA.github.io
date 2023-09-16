package com.apnaproject.apnidukaan.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.apnaproject.apnidukaan.dto.CardDTO;
import com.apnaproject.apnidukaan.dto.TransactionDTO;
import com.apnaproject.apnidukaan.dto.TransactionStatus;
import com.apnaproject.apnidukaan.entity.Card;
import com.apnaproject.apnidukaan.entity.Transaction;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;
import com.apnaproject.apnidukaan.repository.CardRepository;
import com.apnaproject.apnidukaan.repository.TransactionRepository;
import com.apnaproject.apnidukaan.utility.HashingUtility;

//add the missing annotations

public class PaymentServiceImpl implements PaymentService {

	private CardRepository cardRepository;

	private TransactionRepository transactionRepository;

	@Override
	public Integer addCustomerCard(String customerEmailId, CardDTO cardDTO)
			throws ApniDukaanException, NoSuchAlgorithmException {

//		List<Card> listOfCustomerCards = cardRepository.findByCustomerEmailId(customerEmailId);
//		if (listOfCustomerCards.isEmpty())
//			throw new EKartException("PaymentService.CUSTOMER_NOT_FOUND");
		cardDTO.setHashCvv(HashingUtility.getHashValue(cardDTO.getCvv().toString()));
		Card newCard = new Card();
		newCard.setCardId(cardDTO.getCardId());
		newCard.setNameOnCard(cardDTO.getNameOnCard());
		;
		newCard.setCardNumber(cardDTO.getCardNumber());
		newCard.setCardType(cardDTO.getCardType());
		newCard.setExpiryDate(cardDTO.getExpiryDate());
		newCard.setCvv(cardDTO.getHashCvv());
		newCard.setCustomerEmailId(cardDTO.getCustomerEmailId());

		cardRepository.save(newCard);
		return newCard.getCardID();
	}

	@Override
	public void updateCustomerCard(CardDTO cardDTO) throws ApniDukaanException, NoSuchAlgorithmException {

		Optional<Card> optionalCard = cardRepository.findById(cardDTO.getCardId());
		Card card = optionalCard.orElseThrow(() -> new ApniDukaanException("PaymentService.CARD_NOT_FOUND"));
		cardDTO.setHashCvv(HashingUtility.getHashValue(cardDTO.getCvv().toString()));
		card.setCardId(cardDTO.getCardId());
		card.setNameOnCard(cardDTO.getNameOnCard());
		card.setCardNumber(cardDTO.getCardNumber());
		card.setCardType(cardDTO.getCardType());
		card.setCvv(cardDTO.getHashCvv());
		card.setExpiryDate(cardDTO.getExpiryDate());
		card.setCustomerEmailId(cardDTO.getCustomerEmailId());

	}

	@Override
	public void deleteCustomerCard(String customerEmailId, Integer cardId) throws ApniDukaanException {

		List<Card> listOfCustomerCards = cardRepository.findByCustomerEmailId(customerEmailId);
		if (listOfCustomerCards.isEmpty())
			throw new ApniDukaanException("PaymentService.CUSTOMER_NOT_FOUND");

		Optional<Card> optionalCards = cardRepository.findById(cardId);
		Card card = optionalCards.orElseThrow(() -> new ApniDukaanException("PaymentService.CARD_NOT_FOUND"));
		cardRepository.delete(card);

	}

	@Override
	public CardDTO getCard(Integer cardId) throws ApniDukaanException {

		Optional<Card> optionalCards = cardRepository.findById(cardId);
		Card card = optionalCards.orElseThrow(() -> new ApniDukaanException("PaymentService.CARD_NOT_FOUND"));
		CardDTO cardDTO = new CardDTO();
		cardDTO.setCardId(card.getCardID());
		cardDTO.setNameOnCard(card.getNameOnCard());
		cardDTO.setCardNumber(card.getCardNumber());
		cardDTO.setCardType(card.getCardType());
		cardDTO.setHashCvv(card.getCvv());
		cardDTO.setExpiryDate(card.getExpiryDate());
		cardDTO.setCustomerEmailId(card.getCustomerEmailId());
		return cardDTO;
	}

	@Override
	public List<CardDTO> getCustomerCardOfCardType(String customerEmailId, String cardType) throws ApniDukaanException {

		List<Card> cards = cardRepository.findByCustomerEmailIdAndCardType(customerEmailId, cardType);

		if (cards.isEmpty()) {
			throw new ApniDukaanException("PaymentService.CARD_NOT_FOUND");
		}
		List<CardDTO> cardDTOs = new ArrayList<CardDTO>();
		for (Card card : cards) {
			CardDTO cardDTO = new CardDTO();
			cardDTO.setCardId(card.getCardID());
			cardDTO.setNameOnCard(card.getNameOnCard());
			cardDTO.setCardNumber(card.getCardNumber());
			cardDTO.setCardType(card.getCardType());
			cardDTO.setHashCvv("XXX");
			cardDTO.setExpiryDate(card.getExpiryDate());
			cardDTO.setCustomerEmailId(card.getCustomerEmailId());

			cardDTOs.add(cardDTO);
		}
		return cardDTOs;
	}

	
	// Get the list of card details by using the customerEmailId and cardType
    // If the obtained list is empty throw EKartException with message
    // PaymentService.CARD_NOT_FOUND
    // Else populate the obtained card details and return
	
	@Override
	public List<CardDTO> getCardsOfCustomer(String customerEmailId, String cardType) throws ApniDukaanException {

		// write your logic here
		return null;
		
	}

	@Override
	public Integer addTransaction(TransactionDTO transactionDTO) throws ApniDukaanException {
		if (transactionDTO.getTransactionStatus().equals(TransactionStatus.TRANSACTION_FAILED)) {
			throw new ApniDukaanException("PaymentService.TRANSACTION_FAILED_CVV_NOT_MATCHING");
		}
		Transaction transaction = new Transaction();
		transaction.setCardId(transactionDTO.getCard().getCardId());

		transaction.setOrderId(transactionDTO.getOrder().getOrderId());
		transaction.setTotalPrice(transactionDTO.getTotalPrice());
		transaction.setTransactionDate(transactionDTO.getTransactionDate());
		transaction.setTransactionStatus(transactionDTO.getTransactionStatus());
		transactionRepository.save(transaction);

		return transaction.getTransactionId();
	}

	@Override
	public TransactionDTO authenticatePayment(String customerEmailId, TransactionDTO transactionDTO)
			throws ApniDukaanException, NoSuchAlgorithmException {
		if (!transactionDTO.getOrder().getCustomerEmailId().equals(customerEmailId)) {
			throw new ApniDukaanException("PaymentService.ORDER_DOES_NOT_BELONGS");

		}

		if (!transactionDTO.getOrder().getOrderStatus().equals("PLACED")) {
			throw new ApniDukaanException("PaymentService.TRANSACTION_ALREADY_DONE");

		}
		CardDTO cardDTO = getCard(transactionDTO.getCard().getCardId());
		if (!cardDTO.getCustomerEmailId().matches(customerEmailId)) {

			throw new ApniDukaanException("PaymentService.CARD_DOES_NOT_BELONGS");
		}
		if (!cardDTO.getCardType().equals(transactionDTO.getOrder().getPaymentThrough())) {

			throw new ApniDukaanException("PaymentService.PAYMENT_OPTION_SELECTED_NOT_MATCHING_CARD_TYPE");
		}
		if (cardDTO.getHashCvv().equals(HashingUtility.getHashValue(transactionDTO.getCard().getCvv().toString()))) {

			transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
		} else {

			transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_FAILED);

		}

		return transactionDTO;
	}
}
