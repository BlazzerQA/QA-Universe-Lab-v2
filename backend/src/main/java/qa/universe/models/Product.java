package qa.universe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @Column(name = "product_id", nullable = false, length = 64)
    private String productId;

    @NotBlank(message = "Product name must not be blank")
    @Pattern(regexp = ".*[a-zA-Zа-яА-Я0-9]+.*", message = "Product name must contain at least one letter or digit")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be positive")
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    public Product() {}
    public Product(String productId, String productName, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
