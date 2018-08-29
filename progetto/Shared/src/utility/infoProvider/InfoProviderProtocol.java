package utility.infoProvider;

import java.net.*;

class InfoProviderProtocol extends Thread{
    final static int broadcastPort = 50000;      //Porta su cui il server trasmette in brodcast il proprio IP
    static boolean ready = false;               //Flag che specifica se tutto è stato impostato a dovere (in particolare il campo group
    final int period = 5;                       //Secondi di distanza tra un brodcast e l'altro da parte del server
    final int timeOut = period+10;              //Secondi di attesa da parte del client dopo i quali si può dire con certezza che nella rete locale non c'è un infoProvider
    static InetAddress group;                   //Gruppo su cui trasmettere in brodcast il server
    {
        try {
            group = InetAddress.getByName("255.255.255.255");
            ready = true;
        } catch (UnknownHostException e) {
            ready = false;
        }
    }

}
