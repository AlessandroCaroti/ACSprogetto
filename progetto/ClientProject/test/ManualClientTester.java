import client.AnonymousClient;
import client.Client;
import utility.ServerInfoRecover;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ManualClientTester {
    public static void main(String[] args) {

        System.out.println("STARTING TESTER");
        String username;String password;String email;String pubKey="tobeimplememted";String privKey="tobeimplememted";
        List<AnonymousClient> clients = new LinkedList<>();
        AnonymousClient currentClient=null;
        AnonymousClient it;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        int scelta;


        while (true) {
            try{
            System.out.println("Cosa vuoi fare:\n" +
                    "1:create client and set as current\n" +
                    "2:create anonymousclient and set as current\n" +
                    "3:print client list\n" +
                    "4:select client\n"+
                    "5:print currentclient info\n"+
                    "6:connect current to server automatic\n"+
                    "7:register current\n"+
                    "8:disconnect current\n"+
                    "9:retrieve current account1"

            );
            scelta = Integer.parseInt(bufferRead.readLine());
            switch (scelta) {
                case 1:

                    System.out.println("Inserisci username");
                    username=bufferRead.readLine();
                    System.out.println("Inserisci password");
                    password=bufferRead.readLine();
                    System.out.println("Inserisci email");
                    email=bufferRead.readLine();
                    clients.add(currentClient=new Client(username,password,pubKey,privKey,email));
                    break;
                case 2:
                    System.out.println("Inserisci username");
                    username=bufferRead.readLine();
                    clients.add(currentClient=new AnonymousClient(username,pubKey,privKey));
                    break;
                case 3:
                    for(int i=0;i<clients.size();i++){
                        it=clients.get(i);
                        if(it instanceof Client) {
                            System.out.println("i:"+i+"  username:" + it.getUsername() + "  password"+((Client) it).getPlainPassword()+"   email:"+((Client)it).getEmail()+"  cookie:"+it.getCookie());
                        }else if(it !=null){
                            System.out.println("i:"+i+"  username:" + it.getUsername() +"  cookie:"+it.getCookie());
                        }
                    }

                    break;
                case 4:
                    System.out.println("Inserisci indice:");
                    currentClient=clients.get(Integer.parseInt(bufferRead.readLine()));
                    break;
                case 5:
                    if(currentClient==null){break;}
                    it=currentClient;
                    if(it instanceof Client)  {
                        System.out.println("username:" + it.getUsername() + "  password"+((Client) it).getPlainPassword()+"   email:"+((Client)it).getEmail()+"  cookie:"+it.getCookie());
                    }else{
                        System.out.println("username:" + it.getUsername() +"  cookie:"+it.getCookie());
                    }
                    break;
                case 6:
                    if(currentClient==null){System.out.println("currentclient==null");break;}
                    ServerInfoRecover infoServer = new ServerInfoRecover();
                    String[] a = infoServer.getServerInfo();
                    currentClient.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                    break;
                case 7:
                    if(currentClient==null){System.out.println("currentclient==null");break;}
                    if(currentClient instanceof Client){
                        if (currentClient.register()) {
                            System.out.println("REGISTRATO");
                        } else {
                            System.out.println("NON REGISTRATO");
                        }
                    }else {
                        if (currentClient.register()) {
                            System.out.println("REGISTRATO");
                        } else {
                            System.out.println("NON REGISTRATO");
                        }
                    }break;
                case 8:
                    if(currentClient==null){System.out.println("currentclient==null");break;}
                    if(currentClient.disconnect()){
                        System.out.println("DISCONNESSO");
                    }else{System.out.println("NON Disconnesso");}
                    break;
                case 9:
                    if(currentClient==null){System.out.println("currentclient==null");break;}
                    if(currentClient.retrieveAccount()){
                        System.out.println("Account recuperato");
                    }else{System.out.println("NON recuperato");}
                    break;
                default:
                    System.out.println("Comando inesistente");
                    break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        }//endwhile
    }
}
