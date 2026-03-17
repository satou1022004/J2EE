package com.example.bai5_jpa.repository;

import com.example.bai5_jpa.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}