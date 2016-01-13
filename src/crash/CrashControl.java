package crash;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

import registration.Player;
import network.Controller;
import network.InterfaceRemoteMethod;
import network.RmiMessage;

public class CrashControl {
	
	private static CrashControl instance;
	
	/** cerca l'istanza singleton, se la trova la torna altrimenti la crea */
	public static CrashControl getInstance() {
        if(instance == null)
            instance = new CrashControl();
        return instance;
    }
	
	public InterfaceRemoteMethod repairCrash(Player crashedPlayer) throws NotBoundException, ServerNotActiveException, RemoteException {
		Controller.getInstance().getRoom().removePlayer(crashedPlayer);
		//Controller.getInstance().getFrameGUI().removeMotorBike(next.getColor());
		String uuid = Controller.getInstance().getMyHost().getUUID();
		RmiMessage m = new RmiMessage(Controller.getInstance().getRoom(), uuid);
		Controller.getInstance().getCommunication().getNextHostInterface().send(m);
	
		return Controller.getInstance().getCommunication().getNextHostInterface();
	}

}
