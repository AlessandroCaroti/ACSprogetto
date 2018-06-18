import email.EmailHandler;

import java.io.FileInputStream;
import java.util.Properties;

public class email_tester {

    public static void main(String[] args) {
    try{
        FileInputStream in=new FileInputStream("localsettings");
        Properties properties=new Properties();
        properties.load(in);
        EmailHandler emailHandler=new EmailHandler(properties,100);
        emailHandler.addMessage(emailHandler.createEmailMessage("ACSgroup.unige@gmail.com","ALUA","ciaozi"));
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }

}

