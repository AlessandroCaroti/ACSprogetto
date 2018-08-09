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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import utility.Message;
import utility.ResponseCode;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import static utility.ResponseCode.Codici.R220;


public class Client extends AnonymousClient {

    /******************/
    /* client fields */
    private String plainPassword;
    private String email;

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
     * @param email             la mail associata all'account
     */
    public Client(String username, String plainPassword, String email ) throws RemoteException
    {
        super(username);
        if(plainPassword==null||email==null)
            throw new NullPointerException();
        this.plainPassword=plainPassword;
        this.email=email;
        this.className="CLIENT";
        try {
            ECDH_kayPair = ECDH.generateKeyPair(curveName);
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            errorStamp(e, "Error during generation of the keys for the ECDH algorithm.");
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
            errorStamp("Unable to register without the public key of the server.");
            return false;
        }
        try {
            ResponseCode responseCode = server_stub.register(this.skeleton);
            return registered(responseCode);
        }catch (RemoteException e){
            errorStamp(e, "Unable to reach the server.");
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
                        infoStamp("Account successfully recovered.");
                        return true;
                    }else{
                        this.cookie=null;
                        infoStamp("Invalid cookie trying with username and password");
                    }
                }
                response = server_stub.retrieveAccount(username, plainPassword, skeleton);
                if (response.getCodice() == R220 && this.retrieveCookie()) {
                    infoStamp("Account and cookie successfully recovered.");
                    return true;
                }
                errorStamp(response, "Impossible to retrieve information.");
                return false;
            } catch (RemoteException exc) {
                errorStamp(exc, "Unable to reach the server.");
                return false;
            }
        }
        errorStamp("Not connected to any server.");
        return false;
    }


    /**
     * pubblica un messaggio sul server a cui si è connessi
     * @param topic     il topic su cui pubblicare il messsaggio
     * @param title     il titolo del messaggio da inviare
     * @param text      il testo  del messaggio da inviare
     * @return          TRUE se andata a buon fine,FALSE altrimenti
     */
    @Override
    public boolean publish( String topic, String title, String text){
        if(connected()) {
            try {
                Message msg = createMessage(topic, title, text);
                ResponseCode response;
                response=server_stub.publish(this.cookie, msg);
                if(response.IsOK())
                {
                    topicsSubscribed.add(topic);
                    infoStamp("Message published.");
                    return true;
                }
                else {
                    errorStamp(response, "Error while publishing the message.");
                }
            }catch (Exception e) {
                errorStamp(e);
                return false;
            }
        }else {
            errorStamp("Not connected to any server.");
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




    /*****************************************************************************************************************
     * REMOTE METHOD *************************************************************************************************
     ****************************************************************************************************************/

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
            infoStamp("Created secret key sheared whit the server.\n");
            pedanticInfo("Secret key: " + Arrays.toString(sharedSecret));
            secretAesKey = new SecretKeySpec(sharedSecret, "AES");
            return ECDH_kayPair.getPublic();
        } catch (Exception e) {
            errorStamp(e, "Error during creation of shared secret key whit server.");
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
            errorStamp(e, "Errore durante la cifratura delle informazioni dell'account.");
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
    private Message createMessage(String topic, String title, String text){
        Message msg = null;
        try {
            msg = new Message(title, this.username, text, topic);
        } catch (Exception e) {
            errorStamp("An exception has been thrown during the creation of a message.");
        }
        return msg;
    }


    private boolean retrieveCookie(){

        try {
            if(connected()) {
                ResponseCode response = server_stub.retrieveCookie(this.username, this.plainPassword);
                if (response == null || !response.getCodice().equals(ResponseCode.Codici.R100)) {
                    errorStamp(response, "cookie retrieve failed");
                    return false;
                }
                this.cookie = response.getMessaggioInfo();
                infoStamp("Cookie successfully retrieved.");
                return true;
            }else{
                errorStamp("Not connected to any server.");
                return false;
            }
        }catch (RemoteException e){
            errorStamp(e, "Unable to reach the server.");
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
