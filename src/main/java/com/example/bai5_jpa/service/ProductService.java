package com.example.bai5_jpa.service;

import com.example.bai5_jpa.model.Product;
import com.example.bai5_jpa.repository.CategoryRepository;
import com.example.bai5_jpa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getProducts(String keyword, Integer categoryId, String sort, int page, int size) {
        Specification<Product> specification = Specification.where(null);

        if (StringUtils.hasText(keyword)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + keyword.trim().toLowerCase() + "%"
                    )
            );
        }

        if (categoryId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("category").get("id"), categoryId)
            );
        }

        Pageable pageable = PageRequest.of(page, size, resolveSort(sort));
        return productRepository.findAll(specification, pageable);
    }

    public void saveProduct(Product product) {
        if (product.getId() != null && product.getImage() == null) {
            Product existingProduct = getProductById(product.getId());
            if (existingProduct != null) {
                product.setImage(existingProduct.getImage());
            }
        }

        if (product.getCategory() != null && product.getCategory().getId() != null) {
            categoryRepository.findById(product.getCategory().getId())
                    .ifPresent(product::setCategory);
        }

        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private Sort resolveSort(String sort) {
        if ("priceAsc".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.ASC, "price");
        }
        if ("priceDesc".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.DESC, "price");
        }
        return Sort.by(Sort.Direction.ASC, "id");
    }
}
