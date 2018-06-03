package utility;
/**
 * Messaggio di risposta ,utilizzare il costruttore per definire e creare il messaggio
 * Il response code contiene 3 campi:il codice,chi l'ha generato, un messaggio informativo
 */

public class ResponseCode {
    public enum TipoClasse  {
        SERVER,
        CLIENT,
        SERVER_CLIENT
    }
     public enum Codici{
         R100,//Set Cookie
         R101,//pong message (nel messagge info è presente il tempo di delay

         //i messaggi con 200 sono messaggi di successo
         R200,//OK
         R210,//"set public key" all'interno del massageinfo è presente la chiave pubblica del server
         R220,//account successfully retrieved(login a buon fine)

         //i messaggi che iniziano per 600 sono di errore
         R500,//null message
         R610,//registrazione account fallita
         R620,//errore disconnessione
         R630,//login senza successo
         R666,//formato cookie non valido

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

    public ResponseCode(Codici codiceRisposta,TipoClasse classeGeneratriceMessaggio,String messaggioInformativo)
            throws NullPointerException
    {
        if(codice==null || classeGeneratrice==null)
        {
            throw new NullPointerException("codice o classeGeneratrice ==null");
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

