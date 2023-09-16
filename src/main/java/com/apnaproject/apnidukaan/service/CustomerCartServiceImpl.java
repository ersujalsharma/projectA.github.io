package com.apnaproject.apnidukaan.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.apnaproject.apnidukaan.dto.CartProductDTO;
import com.apnaproject.apnidukaan.dto.CustomerCartDTO;
import com.apnaproject.apnidukaan.entity.CartProduct;
import com.apnaproject.apnidukaan.entity.CustomerCart;
import com.apnaproject.apnidukaan.exception.ApniDukaanException;
import com.apnaproject.apnidukaan.repository.CartProductRepository;
import com.apnaproject.apnidukaan.repository.CustomerCartRepository;


//add the missing annotations

public class CustomerCartServiceImpl implements CustomerCartService {

	private CustomerCartRepository customerCartRepository;

	private CartProductRepository cartProductRepository;

	@Override
	public Integer addProductToCart(CustomerCartDTO customerCartDTO) throws ApniDukaanException {
		Set<CartProduct> cartProducts = new HashSet<>();
		Integer cartId = null;
		for (CartProductDTO cartProductDTO : customerCartDTO.getCartProducts()) {
			CartProduct cartProduct = new CartProduct();
			cartProduct.setProductId(cartProductDTO.getProduct().getProductId());
			cartProduct.setQuantity(cartProductDTO.getQuantity());
			cartProducts.add(cartProduct);
		}
		Optional<CustomerCart> cartOptional = customerCartRepository
				.findByCustomerEmailId(customerCartDTO.getCustomerEmailId());
		if (cartOptional.isEmpty()) {
			CustomerCart newCart = new CustomerCart();
			newCart.setCustomerEmailId(customerCartDTO.getCustomerEmailId());
			newCart.setCartProducts(cartProducts);
			customerCartRepository.save(newCart);
			cartId = newCart.getCartId();
		} else {
			CustomerCart cart = cartOptional.get();
			for (CartProduct cartProductToBeAdded : cartProducts) {
				Boolean found = false;
				for (CartProduct cartProductFromCart : cart.getCartProducts()) {
					if (cartProductFromCart.equals(cartProductToBeAdded)) {

						cartProductFromCart
								.setQuantity(cartProductToBeAdded.getQuantity() + cartProductFromCart.getQuantity());
						found = true;
					}
				}
				if (found == false) {
					cart.getCartProducts().add(cartProductToBeAdded);
				}
			}

			cartId = cart.getCartId();
		}
		return cartId;
	}

	
	//Get the customer cart details by using customerEmailId
	//If no cart found then throw an EKartException with message "CustomerCartService.NO_CART_FOUND"
	//Else if cart is empty then throw an EKartException with message "CustomerCartService.NO_PRODUCT_ADDED_TO_CART"
	//Otherwise return the set of cart products
	
	@Override
	public Set<CartProductDTO> getProductsFromCart(String customerEmailId) throws ApniDukaanException {
		
		// write your logic here
		return null;
		
	}

	
	//Get the customer cart details by using customerEmailId
	//If no cart found then throw an EKartException with message "CustomerCartService.NO_CART_FOUND"
	//Else if cart is empty then throw an EKartException with message "CustomerCartService.NO_PRODUCT_ADDED_TO_CART"
	//Otherwise delete the given product from the customer cart
	
	@Override
	public void deleteProductFromCart(String customerEmailId, Integer productId) throws ApniDukaanException {

		// write your logic here
		
	}

	@Override
	public void deleteAllProductsFromCart(String customerEmailId) throws ApniDukaanException {
		Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
		CustomerCart cart = cartOptional.orElseThrow(() -> new ApniDukaanException("CustomerCartService.NO_CART_FOUND"));

		if (cart.getCartProducts().isEmpty()) {
			throw new ApniDukaanException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
		}
		List<Integer> productIds = new ArrayList<>();
		cart.getCartProducts().parallelStream().forEach(cp -> {
			productIds.add(cp.getCartProductId());
			cart.getCartProducts().remove(cp);
		});

		productIds.forEach(pid -> {

			cartProductRepository.deleteById(pid);
		});

	}

	@Override
	public void modifyQuantityOfProductInCart(String customerEmailId, Integer productId, Integer quantity)
			throws ApniDukaanException {

		Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
		CustomerCart cart = cartOptional.orElseThrow(() -> new ApniDukaanException("CustomerCartService.NO_CART_FOUND"));

		if (cart.getCartProducts().isEmpty()) {
			throw new ApniDukaanException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
		}
		CartProduct selectedProduct = null;
		for (CartProduct product : cart.getCartProducts()) {
			if (product.getProductId().equals(productId)) {
				selectedProduct = product;
			}
		}
		if (selectedProduct == null) {
			throw new ApniDukaanException("CustomerCartService.PRODUCT_ALREADY_NOT_AVAILABLE");
		}
		selectedProduct.setQuantity(quantity);
	}

}