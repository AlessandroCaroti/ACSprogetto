package utility;

import java.net.*;

class InfoProviderProtocol extends Thread{
    final static int port = 6666;               //Porta in cui il server ha un serverSocket in ascolto che passa le info del registry
    final static int brodcastPort = 4444;       //Porta su cui il server trasmette in brodcast il proprio IP
    static boolean ready = false;               //Flag che specifica se tutto è stato impostato a dovere (in particolare il campo group
    final boolean pedantic = true;
    final int period = 5;                      //Secondi di distanza tra un brodcast e l'altro da parte del server
    final int timeOut = period+10;              //Secondi di attesa da parte del client dopo i quali si può dire con certezza che nella rete locale non c'è un infoProvider
    static InetAddress group;                   //Gruppo su cui trasmettere in brodcast il server, solo quelli che si sono uniti a questo gruppo riceveranno i messeggi tramessi dal server
    {
        try {
            group = InetAddress.getByName("255.255.255.255");
            ready = true;
        } catch (UnknownHostException e) {
            ready = false;
        }
    }

}
