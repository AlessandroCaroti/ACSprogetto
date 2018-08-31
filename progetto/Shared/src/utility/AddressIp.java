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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;

public class AddressIp {
	private static String localIp = "Unknown";
	private static String publicIp = "Unknown";
	
	static public String getLocalIp()
	{
		return localIp;
	}
	
	static public String getPublicIp()
	{
		return publicIp;
	}
	
	static public void updateIp() {
		getExternalAddress();
	}

	static public String getLocalAddress() {
		try(final DatagramSocket socket = new DatagramSocket()){
			  socket.connect(InetAddress.getByName("1.1.1.1"), 10002);
			  localIp =  socket.getLocalAddress().getHostAddress();
		}catch(Exception e){
			localIp = "Unknown";
		}
		return localIp;
	}

	static public String getExternalAddress() {
        try {
        	URL whatismyip = new URL("http://checkip.amazonaws.com");
        	BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        	publicIp = in.readLine();
			in.close();
			getLocalAddress();
        }catch(IOException e) {
			publicIp = "Unknown";
			localIp = "Unknown";
        }
        return publicIp;
    }

}
