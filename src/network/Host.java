package network;

import java.io.Serializable;

/*
 * Classe che identifica un host coinvolto nella rete, implementa l'interfaccia serializzabile in
 * modo che un oggetto di tipo Host possa essere passato come parametro ad un metodo remoto tra
 * Client e Server (e/o viceversa).
 */
public class Host implements Serializable {
	
	private static final long serialVersionUID = 2122441862583128094L;
	private String IP;
	private int PORT;
	
	/* costructor */
	public Host() {
		
	}

	public Host(String IP, int port) {
		this.IP = IP;
		this.PORT = port;
	}
	
	public String getIP() {
		return this.IP;
	}

	public void setIP(String ip) {
		this.IP = ip;
	}
	
	public int getPort() {
		return this.PORT;
	}
	
	public void setPort(int port) {
		this.PORT = port;
	}
}
