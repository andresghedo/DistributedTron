package network;

import java.io.Serializable;

/**
 * Classe che rappresenta il pPrototipo di un messaggio RMI.
 * Header->uuid dell'host per rendere il messaggio riconoscibile dal mittente stesso
 * PayLoad-> Object generico che può essere castato in qualsiasi classe (Purchè questa sia Serializzabile)
 * @author andreasd
 *
 */
public class RmiMessage implements Serializable {

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
	
	public void setPayload(Object o) {
		this.payload = o;
	}
}
