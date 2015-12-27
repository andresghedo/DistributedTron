package network;

import java.util.ArrayList;

public class RingList<Host> extends ArrayList<Host> {

	public RingList() {
		super();
	}
	
	public Host getNext(Host host) {
		int index = this.indexOf(host);
		if (index == (this.size()-1))
			return this.get(0);
		return this.get(index + 1);
	}
	
}
