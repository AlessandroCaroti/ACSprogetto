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

public interface ServerInterface extends Remote,Serializable
{

    ResponseCode register(String userName, String plainPassword, ClientInterface stub, String publicKey,String email) throws RemoteException;

    ResponseCode anonymousRegister(ClientInterface stub, String publicKey)throws RemoteException;

    ResponseCode retrieveAccount(String username, String plainPassword, ClientInterface clientStub)throws RemoteException;

    ResponseCode retrieveAccountByCookie( String cookie, String plainPassword, ClientInterface clientStub) throws RemoteException;

    ResponseCode retrieveCookie(String username,String plainPassword) throws  RemoteException;

    ResponseCode connect() throws RemoteException;

    ResponseCode disconnect(String cookie) throws RemoteException;

    ResponseCode subscribe(String cookie, String topicName) throws RemoteException;

    ResponseCode unsubscribe(String cookie,String topicName) throws RemoteException ;

    ResponseCode publish(String cookie, Message msg) throws RemoteException;

    void ping() throws RemoteException;

    String[] getTopicList() throws RemoteException;
}
