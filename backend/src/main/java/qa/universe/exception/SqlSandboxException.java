package qa.universe.exception;

public class SqlSandboxException extends RuntimeException {

    public SqlSandboxException(String message) {
        super(message);
    }

    public SqlSandboxException(String message, Throwable cause) {
        super(message, cause);
    }
}
