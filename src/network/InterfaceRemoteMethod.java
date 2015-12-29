package network;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import registration.Room;

/* 
 * Interfaccia dei metodi remoti che vengono resi accessibili dall'esterno,
 * rendendoli chiamabili da altri host remoti.
 */
public interface InterfaceRemoteMethod extends Remote {
	
	void action1() throws RemoteException;
	void action2() throws RemoteException;
	void message1(String m) throws RemoteException;
	void setRingConfiguration(Room room) throws RemoteException, UnknownHostException, SocketException;
	void send(RmiMessage message) throws RemoteException;
	// TODO

}
