package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;

public class AddressIp {
	private static String localIp = "Unkown";
	private static String pubblicIp = "Unkown";
	
	static public String getLocalIp()
	{
		return localIp;
	}
	
	static public String getPublicIp()
	{
		return pubblicIp;
	}
	
	static public void updateIp() {
		getExternalAddres();
	}	
	
	static private void getLocalAddres()
	{
		try(final DatagramSocket socket = new DatagramSocket()){
			  socket.connect(InetAddress.getByName("1.1.1.1"), 10002);
			  localIp =  socket.getLocalAddress().getHostAddress();
		}catch(Exception e){
			localIp = "Unkown";
		}
	}
	
	static private void getExternalAddres()
	{
        try {
        	URL whatismyip = new URL("http://checkip.amazonaws.com");
        	BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        	pubblicIp = in.readLine();
            if (in != null)
            	in.close();
            getLocalAddres();
        }catch(IOException e) {
        	pubblicIp = "Unkown";
        	localIp = "Unkown";
        }
    }

}
