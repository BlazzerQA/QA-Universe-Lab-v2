package qa.universe.service;

import qa.universe.exception.SqlSandboxException;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows a single SELECT against the shop table {@code products}.
 * Blocks writes, DDL, and queries to other tables (including {@code users}).
 */
public final class SqlSelectGuard {

    static final Set<String> ALLOWED_TABLES = Set.of("products");

    private static final Pattern FORBIDDEN_KEYWORDS = Pattern.compile(
            "(?i)\\b(insert|update|delete|drop|alter|create|attach|detach|pragma|replace|vacuum|reindex|trigger|grant|revoke)\\b"
    );
    private static final Pattern TABLE_REF = Pattern.compile(
            "(?i)\\b(?:from|join)\\s+[\"'`\\[]?([a-zA-Z_][a-zA-Z0-9_]*)"
    );

    private SqlSelectGuard() {
    }

    public static String validate(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new SqlSandboxException("Введите SQL-запрос.");
        }

        String trimmed = sql.trim();
        if (trimmed.endsWith(";")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
        }
        if (trimmed.contains(";")) {
            throw new SqlSandboxException("Можно выполнить только один SELECT.");
        }
        if (trimmed.contains("--") || trimmed.contains("/*") || trimmed.contains("*/")) {
            throw new SqlSandboxException("Комментарии в запросе запрещены.");
        }
        if (!trimmed.matches("(?is)select\\b[\\s\\S]+")) {
            throw new SqlSandboxException("Разрешён только SELECT.");
        }
        if (FORBIDDEN_KEYWORDS.matcher(trimmed).find()) {
            throw new SqlSandboxException("Запрос содержит запрещённую команду.");
        }

        Matcher matcher = TABLE_REF.matcher(trimmed);
        boolean foundTable = false;
        while (matcher.find()) {
            foundTable = true;
            String table = matcher.group(1).toLowerCase();
            if (!ALLOWED_TABLES.contains(table)) {
                throw new SqlSandboxException("Можно читать только таблицу products.");
            }
        }
        if (!foundTable) {
            throw new SqlSandboxException("Укажите таблицу: FROM products.");
        }
        return trimmed;
    }
}
