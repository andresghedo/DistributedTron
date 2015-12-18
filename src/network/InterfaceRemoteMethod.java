package network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/* 
 * Interfaccia dei metodi remoti che vengono resi accessibili dall'esterno,
 * rendendoli chiamabili da altri host remoti.
 */
public interface InterfaceRemoteMethod extends Remote {
	
	double calculateSquareRoot(double aNumber) throws RemoteException;
	double calculatePowerTwo(double aNumber) throws RemoteException;

}
