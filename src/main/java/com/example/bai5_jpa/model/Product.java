package com.example.bai5_jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ten san pham khong duoc de trong")
    @Column(nullable = false, length = 255)
    private String name;

    @Min(value = 1, message = "Gia san pham khong duoc nho hon 1")
    @Max(value = 9999999, message = "Gia san pham khong duoc lon hon 9999999")
    @Column(nullable = false)
    private long price;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @NotNull(message = "Vui long chon category")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
