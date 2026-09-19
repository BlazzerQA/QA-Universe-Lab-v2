package qa.universe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import qa.universe.models.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
}
