package com.apnaproject.apnidukaan.service;

import java.util.List;

import com.apnaproject.apnidukaan.dto.OrderDTO;
import com.apnaproject.apnidukaan.dto.OrderStatus;
import com.apnaproject.apnidukaan.dto.PaymentThrough;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;


public interface CustomerOrderService {
	Integer placeOrder(OrderDTO orderDTO) throws ApniDukaanException;

	OrderDTO getOrderDetails(Integer orderId) throws ApniDukaanException;

	List<OrderDTO> findOrdersByCustomerEmailId(String emailId) throws ApniDukaanException;

	void updateOrderStatus(Integer orderId, OrderStatus orderStatus) throws ApniDukaanException;

	void updatePaymentThrough(Integer orderId, PaymentThrough paymentThrough) throws ApniDukaanException;
}
