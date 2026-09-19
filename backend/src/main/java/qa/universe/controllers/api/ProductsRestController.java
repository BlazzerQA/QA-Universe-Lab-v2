package qa.universe.controllers.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qa.universe.models.Product;
import qa.universe.repositories.ProductRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ProductsRestController {

    private final ProductRepository productRepository;

    @GetMapping
    public Collection<Product> getProducts() {
        return productRepository.findAll(Sort.by("productName"));
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()));
        }

        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productRepository.save(product);
        return ResponseEntity.ok(Map.of(
                "product", product.getProductName(),
                "price", product.getPrice(),
                "productId", product.getProductId()
        ));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        if (!productRepository.existsById(productId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found"));
        }
        productRepository.deleteById(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
