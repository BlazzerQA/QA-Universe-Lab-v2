package qa.universe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import qa.universe.models.ShopOrder;

public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
}
