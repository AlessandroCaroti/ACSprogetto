/*
 This file is part of ACSprogetto.

 ACSprogetto is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 ACSprogetto is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with ACSprogetto.  If not, see <http://www.gnu.org/licenses/>.

 */
package email;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

import static java.util.Objects.requireNonNull;

//TODO add method which use put method of array blocking queue

public class EmailHandler {

    private String username;
    private String password;
    private Session session;
    private Properties props=new Properties();
    private BlockingQueue<Message> messagesList;
    private ExecutorService emailHandlerThread=Executors.newSingleThreadScheduledExecutor();

    private EmailHandler(String myEmail,String myPassword){
        this.username=requireNonNull(myEmail);
        this.password=requireNonNull(myPassword);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");//TODO non è detto che sia gmail generalizzare
        props.put("mail.smtp.port", "587");
        this.session=Session.getInstance(
                                            props,
                                            new javax.mail.Authenticator(){
                                                protected PasswordAuthentication getPasswordAuthentication() {
                                                    return new PasswordAuthentication(username, password);
                                                }
                                            }
        );
    }

    /**
     * Usa la mail salvata nel file di configurazione del server
     */
    public EmailHandler(Properties serverProperties,int handlerMaxCapacity) {
        this(serverProperties.getProperty("serveremail"),serverProperties.getProperty("emailpassword"));
        if(handlerMaxCapacity<=0){throw  new IllegalArgumentException("Error: emailhandlerCapacity <=0");}
        this.messagesList=new ArrayBlockingQueue<>(handlerMaxCapacity);
    }

    public EmailHandler(String myEmail,String myPassword,int handlerMaxCapacity){
        this(myEmail,myPassword);
        if(handlerMaxCapacity<=0){throw  new IllegalArgumentException("Error: emailhandlerCapacity <=0");}
        this.messagesList=new ArrayBlockingQueue<>(handlerMaxCapacity);
    }


    public void startEmailHandlerManager(){
        emailHandlerThread.submit(new EmailThread(this));
    }


    public  Message createEmailMessage(String to,String subject,String bodyText) throws MessagingException {
        Message message=new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(bodyText);
        return message;
    }



    /**
     * Inserisce il messaggio nella coda di quelli da inviare
     * @param message il messaggio da inviare (non null)
     * @throws IllegalStateException se è stata raggiunta la capacità massima della coda
     * @throws NullPointerException se viene passato un ref. messaggio null
     */
    public  void addMessage(Message message)throws IllegalStateException,NullPointerException {

        if (message == null) {
            throw new NullPointerException("Error:message == null");
        }
        this.messagesList.add(message);
        synchronized (messagesList) {
            System.out.println("NOTIF");
            this.messagesList.notify();
        }
        System.out.println("NOTIFexit");
    }








    /*runnable del Thread che gestisce la coda*/
    private class EmailThread implements Runnable{
            private EmailHandler emailHandlerClass;

            private EmailThread(EmailHandler handlerClass){
                this.emailHandlerClass=requireNonNull(handlerClass);
            }

            public  void run(){/*TODO not sure on this part (Interrupted exception )*/
                Message toBeSent;
                while(true){
                    try {
                            while((toBeSent=emailHandlerClass.messagesList.poll())==null) {
                                System.out.println("WAITING");
                                synchronized(emailHandlerClass.messagesList) {
                                    emailHandlerClass.messagesList.wait();
                                }
                            }
                            System.out.println("exit from WAITING");
                            Transport.send(toBeSent);
                            System.out.println("SENT");
                        }
                        catch (InterruptedException |MessagingException e) {
                            if(e instanceof InterruptedException) {
                                return;
                            }
                            System.err.println("ERROR:unable to send email");
                        }
                    }
                }
    }

}
