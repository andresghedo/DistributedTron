package network;

import java.io.Serializable;

public class RmiMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uuid;
	private Object payload;
	
	public RmiMessage(Object o, String uuid) {
		this.payload = o;
		this.uuid = uuid;
	}
	
	public Object getPayload(){
		return this.payload;
	}
	
	public String  getUuid() {
		return this.uuid;
	}
}
