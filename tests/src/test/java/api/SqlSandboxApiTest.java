package api;

import core.BaseTest;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class SqlSandboxApiTest extends BaseTest {

    @Test
    @Description("SQL sandbox returns the products table schema")
    public void testSchema() {
        when()
                .get("/api/sql/schema")
                .then()
                .statusCode(200)
                .body("table", equalTo("products"))
                .body("columns", hasItem("product_name"));
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
