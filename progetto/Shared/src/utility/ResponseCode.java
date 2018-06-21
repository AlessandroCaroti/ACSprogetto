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

         //I messaggi che iniziano per 600 sono di errore del server
         R500,  //null message
         R505,  //Internal Server Error


         //I messaggi che iniziano per 600 sono di errore del client
         R610,  //registrazione account fallita
         R620,  //errore disconnessione
         R630,  //login senza successo
         R640,  //inserito codice validazione mail errato
         R666,  //formato cookie non valido

         //internal server error
         R999

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
            throw new NullPointerException("codice  ==null");
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
    public boolean IsOK(){ return Codici.R200==codice; }
    public boolean IsSetCookie(){ return Codici.R100==codice;}
}

