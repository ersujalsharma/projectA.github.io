package com.apnaproject.apnidukaan.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apnaproject.apnidukaan.dto.CustomerDTO;
import com.apnaproject.apnidukaan.dto.OrderDTO;
import com.apnaproject.apnidukaan.dto.OrderStatus;
import com.apnaproject.apnidukaan.dto.OrderedProductDTO;
import com.apnaproject.apnidukaan.dto.PaymentThrough;
import com.apnaproject.apnidukaan.dto.ProductDTO;
import com.apnaproject.apnidukaan.entity.Order;
import com.apnaproject.apnidukaan.entity.OrderedProduct;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;
import com.apnaproject.apnidukaan.repository.OrderRepository;
@Service
@Transactional
public class CustomerOrderServiceImpl implements CustomerOrderService{

	@Autowired
	private CustomerService customerService;
	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	public Integer placeOrder(OrderDTO orderDTO) throws ApniDukaanException {
		CustomerDTO customerDTO = customerService.getCustomerByEmailId(orderDTO.getCustomerEmailId());
		if(customerDTO.getAddress() == null || customerDTO.getAddress().isBlank()) {
			throw new ApniDukaanException("OrderService.ADDRESS_NOT_AVAILABLE");
		}
		Order order = new Order();
		order.setDeliveryAddress(customerDTO.getAddress());
		order.setCustomerEmailId(orderDTO.getCustomerEmailId());
		order.setDateOfDelivery(orderDTO.getDateOfDelivery());
		order.setDateOfOrder(LocalDateTime.now());
		order.setPaymentThrough(PaymentThrough.valueOf(orderDTO.getPaymentThrough()));
		if (order.getPaymentThrough().equals(PaymentThrough.CREDIT_CARD)) {
			order.setDiscount(10.00d);
		} else {
			order.setDiscount(5.00d);
		}
		order.setOrderStatus(OrderStatus.PLACED);
		Double price = 0.0;
		List<OrderedProduct> orderedProducts = new ArrayList<OrderedProduct>();

		for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {
			if (orderedProductDTO.getProduct().getAvailableQuantity() < orderedProductDTO.getQuantity()) {
				throw new ApniDukaanException("OrderService.INSUFFICIENT_STOCK");
			}

			OrderedProduct orderedProduct = new OrderedProduct();
			orderedProduct.setProductId(orderedProductDTO.getProduct().getProductId());
			orderedProduct.setQuantity(orderedProductDTO.getQuantity());
			orderedProducts.add(orderedProduct);
			price = price + orderedProductDTO.getQuantity() * orderedProductDTO.getProduct().getPrice();

		}
		order.setOrderedProducts(orderedProducts);

		order.setTotalPrice(price * (100 - order.getDiscount()) / 100);

		orderRepository.save(order);

		return order.getOrderId();
	}

	@Override
	public OrderDTO getOrderDetails(Integer orderId) throws ApniDukaanException {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		Order order = optionalOrder.orElseThrow(() -> new ApniDukaanException("OrderService.ORDER_NOT_FOUND"));

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(orderId);
		orderDTO.setCustomerEmailId(order.getCustomerEmailId());
		orderDTO.setDateOfDelivery(order.getDateOfDelivery());
		orderDTO.setDateOfOrder(order.getDateOfOrder());
		orderDTO.setPaymentThrough(order.getPaymentThrough().toString());
		orderDTO.setTotalPrice(order.getTotalPrice());
		orderDTO.setOrderStatus(order.getOrderStatus().toString());
		orderDTO.setDiscount(order.getDiscount());
		List<OrderedProductDTO> orderedProductDTOs = new ArrayList<OrderedProductDTO>();
		for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
			OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
			ProductDTO productDTO = new ProductDTO();
			productDTO.setProductId(orderedProduct.getProductId());
			orderedProductDTO.setOrderedProductId(orderedProduct.getOrderedProductId());
			orderedProductDTO.setQuantity(orderedProduct.getQuantity());
			orderedProductDTO.setProduct(productDTO);
			orderedProductDTOs.add(orderedProductDTO);
		}
		orderDTO.setOrderedProducts(orderedProductDTOs);
		return orderDTO;
	}

	@Override
	public List<OrderDTO> findOrdersByCustomerEmailId(String emailId) throws ApniDukaanException {
		List<Order> orders = orderRepository.findByCustomerEmailId(emailId);
		if (orders.isEmpty()) {
			throw new ApniDukaanException("OrderService.NO_ORDERS_FOUND");
		}
		List<OrderDTO> orderDTOs = new ArrayList<>();
		for (Order order : orders) {
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setOrderId(order.getOrderId());
			orderDTO.setCustomerEmailId(order.getCustomerEmailId());
			orderDTO.setDateOfDelivery(order.getDateOfDelivery());
			orderDTO.setDateOfOrder(order.getDateOfOrder());
			orderDTO.setPaymentThrough(order.getPaymentThrough().toString());
			orderDTO.setTotalPrice(order.getTotalPrice());
			orderDTO.setOrderStatus(order.getOrderStatus().toString());
			orderDTO.setDiscount(order.getDiscount());
			List<OrderedProductDTO> orderedProductDTOs = new ArrayList<OrderedProductDTO>();
			for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
				OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
				ProductDTO productDTO = new ProductDTO();
				productDTO.setProductId(orderedProduct.getProductId());
				orderedProductDTO.setOrderedProductId(orderedProduct.getOrderedProductId());
				orderedProductDTO.setQuantity(orderedProduct.getQuantity());
				orderedProductDTO.setProduct(productDTO);
				orderedProductDTOs.add(orderedProductDTO);
			}
			orderDTO.setOrderedProducts(orderedProductDTOs);
			orderDTO.setDeliveryAddress(order.getDeliveryAddress());
			orderDTOs.add(orderDTO);
		}
		return orderDTOs;
	}

	@Override
	public void updateOrderStatus(Integer orderId, OrderStatus orderStatus) throws ApniDukaanException {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		Order order = optionalOrder.orElseThrow(() -> new ApniDukaanException("OrderService.ORDER_NOT_FOUND"));
		order.setOrderStatus(orderStatus);
	}

	@Override
	public void updatePaymentThrough(Integer orderId, PaymentThrough paymentThrough) throws ApniDukaanException {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		Order order = optionalOrder.orElseThrow(() -> new ApniDukaanException("OrderService.ORDER_NOT_FOUND"));
		if (order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
			throw new ApniDukaanException("OrderService.TRANSACTION_ALREADY_DONE");
		}
		order.setPaymentThrough(paymentThrough);
	}

}
