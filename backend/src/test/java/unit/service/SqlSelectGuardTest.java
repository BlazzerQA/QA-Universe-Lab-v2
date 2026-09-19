package unit.service;

import org.junit.jupiter.api.Test;
import qa.universe.exception.SqlSandboxException;
import qa.universe.service.SqlSelectGuard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlSelectGuardTest {

    @Test
    void allowsSelectFromProducts() {
        String sql = SqlSelectGuard.validate("SELECT * FROM products;");
        assertEquals("SELECT * FROM products", sql);
    }

    @Test
    void allowsSelectWithWhereAndOrder() {
        String sql = SqlSelectGuard.validate(
                "select product_name, price from products where price > 100 order by price desc"
        );
        assertEquals("select product_name, price from products where price > 100 order by price desc", sql);
    }

    @Test
    void rejectsBlank() {
        assertThrows(SqlSandboxException.class, () -> SqlSelectGuard.validate("  "));
    }

    @Test
    void rejectsNonSelect() {
        assertThrows(SqlSandboxException.class, () -> SqlSelectGuard.validate("INSERT INTO products VALUES (1)"));
    }

    @Test
    void rejectsOtherTables() {
        assertThrows(SqlSandboxException.class, () -> SqlSelectGuard.validate("SELECT * FROM users"));
    }

    @Test
    void rejectsMultipleStatements() {
        assertThrows(SqlSandboxException.class, () -> SqlSelectGuard.validate("SELECT * FROM products; SELECT 1"));
    }
}
