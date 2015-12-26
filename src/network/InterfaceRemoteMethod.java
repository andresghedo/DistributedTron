package network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/* 
 * Interfaccia dei metodi remoti che vengono resi accessibili dall'esterno,
 * rendendoli chiamabili da altri host remoti.
 */
public interface InterfaceRemoteMethod extends Remote {
	
	void action1() throws RemoteException;
	void action2() throws RemoteException;

}
