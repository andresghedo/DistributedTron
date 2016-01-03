package network;

import registration.Room;
/**
 * Classe Controller, implementa il pattern SINGLETON. La classe contiene tutti gli
 * elementi necessari per amministrare l'Host. La seguente soluzione permette di richiamare
 * l'unica istanza del Controller da qualsiasi punto del codice ed accedere tramite i getters 
 * ad elementi fondamentali per amministrare diversi aspetti(ad esempio accedere alla configurazione
 * dell'anello, ai dati personali dell'host, oppure al server in ascolto di client per l'esportazione
 * dei propri metodi remoti).
 * @author andreasd
 *
 */
public class Controller {
	
private static Controller instance;
	
	private Host host;
	private Room room;
	private RmiServer communication;

	/* cerca l'istanza, se la trova la torna altrimenti la crea */
	public static Controller getInstance() {
        if(instance == null)
            instance = new Controller();
        return instance;
    }
	
	public Host getMyHost() {
		return this.host;
	}
	
	public void setMyHost(Host host) {
		this.host = host;
	}
	
	public Room getRoom() {
		return this.room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public RmiServer getCommunication() {
		return this.communication;
	}
	
	public void setCommunication(RmiServer c) {
		this.communication = c;
	}
}
