package com.example.bai5_jpa.controller;

import com.example.bai5_jpa.model.Product;
import com.example.bai5_jpa.service.CategoryService;
import com.example.bai5_jpa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        return "product/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            product.setImage(imageFile.getBytes());
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Integer id) {
        Product product = productService.getProductById(id);
        byte[] image = product.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}