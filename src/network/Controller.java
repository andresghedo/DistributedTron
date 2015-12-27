package network;

import registration.Room;

public class Controller {
	
private static Controller instance;
	
	private Host host;
	private Room room;

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
}
