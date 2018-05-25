package utility;

public class hashFunctions {
    protected hashFunctions(){
    }

    /**Cripta la string passata NOTABENE:metodo non thread safe!
     * @param plainText il testo in chiaro
     * @return l'hash della stringa passata
     * @throws NullPointerException se plainText è null
     */
    public int stringHash(String plainText) throws NullPointerException
    {
        if(plainText==null)
        {
            throw new NullPointerException("plainText == null");
        }
        return plainText.hashCode();
    }
    /**Cripta la string passata NOTABENE:metodo  thread safe!
     * @param plainText il testo in chiaro
     * @return l'hash della stringa passata
     * @throws NullPointerException se plainText è null
     */
    public  synchronized int stringHashThreadSafe(String plainText) throws NullPointerException
    {
        if(plainText==null)
        {
            throw new NullPointerException("plainText == null");
        }
        return plainText.hashCode();
    }

}
