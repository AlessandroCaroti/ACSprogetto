package customException;

public class AccountRegistrationException extends Exception{
        public AccountRegistrationException(){}
        public AccountRegistrationException(String message){
            super(message);
        }
}