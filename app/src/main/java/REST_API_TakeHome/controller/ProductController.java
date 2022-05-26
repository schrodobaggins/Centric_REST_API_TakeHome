package REST_API_TakeHome.controller;

import REST_API_TakeHome.entity.Product;
import REST_API_TakeHome.repository.ProductRepository;
//import REST_API_TakeHome.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/v1/products", produces = "application/json")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    /**
     * GET requests at /v1/products
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllProductsByCategory(
            @RequestParam(name = "category", defaultValue = "all") @Valid @Pattern(regexp = "\\D*") String category,
            @RequestParam(name = "page", defaultValue = "1") @Valid @Min(1) Integer pageNum,
            @RequestParam(name = "max", defaultValue = "10") @Valid @Min(0) Integer maxEntries) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Product> products;

            Pageable productPage = PageRequest.of(
                    pageNum - 1,
                    maxEntries,
                    Sort.by("createdAt").ascending()
            );
            Page<Product> p;
            if (category.equals("all")) {
                p = productRepository.findAll(productPage);
            } else {
                p = productRepository.findAllByCategory(category, productPage);
            }
            products = p.getContent();

            response.put("status", HttpStatus.OK);
            response.put("data", products);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("error", e.getClass().getName());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}