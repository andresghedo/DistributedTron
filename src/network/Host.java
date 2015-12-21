package network;

public class Host {
	
	private String IP;
	private int PORT;
	
	/* costructor */
	public Host() {
		
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
