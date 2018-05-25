package utility;

public class hashFunctions {
    protected hashFunctions(){
    }

    /**Cripta la string passata
     * @param plainText il testo in chiaro
     * @return l'hash della stringa passata
     * @throws NullPointerException se plainText è null
     */
     static int stringHash(String plainText) throws NullPointerException
    {
        if(plainText==null)
        {
            throw new NullPointerException("plainText == null");
        }
        return plainText.hashCode();
    }
     static boolean compareHashandString(int hash,String string)
    {
        return hash==stringHash(string) ;
    }


}
