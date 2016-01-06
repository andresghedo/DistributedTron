package network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** 
 * Interfaccia dei metodi remoti che vengono resi accessibili dall'esterno,
 * rendendoli chiamabili da altri host remoti.
 */
public interface InterfaceRemoteMethod extends Remote {
	/** Logicamente è come una primitiva send in un anello unidirezionale */
	void send(RmiMessage message) throws RemoteException;
}
