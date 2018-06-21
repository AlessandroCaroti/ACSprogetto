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

import javax.mail.Message;
import javax.mail.MessagingException;

public interface EmailController {

    /**
     * Crea l'oggetto email da spedire
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
