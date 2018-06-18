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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.*;
import static java.util.Objects.requireNonNull;

//TODO add method which use put method of array blocking queue

public class EmailHandler {

    private final String username;
    private final String password;
    private final Session session;
    private final BlockingQueue<Message> messagesList;
    private ExecutorService emailHandlerThread=Executors.newSingleThreadScheduledExecutor();


    /* ********************************************************
        CONSTRUCTORS
     **********************************************************/

    public EmailHandler(String myEmail,String myPassword,int handlerMaxCapacity,int smtpPort,String smtpProvider) throws   IllegalArgumentException{
        this.username=requireNonNull(myEmail);
        this.password=requireNonNull(myPassword);
        Integer smtpPort1 = smtpPort;
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpProvider);
        props.put("mail.smtp.port", Integer.toString(smtpPort));
        this.session=Session.getInstance(
                props,
                                            new javax.mail.Authenticator(){
                                                protected PasswordAuthentication getPasswordAuthentication() {
                                                    return new PasswordAuthentication(username, password);
                                                }
                                            }
        );
        if(handlerMaxCapacity<=0){throw  new IllegalArgumentException("Error: emailhandlerCapacity <=0");}
        this.messagesList=new ArrayBlockingQueue<>(handlerMaxCapacity);
    }



    /**
     * Usa le impostazioni salvate nel file di configurazione passato
     */
    public EmailHandler(Properties serverProperties,int handlerMaxCapacity) throws IllegalArgumentException{
        this(
                serverProperties.getProperty("serveremail"),
                serverProperties.getProperty("emailpassword"),
                handlerMaxCapacity,
                Integer.parseInt(serverProperties.getProperty("smtpPort")),
                serverProperties.getProperty("smtpProvider")
        );
    }








    /* *******************************************************
        PUBLIC METHODS
     *********************************************************/


    /**
     * Crea
     * @param to l'idirizzo email del destinatario
     * @param subject il titolo della mail
     * @param bodyText il testo della mail
     * @return l'oggetto messaggio appena creato
     * @throws MessagingException
     */
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
            this.messagesList.notify();
        }
    }


    /**
     * Avvia il manager delle email (gestore della cosa delle email)
     */
    public void startEmailHandlerManager(){
        emailHandlerThread.submit(new EmailThread(this));
    }



    /* ****************************************************************************
        THREAD MANAGER
     ******************************************************************************/
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
                                synchronized(emailHandlerClass.messagesList) {
                                    emailHandlerClass.messagesList.wait();
                                }
                            }
                            Transport.send(toBeSent);
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
