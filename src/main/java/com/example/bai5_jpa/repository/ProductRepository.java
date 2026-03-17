package com.example.bai5_jpa.repository;

import com.example.bai5_jpa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}