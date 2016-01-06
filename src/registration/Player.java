package registration;

import java.awt.Color;
import java.io.Serializable;

import network.Host;

/**
 *	Classe Player che rappresenta il giocatore, l'insieme dei Player previsti
 *  sono raccolti nella classe Room, in ArrayList. 
 */
public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** username del giocatore */
	private String username;
	/** colore utilizzato nell'interfaccia grafica */
	private Color color;
	/** classe host che tiene traccia dei parametri di networking */
	private Host host;
	/** posizione iniziale della moto del player nell'interfaccia grafica */
	private int startXPos;
	
	public Player() {}
	
	public Player(String u, Host h, Color c) {
		this.username = u;
		this.color = c;
		this.host = h;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String u) {
		this.username = u;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public Host getHost() {
		return this.host;
	}
	
	public void setHost(Host h) {
		this.host = h;
	}
	
	public int getStartXPos() {
		return this.startXPos;
	}
	
	public void setStartXPos(int x) {
		this.startXPos = x;
	}

}
