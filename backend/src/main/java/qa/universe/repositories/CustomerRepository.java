package qa.universe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import qa.universe.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
