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


public class EmailHandlerTLS implements EmailController {

    private final boolean pedantic = false;

    private final String username;
    private final String password;
    private final Session session;
    private final BlockingQueue<Message> messagesList;
    private ExecutorService emailHandlerThread=Executors.newSingleThreadScheduledExecutor();


    /* ********************************************************
        CONSTRUCTORS
     **********************************************************/

    public EmailHandlerTLS(String myEmail,String myPassword,int handlerMaxCapacity,int smtpPort,String smtpProvider) throws   IllegalArgumentException{
        if(handlerMaxCapacity<=0){throw  new IllegalArgumentException("Error: emailhandlerCapacity <=0");}
        this.messagesList=new ArrayBlockingQueue<>(handlerMaxCapacity);
        this.username=requireNonNull(myEmail);
        this.password=requireNonNull(myPassword);
        infoStamp("Connecting to:"+this.username+"; password:"+this.password+";  smtpPort:"+Integer.toString(smtpPort)+";  smtpProvider:"+smtpProvider+";");

        Properties props=new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpProvider);
        props.put("mail.smtp.port",Integer.toString(smtpPort));

        this.session=Session.getInstance(props,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myEmail,myPassword);
            }
                });

    }



    /**
     * Usa le impostazioni salvate nel file di configurazione passato
     */
    public EmailHandlerTLS(Properties serverProperties,int handlerMaxCapacity) throws IllegalArgumentException{
        this(
                serverProperties.getProperty("serveremail"),
                serverProperties.getProperty("emailpassword"),
                handlerMaxCapacity,
                Integer.parseInt(serverProperties.getProperty("smtpPort")),
                serverProperties.getProperty("smtpProvider")
        );
    }

    public BlockingQueue<Message> getMessagesList() {
        return messagesList;
    }


    /* *******************************************************
        PUBLIC METHODS
     *********************************************************/

    public  Message createEmailMessage(String to,String subject,String bodyText) throws MessagingException {
        Message message=new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(bodyText);
        return message;
    }


    public  void sendMessage(Message message)throws IllegalStateException,NullPointerException {

        if (message == null) {
            throw new NullPointerException("Error: message == null.");
        }
        this.messagesList.add(message);
        synchronized (messagesList) {
            pedanticInfo("Waking up the email demon.");
            this.messagesList.notify();
        }
    }

    public void startEmailHandlerManager(){
        emailHandlerThread.submit(new EmailThread(this));
    }



    /* ****************************************************************************
        THREAD MANAGER
     ******************************************************************************/
    private class EmailThread implements Runnable{
        private EmailHandlerTLS emailHandlerClass;

        private EmailThread(EmailHandlerTLS handlerClass){
            this.emailHandlerClass=requireNonNull(handlerClass);
        }

        public  void run(){/*TODO not sure on this part (Interrupted exception )*/
            Message toBeSent;
            while(true){
                try {
                    while((toBeSent=emailHandlerClass.messagesList.poll())==null) {
                        synchronized(emailHandlerClass.messagesList) {
                            pedanticInfo("Email daemon's going to sleep.");
                            emailHandlerClass.messagesList.wait();
                        }
                    }
                    pedanticInfo("Trying to send message ...");
                    Transport.send(toBeSent);
                    pedanticInfo("... message sent!");  //todo da cntrollare, non viene mai stampata la scritta
                }
                catch (InterruptedException |MessagingException e) {
                    if(e instanceof InterruptedException) {
                        return;
                    }
                    errorStamp(e,"unable to send email");
                }
            }

        }
    }

    private void errorStamp(Exception e){
        System.out.flush();
        System.err.println("[EMAILHANDLER-ERROR]");
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[EMAILHANDLER-ERROR]: "      + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void warningStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[EMAILHANDLER-WARNING]: "    + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }
    private void infoStamp(String msg){
        System.out.println("[EMAILHANDLER-INFO]: " + msg);
    }

    private void pedanticInfo(String msg){
        if(pedantic){
            infoStamp(msg);
        }
    }
}
