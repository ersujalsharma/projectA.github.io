package com.apnaproject.apnidukaan.repository;

import org.springframework.data.repository.CrudRepository;

import com.apnaproject.apnidukaan.entity.Transaction;


public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}