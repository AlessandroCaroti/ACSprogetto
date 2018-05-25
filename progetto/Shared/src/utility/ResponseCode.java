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
        R200,
        R400,//bad request
         R500//null message

    }

    private Codici codice;
    private  TipoClasse classeGeneratrice;
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
}

