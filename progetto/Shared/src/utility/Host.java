package utility;

import Interfaces.ClientInterface;
import Interfaces.ServerInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Host {
        private ExecutorService executors;
        public GuiInterfaceStream userInterface;



    public Host(boolean usingUserInterface){
            userInterface=new GuiInterfaceStream(usingUserInterface);
            executors= Executors.newFixedThreadPool(2);

    }





    public static void  Main(String[] args){
        //init
        if(args.length<2)
        {
            System.err.println("args: userinterface(true/false) hostType(server/client)");
            return;
        }
        Host host=new Host(Boolean.parseBoolean(args[0]));

        if(args[1].equals("server"))
        {

        }else{
            if(args[1].equals("client")){

            }
            else{
                System.err.println("args: userinterface(true/false) hostType(server/client)");
                return;
            }
        }




        return;
    }





}
