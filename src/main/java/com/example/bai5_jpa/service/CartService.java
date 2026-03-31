package com.example.bai5_jpa.service;

import com.example.bai5_jpa.model.CartItem;
import com.example.bai5_jpa.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    public void addToCart(HttpSession session, Product product, int quantity) {
        Map<Long, CartItem> cart = getCart(session);
        CartItem item = cart.get(product.getId());

        if (item == null) {
            item = new CartItem(product.getId(), product.getName(), product.getPrice(), 0);
            cart.put(product.getId(), item);
        }

        item.setQuantity(item.getQuantity() + quantity);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        Map<Long, CartItem> cart = getCart(session);
        if (!cart.containsKey(productId)) {
            return;
        }

        if (quantity <= 0) {
            cart.remove(productId);
        } else {
            cart.get(productId).setQuantity(quantity);
        }

        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(productId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public Collection<CartItem> getCartItems(HttpSession session) {
        return getCart(session).values();
    }

    public int getCartItemCount(HttpSession session) {
        return getCart(session).values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public long getCartTotal(HttpSession session) {
        return getCart(session).values().stream()
                .mapToLong(CartItem::getLineTotal)
                .sum();
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCart(HttpSession session) {
        Object cartObject = session.getAttribute(CART_SESSION_KEY);
        if (cartObject instanceof Map<?, ?> cart) {
            return (Map<Long, CartItem>) cart;
        }

        Map<Long, CartItem> newCart = new LinkedHashMap<>();
        session.setAttribute(CART_SESSION_KEY, newCart);
        return newCart;
    }
}
