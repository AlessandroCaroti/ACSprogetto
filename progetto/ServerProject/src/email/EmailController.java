package email;

import javax.mail.Message;
import javax.mail.MessagingException;

public interface EmailController {

    /**
     * Crea
     * @param to l'idirizzo email del destinatario
     * @param subject il titolo della mail
     * @param bodyText il testo della mail
     * @return l'oggetto messaggio appena creato
     * @throws MessagingException MessagingException
     */
    Message createEmailMessage(String to, String subject, String bodyText) throws MessagingException ;  //Todo controllare con wireshark se usa ssl

    /**
     * Inserisce il messaggio nella coda di quelli da inviare
     * @param message il messaggio da inviare (non null)
     * @throws IllegalStateException se è stata raggiunta la capacità massima della coda
     * @throws NullPointerException se viene passato un ref. messaggio null
     */
    void sendMessage(Message message)throws IllegalStateException,NullPointerException;

    /**
     * Avvia il manager delle email (gestore della cosa delle email)
     */
    void startEmailHandlerManager();



}
