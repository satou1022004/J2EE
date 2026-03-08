package com.example.BT4.controller;

import com.example.BT4.model.Category;
import com.example.BT4.model.Product;
import com.example.BT4.service.CategoryService;
import com.example.BT4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Danh sách sản phẩm
    @GetMapping
    public String Index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }

    // Form tạo mới
    @GetMapping("/create")
    public String Create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    // Xử lý tạo mới
    @PostMapping("/create")
    public String Create(@Valid Product newProduct,
                         BindingResult result,
                         @RequestParam("category.id") int categoryId,
                         @RequestParam("imageProduct") MultipartFile imageProduct,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        // Xử lý ảnh
        if (!imageProduct.isEmpty()) {
            productService.updateImage(newProduct, imageProduct);
        }

        Category selectedCategory = categoryService.get(categoryId);
        newProduct.setCategory(selectedCategory);
        productService.add(newProduct);

        return "redirect:/products";
    }

    // Form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String Edit(@PathVariable int id, Model model) {
        Product find = productService.get(id);
        if (find == null) {
            return "error/404";
        }
        model.addAttribute("product", find);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }

    // Xử lý chỉnh sửa
    @PostMapping("/edit")
    public String Edit(@Valid Product editProduct,
                       BindingResult result,
                       @RequestParam("imageProduct") MultipartFile imageProduct,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        // Cập nhật ảnh nếu có
        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(editProduct, imageProduct);
        }

        productService.update(editProduct);
        return "redirect:/products";
    }

    // Xóa sản phẩm
    @PostMapping("/delete/{id}")
    public String Delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}