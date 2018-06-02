package customException;

public class AccountMonitorRuntimeException extends Exception {
    public AccountMonitorRuntimeException(){}
    public AccountMonitorRuntimeException(String message){
        super(message);
    }
}