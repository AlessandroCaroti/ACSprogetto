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
package interfaces;

import utility.Message;
import utility.ResponseCode;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface ClientInterface extends Remote,Serializable
{
    ResponseCode notify(Message m) throws RemoteException;

    void isAlive()  throws RemoteException;

    //lo si può usare sia per la register sia per il forgot password!
    ResponseCode getCode(int nAttempts) throws RemoteException;

    //Metodo utilizzato nella register e nella retriveAccount fatto per stabilire una chiave segreta condivisa per criptare le informazioni senibili
    PublicKey publicKeyExchange(byte[] serverPubKey_encrypted) throws RemoteException;

    byte[] testSecretKey(byte[] messageEncrypted) throws RemoteException;

    byte[][] getAccountInfo();



}

