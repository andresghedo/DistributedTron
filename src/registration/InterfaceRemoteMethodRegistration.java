package registration;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import network.Host;

/**
 * Interfaccia dei metodi remoti che vengono resi accessibili dall'esterno,
 * rendendoli chiamabili da altri host remoti.
 * Questi metodi sono utilizzati da un unico host per la registrazione al gioco 
 * di tutti i giocatori.
 */
public interface InterfaceRemoteMethodRegistration extends Remote {
	/** Aggiunta di un giocatore al tavolo di gioco */
	void addPlayer(Host host) throws RemoteException, InterruptedException, ServerNotActiveException;
	/** Indica il numero di giocatori seduti al tavolo*/
	int getCurrentPlayers() throws RemoteException;
	/** Ritorna la stanza di gioco */
	Room getRoom() throws RemoteException;
}
