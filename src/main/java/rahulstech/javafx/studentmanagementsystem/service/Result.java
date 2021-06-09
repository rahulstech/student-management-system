package rahulstech.javafx.studentmanagementsystem.service;

public class Result {

    private int operationCode;
    private Object value;
    private boolean successful;
    private int errorCode;

    public Result(int operationCode) {
        this.operationCode = operationCode;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public Object getValue() {
        return value;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Result setOperationCode(int operationCode) {
        this.operationCode = operationCode;
        return this;
    }

    public Result setValue(Object value) {
        this.value = value;
        return this;
    }

    public Result setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public Result setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }
}
