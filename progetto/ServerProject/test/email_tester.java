import email.EmailHandlerTLS;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class email_tester {

    public static void main(String[] args) {
    try{
        BufferedReader buff=new BufferedReader(new InputStreamReader(System.in));
        String password;
        System.out.println("Inserisci password:");
        password=buff.readLine();
        EmailHandlerTLS emailHandler=new EmailHandlerTLS("acsgroup.unige@gmail.com",password,100,587,"smtp.gmail.com");
        emailHandler.startEmailHandlerManager();
        emailHandler.sendMessage(emailHandler.createEmailMessage("ACSgroup.unige@gmail.com","ALUA","ciaozi"));
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }

}

