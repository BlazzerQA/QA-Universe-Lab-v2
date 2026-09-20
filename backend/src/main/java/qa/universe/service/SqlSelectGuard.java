package qa.universe.service;

import qa.universe.exception.SqlSandboxException;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows a single SELECT against shop and JOIN-practice tables.
 * Blocks writes, DDL, and queries to other tables (including {@code users}).
 */
public final class SqlSelectGuard {

    static final Set<String> ALLOWED_TABLES = Set.of("products", "customers", "orders");
    private static final String ALLOWED_TABLES_HINT =
            "Можно читать только таблицы products, customers, orders.";

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

        String trimmed = stripComments(sql).trim();
        if (trimmed.endsWith(";")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
        }
        if (trimmed.contains(";")) {
            throw new SqlSandboxException("Можно выполнить только один SELECT.");
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
                throw new SqlSandboxException(ALLOWED_TABLES_HINT);
            }
        }
        if (!foundTable) {
            throw new SqlSandboxException("Укажите таблицу: FROM products, customers или orders.");
        }
        return trimmed;
    }

    /** Drops {@code --} and {@code /* *}{@code /} comments so students can annotate queries. */
    static String stripComments(String sql) {
        StringBuilder out = new StringBuilder(sql.length());
        int i = 0;
        char quote = 0;
        while (i < sql.length()) {
            char c = sql.charAt(i);
            char next = i + 1 < sql.length() ? sql.charAt(i + 1) : 0;
            if (quote != 0) {
                out.append(c);
                if (c == quote) {
                    if (next == quote) {
                        out.append(next);
                        i += 2;
                        continue;
                    }
                    quote = 0;
                }
                i++;
                continue;
            }
            if (c == '\'' || c == '"') {
                quote = c;
                out.append(c);
                i++;
                continue;
            }
            if (c == '-' && next == '-') {
                while (i < sql.length() && sql.charAt(i) != '\n') {
                    i++;
                }
                out.append(' ');
                continue;
            }
            if (c == '/' && next == '*') {
                i += 2;
                while (i + 1 < sql.length() && !(sql.charAt(i) == '*' && sql.charAt(i + 1) == '/')) {
                    i++;
                }
                i = Math.min(i + 2, sql.length());
                out.append(' ');
                continue;
            }
            out.append(c);
            i++;
        }
        return out.toString();
    }
}
