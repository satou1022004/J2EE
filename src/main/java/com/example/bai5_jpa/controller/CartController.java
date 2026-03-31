package com.example.bai5_jpa.controller;

import com.example.bai5_jpa.model.Account;
import com.example.bai5_jpa.model.OrderEntity;
import com.example.bai5_jpa.model.Product;
import com.example.bai5_jpa.service.AccountService;
import com.example.bai5_jpa.service.CartService;
import com.example.bai5_jpa.service.OrderService;
import com.example.bai5_jpa.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong tim thay san pham.");
            return "redirect:/products";
        }

        cartService.addToCart(session, product, Math.max(quantity, 1));
        redirectAttributes.addFlashAttribute("successMessage", "Da them san pham vao gio hang.");
        return "redirect:/products";
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        model.addAttribute("cartItems", cartService.getCartItems(session));
        model.addAttribute("cartTotal", cartService.getCartTotal(session));
        return "cart/view";
    }

    @PostMapping("/update/{productId}")
    public String updateCart(@PathVariable Long productId,
                             @RequestParam int quantity,
                             HttpSession session) {
        cartService.updateQuantity(session, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        cartService.removeFromCart(session, productId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Authentication authentication,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        try {
            Account account = accountService.findByLoginName(authentication.getName());
            OrderEntity order = orderService.checkout(account, cartService.getCartItems(session));
            cartService.clearCart(session);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Dat hang thanh cong. Ma don hang: " + order.getId() + ". Tong tien: " + order.getTotalAmount()
            );
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/cart";
    }
}
