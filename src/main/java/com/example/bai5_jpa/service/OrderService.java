package com.example.bai5_jpa.service;

import com.example.bai5_jpa.model.Account;
import com.example.bai5_jpa.model.CartItem;
import com.example.bai5_jpa.model.OrderDetail;
import com.example.bai5_jpa.model.OrderEntity;
import com.example.bai5_jpa.model.Product;
import com.example.bai5_jpa.repository.OrderDetailRepository;
import com.example.bai5_jpa.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public OrderEntity checkout(Account account, Collection<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        OrderEntity order = new OrderEntity();
        order.setAccount(account);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(cartItems.stream().mapToLong(CartItem::getLineTotal).sum());

        OrderEntity savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            Product product = productService.getProductById(cartItem.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product not found: " + cartItem.getProductId());
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getPrice());
            orderDetail.setLineTotal(cartItem.getLineTotal());
            orderDetailRepository.save(orderDetail);
        }

        return savedOrder;
    }
}
