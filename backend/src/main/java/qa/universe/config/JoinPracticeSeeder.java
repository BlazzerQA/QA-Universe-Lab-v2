package qa.universe.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import qa.universe.models.Customer;
import qa.universe.models.ShopOrder;
import qa.universe.repositories.CustomerRepository;
import qa.universe.repositories.ShopOrderRepository;

import java.math.BigDecimal;

/**
 * Demo data for JOIN practice: matched pairs, customers without orders, orders without a customer.
 */
@Component
@RequiredArgsConstructor
public class JoinPracticeSeeder {

    private final CustomerRepository customerRepository;
    private final ShopOrderRepository shopOrderRepository;

    @PostConstruct
    public void seedJoinPractice() {
        if (customerRepository.count() > 0 || shopOrderRepository.count() > 0) {
            return;
        }

        customerRepository.save(new Customer(1L, "Анна Смирнова", "Москва", "anna@shop.test"));
        customerRepository.save(new Customer(2L, "Борис Козлов", "Казань", "boris@shop.test"));
        customerRepository.save(new Customer(3L, "Вера Новикова", "Москва", "vera@shop.test"));
        customerRepository.save(new Customer(4L, "Глеб Орлов", "Сочи", "gleb@shop.test"));

        shopOrderRepository.save(new ShopOrder(101L, 1L, "paid", new BigDecimal("4590.00"), "2026-03-01"));
        shopOrderRepository.save(new ShopOrder(102L, 1L, "new", new BigDecimal("1290.00"), "2026-03-12"));
        shopOrderRepository.save(new ShopOrder(103L, 2L, "paid", new BigDecimal("74990.00"), "2026-03-05"));
        shopOrderRepository.save(new ShopOrder(104L, null, "cancelled", new BigDecimal("800.00"), "2026-03-08"));
        shopOrderRepository.save(new ShopOrder(105L, null, "paid", new BigDecimal("199.00"), "2026-03-15"));
    }
}
