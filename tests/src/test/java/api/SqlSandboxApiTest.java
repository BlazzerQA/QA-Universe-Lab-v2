package api;

import core.BaseTest;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

public class SqlSandboxApiTest extends BaseTest {

    @Test
    @Description("SQL sandbox returns the products table schema")
    public void testSchema() {
        when()
                .get("/api/sql/schema")
                .then()
                .statusCode(200)
                .body("table", equalTo("products"))
                .body("columns", hasItem("product_name"))
                .body("tables.table", hasItems("products", "customers", "orders"));
    }

    @Test
    @Description("SQL sandbox executes LEFT JOIN on customers and orders")
    public void testLeftJoinCustomersOrders() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("sql",
                        "SELECT c.full_name, o.order_id FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id"))
                .when()
                .post("/api/sql/query")
                .then()
                .statusCode(200)
                .body("columns", hasItem("full_name"))
                .body("rowCount", greaterThanOrEqualTo(4));
    }

    @Test
    @Description("SQL sandbox executes SELECT against shop products")
    public void testSelectProducts() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("sql", "SELECT product_name, price FROM products"))
                .when()
                .post("/api/sql/query")
                .then()
                .statusCode(200)
                .body("columns", hasItem("product_name"))
                .body("rowCount", greaterThanOrEqualTo(0));
    }

    @Test
    @Description("SQL sandbox rejects non-SELECT statements")
    public void testRejectsWrite() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("sql", "DELETE FROM products"))
                .when()
                .post("/api/sql/query")
                .then()
                .statusCode(400);
    }
}
