package registration;

import java.awt.Color;
import java.io.Serializable;

import network.Host;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String username;
	private Color color;
	private Host host;
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
