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
package client;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import interfaces.ServerInterface;
import utility.*;
import utility.cryptography.AES;
import utility.cryptography.ECDH;
import utility.cryptography.RSA;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

import static utility.ResponseCode.Codici.R200;
import static utility.ResponseCode.Codici.R220;


public class Client extends AnonymousClient {

    /******************/
    /* client fields */
    private String plainPassword;
    private String email;
    protected String username;
    /* security fields */
    final private String curveName = "prime192v1";
    private KeyPair ECDH_kayPair;       //todo cercare di renderla final
    private PublicKey serverPublicKey_RSA;
    private SecretKeySpec secretAesKey;




    // ************************************************************************************************************
    //CONSTRUCTORS

    /**
     * Client's constructor
     * @param username          identificativo client
     * @param plainPassword     password in chiaro
     * @param email             la mail associata all'account(può essere vuota)
     */
    public Client(String username, String plainPassword, String email ) throws RemoteException
    {
        super();
        print = new LogFormatManager("CLIENT", true);
        if(plainPassword==null)
            throw new NullPointerException();
        this.plainPassword=plainPassword;
        this.username     = username;
        this.email=email;
        this.className = "CLIENT";        //TODO serve ancora questo campo?
        try {
            ECDH_kayPair = ECDH.generateKeyPair(curveName);
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            print.error(e, "Error during generation of the keys for the ECDH algorithm.");
            System.exit(1);
        }

    }



    /*****************************************************************************************************************
     * API ***********************************************************************************************************
     ****************************************************************************************************************/

    /**
     *Il client si registra sul server su cui si era connesso con il metodo connect() e viene settato il cookie
     * @return true se registrazione andata a buon fine, false altrimenti
     */
    @Override
    public boolean register() {
        if(serverPublicKey_RSA==null) {
            print.error("Unable to register without the public key of the server.");
            return false;
        }
        try {
            ResponseCode responseCode = server_stub.register(this.skeleton);
            return registered(responseCode);
        }catch (RemoteException e){
            print.error(e, "Unable to reach the server.");
            return false;
        }
    }



    /**
     * Si chiede al server di recuperare le informazioni legate al nostro account
     * Viene prima tentato l'accesso tramite (cookie,password) se fallisce tenta con (username,password)
     * @return TRUE se andata a buon fine,FALSE altrimenti
     */
    @Override
    public boolean retrieveAccount(){
        ResponseCode response;
        if(connected()) {
            try {

                if(this.getCookie()!=null) {
                    response = server_stub.retrieveAccountByCookie(this.getCookie(),this.plainPassword,this.skeleton);
                    if (response.getCodice() == R220) {
                        print.info("Account successfully recovered.");
                        return true;
                    }else{
                        this.cookie=null;
                        print.info("Invalid cookie trying with username and password");
                    }
                }
                response = server_stub.retrieveAccount(username, plainPassword, skeleton);
                if (response.getCodice() == R220 && this.retrieveCookie()) {
                    print.info("Account and cookie successfully recovered.");
                    return true;
                }
                print.error(response, "Impossible to retrieve information.");
                return false;
            } catch (RemoteException exc) {
                print.error(exc, "Unable to reach the server.");
                return false;
            }
        }
        print.error("Not connected to any server.");
        return false;
    }


