/**
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

**/
package utility;


import java.io.Serializable;

/**
 * Messaggio di risposta ,utilizzare il costruttore per definire e creare il messaggio
 * Il response code contiene 3 campi:il codice,chi l'ha generato, un messaggio informativo
 */

public  class ResponseCode implements Serializable {
    public  enum TipoClasse implements Serializable  {
        SERVER,
        CLIENT,
        SERVER_CLIENT
    }
     public enum Codici implements Serializable{
         R100,//Set Cookie
         R101,//pong message (nel messagge info è presente il tempo di delay

         //I messaggi con 200 sono messaggi di successo
         R200,  //OK
         R210,  //"set public key" all'interno del massageinfo è presente la chiave pubblica del server
         R220,  //account successfully retrieved(login a buon fine)

         //I messaggi che iniziano per 500 sono di errore del server
         R500,  //received null message
         R505,  //Internal Server Error


         //I messaggi che iniziano per 600 sono di errore del client
         R610,  //Registrazione account fallita
         R620,  //Errore disconnessione
         R630,  //Login senza successo
         R640,  //Topic non esistente
         R650,  //Codice validazione email inserito errato,Tentativi terminati
         R660,  //Username per la registrazione già in uso,sceglierne un altro
         R666,  //Formato cookie non valido
         R670   //Internal client error

    }

    private Codici codice;
    private TipoClasse classeGeneratrice;
    private String messaggioInfo;

    /**
     * Crea il codice di risposta
     * @param codiceRisposta  deve essere diverso da null
     * @param classeGeneratriceMessaggio deve essere diversi da null
     * @param messaggioInformativo informativo può essere null
     * @throws NullPointerException se codice o classegeneratrice corrispondono a null
     */

    public ResponseCode(Codici codiceRisposta, TipoClasse classeGeneratriceMessaggio, String messaggioInformativo)
            throws NullPointerException
    {
        if(codiceRisposta==null)
        {
            throw new NullPointerException("codice  == null");
        }
        if( classeGeneratriceMessaggio==null){
            throw new NullPointerException(" classeGeneratrice ==null");
        }
        if(messaggioInformativo==null)
        {
            this.messaggioInfo="";
        }
        this.codice=codiceRisposta;
        this.classeGeneratrice=classeGeneratriceMessaggio;
        this.messaggioInfo=messaggioInformativo;
    }

    public Codici getCodice() {
        return codice;
    }

    public String getMessaggioInfo() {
        return messaggioInfo;
    }
    public TipoClasse getClasseGeneratrice()
    {
        return classeGeneratrice;
    }
    public boolean IsOK(){
        if(codice==null){return false;}
        return Codici.R200==codice;
    }
    public boolean IsSetCookie(){ return Codici.R100==codice;}

    public String getStandardMessage(){
        switch (this.codice){
            case R100:
                return "Set Cookie.";
            case R101:
                return "Pong Message.";
            case R200:
                return "OK.";
            case R210:
                return "OK, Set Public Key.";
            case R220:
                return "OK, Account Retrieved.";
            case R500:
                return "ServerError - Null Message";
            case R505:
                return "ServerError - Internal Server Error.";
            case R610:
                return "ClientError - Registration Failed.";
            case R620:
                return "ClientError - Disconnection Error";
            case R630:
                return "ClientError - Login Failed";
            case R640:
                return "ClientError - Topics Not Existing";
            case R650:
                return "ClientError - Wrong Code, Attempts Terminated";
            case R660:
                return "ClientError - Username already in use";
            case R666:
                return "ClientError - Invalid Cookies";
            default:
                return "Message Of Code "+this.codice+" Not Supported";
        }
    }
}

