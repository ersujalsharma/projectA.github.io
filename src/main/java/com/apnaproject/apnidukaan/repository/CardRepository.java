package com.apnaproject.apnidukaan.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.apnaproject.apnidukaan.entity.Card;


public interface CardRepository extends CrudRepository<Card, Integer> {

	List<Card> findByCustomerEmailId(String customerEmailId);

	List<Card> findByCustomerEmailIdAndCardType(String customerEmailId, String cardType);
}