    /**
     * pubblica un messaggio sul server a cui si è connessi
     * @param topic     il topic su cui pubblicare il messsaggio
     * @param title     il titolo del messaggio da inviare
     * @param text      il testo  del messaggio da inviare
     * @return TRUE se andata a buon fine, FALSE altrimenti
     */
    @Override
    public boolean publish( String topic, String title, String text){
        if(connected()) {
            try {
                Message msg = new Message(title, this.username, text, topic);
                ResponseCode response=server_stub.publish(this.cookie, msg);
                if(response.IsOK())
                {
//                    topicsSubscribed.add(topic);
                    this.subscribe(topic);
                    return true;
                }
                return false;
            }catch (Exception e) {
                return false;
            }
        }
        return false;
    }



    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getClassName() {
        return this.className;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    // *************************************************************************************************************
    //PRIVATE METHOD




    /*****************************************************************************************************************
     * REMOTE METHOD *************************************************************************************************
     ****************************************************************************************************************/

    /*La notify() di anonymousClient va già benissimo e il controllo che il messaggio ricevuto sia nostro non è necessario.
    TODO Questo controllo può essere effettuato dal client engine

    @Override
    public ResponseCode notify(Message m) {
        ResponseCode rc;
        if (m == null) {
            System.err.println("[DEBUG-STUMP] il messaggio ricevuto è nullo");
            rc = new ResponseCode(ResponseCode.Codici.R500, ResponseCode.TipoClasse.CLIENT, "(-) WARNING Il client ha ricevuto un messaggio vuoto");
        } else {

            rc = new ResponseCode(R200, ResponseCode.TipoClasse.CLIENT,
                    "(+) OK il client ha ricevuto il messaggio");

            messageManager.execute(() -> {
                Message mm;
                if (m.getAuthor().equals(this.username)) {  //messagio ignorato
                    try {   //yield per permettere al thread della publish di inserire il messaggio nella lista dei messaggi inviati e non ricevuti
                        Thread.sleep(0);
                    } catch (InterruptedException ignored) { }
                    while (true) {      //while loop per lo stesso motivo del commento precedente
                        mm = messagesSendAndNotReceived.poll();
                        if (mm == null)
                            continue;
                        if (mm.equals(m)) { //Rimozione del messaggio ricevuto da messagesSendAndNotReceived
                            System.err.println("**messaggio ignorato**");
                            return;
                        }
                        messagesSendAndNotReceived.add(mm);
                    }
                }

                print.pedanticInfo("Received new message\n" + m.toString());
            });
        }

        return rc;
    }
    */
    /** Metodo che produce una chiave segreta utilizzando il protocollo Diffie–Hellman
     *  con la variante che utilizza le curve ellittiche (Elliptic-curve Diffie–Hellman)
     *
     * @param serverPubKey_encrypted la chiave pubblica ECDH del server criptata con la chiave privata RSA del server
     * @return la chiave pubblica ECDH del client
     */
    @Override
    public PublicKey publicKeyExchange(byte[] serverPubKey_encrypted){
        try {
            byte[] serverPubKey_decrypted = RSA.decrypt(serverPublicKey_RSA, serverPubKey_encrypted);
            PublicKey serverPubKey = KeyFactory.getInstance("ECDH", "BC").generatePublic(new X509EncodedKeySpec(serverPubKey_decrypted));
            byte[] sharedSecret = ECDH.sharedSecretKey(ECDH_kayPair.getPrivate(), serverPubKey);
            print.pedanticInfo("Created secret key sheared whit the server:\n\t" + Arrays.toString(sharedSecret) + "\n");
            secretAesKey = new SecretKeySpec(sharedSecret, "AES");
            return ECDH_kayPair.getPublic();
        } catch (Exception e) {
            print.error(e, "Error during creation of shared secret key whit server.");
            return null;
        }
    }

    @Override
    public byte[] testSecretKey(byte[] messageEncrypted) {
        try {
            return AES.encrypt(messageEncrypted, secretAesKey);
        } catch (Exception e) { return null;}
    }

    @Override
    public byte[][] getAccountInfo(){
        try {
            byte[][] encryptedAccountInfo = new byte[3][];
            encryptedAccountInfo[0] = AES.encrypt(email.getBytes(),         secretAesKey);
            encryptedAccountInfo[1] = AES.encrypt(username.getBytes(),      secretAesKey);
            encryptedAccountInfo[2] = AES.encrypt(plainPassword.getBytes(), secretAesKey);

            /*
            byte[][] accountInfo = new byte[3][];
            accountInfo[0] = email.getBytes();
            accountInfo[1] = username.getBytes();
            accountInfo[2] = plainPassword.getBytes();
            */
            return encryptedAccountInfo;
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            print.error(e, "Errore durante la cifratura delle informazioni dell'account.");
            //todo forse aggiungere una migliore della gestione degli errori
        }
        return null;
    }




    /*****************************************************************************************************************
     * PRIVATE METHOD ************************************************************************************************
     ****************************************************************************************************************/

    @Override
    protected ServerInterface connect(String regHost, String server, Integer regPort)
    {
        try {
            Registry r = LocateRegistry.getRegistry(regHost, regPort);
            ServerInterface server_stub = (ServerInterface) r.lookup(server);
            ResponseCode rc = server_stub.connect();
            if(rc.IsOK()) {
                String pubKey_str = rc.getMessaggioInfo();
                serverPublicKey_RSA = stringToPublicKey(pubKey_str);
                return server_stub;
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }


    private boolean retrieveCookie(){

        try {
            if(connected()) {
                ResponseCode response = server_stub.retrieveCookie(this.username, this.plainPassword);
                if (response == null || !response.getCodice().equals(ResponseCode.Codici.R100)) {
                    //print.error(response, "cookie retrieve failed");
                    return false;
                }
                this.cookie = response.getMessaggioInfo();
                //print.info("Cookie successfully retrieved.");
                return true;
            }else{
                //print.error("Not connected to any server.");
                return false;
            }
        }catch (RemoteException e){
            //print.error(e, "Unable to reach the server.");
            return false;
        }
    }

    private static PublicKey stringToPublicKey(String publickey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] pubBytes64Decode = Base64.decode(publickey);
        java.security.interfaces.RSAPublicKey chiavePubblicaRicostruitra=null;
        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
        java.security.PublicKey pubKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(pubBytes64Decode));
        chiavePubblicaRicostruitra = (java.security.interfaces.RSAPublicKey) pubKey;
        return chiavePubblicaRicostruitra;
    }




}
