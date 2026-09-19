package qa.universe.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import qa.universe.models.Product;
import qa.universe.repositories.ProductRepository;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ProductSeeder {

    private final ProductRepository productRepository;

    @PostConstruct
    public void seedDemoProducts() {
        if (productRepository.count() > 0) {
            return;
        }
        productRepository.save(new Product("seed-notebook", "Ноутбук", new BigDecimal("74990.00")));
        productRepository.save(new Product("seed-mouse", "Мышь", new BigDecimal("1290.00")));
        productRepository.save(new Product("seed-coffee", "Кофе", new BigDecimal("349.00")));
    }
}
