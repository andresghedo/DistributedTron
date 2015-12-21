package registration;

public class Room {
	
	/* numero di giocatori necessari per iniziare il gioco */
	private int startPlayers;
	/* numero di giocatori iscritti, ad un certo istante */
	private int currentPlayers;
	
	public Room() {
		this.currentPlayers = 1;
	}

	public Room(int sp) {
		this.currentPlayers = 1;
		this.startPlayers = sp;
	}
	
	public int getStartPlayers() {
		return this.startPlayers;
	}
	
	public void setStartPlayers(int sp) {
		this.startPlayers = sp;
	}
	
	public int getCurrentPlayers() {
		return this.currentPlayers;
	}
	
	public void setCurrentPlayers(int cp) {
		this.currentPlayers = cp;
	}
	
	public void incrementCurrentPlayers() {
		this.currentPlayers++;
	}
	
	/* controlla la disponibilità ad accogliere altri host o meno */
	public boolean isCompleted() {
		if ((this.startPlayers - this.currentPlayers)==0)
			return true;
		return false;
	}
}
