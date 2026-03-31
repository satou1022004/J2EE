package com.example.bai5_jpa.repository;

import com.example.bai5_jpa.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
