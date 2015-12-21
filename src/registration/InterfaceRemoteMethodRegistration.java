package registration;

import java.rmi.Remote;
import java.rmi.RemoteException;

/* 
 * Interfaccia dei metodi remoti che vengono resi accessibili dall'esterno,
 * rendendoli chiamabili da altri host remoti.
 * Questi metodi sono utilizzati da un unico host per la registrazione al gioco 
 * di tutti i giocatori.
 */
public interface InterfaceRemoteMethodRegistration extends Remote {
	void incrementPlayers() throws RemoteException;
	int getCurrentPlayers() throws RemoteException;
}
