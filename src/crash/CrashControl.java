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
		System.out.println("[CRASH CONTROL] Il nodo " + crashedPlayer.getHost().getIP() + " ha fatto crash!! che ha ID: " + crashedPlayer.getId());
		Controller.getInstance().getRoom().removePlayer(crashedPlayer);
		Controller.getInstance().getFrameGUI().removeCrashedNode(crashedPlayer.getId());
		String uuid = Controller.getInstance().getMyHost().getUUID();
		RmiMessage m = new RmiMessage(crashedPlayer, uuid);
		Controller.getInstance().getCommunication().getNextHostInterface().send(m);
		if(Controller.getInstance().getRoom().getPlayers().size() == 1) {
			Controller.getInstance().getFrameGUI().setWinnerLabel();
		}

		return Controller.getInstance().getCommunication().getNextHostInterface();
	}

	public void repairRingAndGUI(Player crashedPlayer) {
		System.out.println("[CRASH CONTROL NON DIRETTO] Il nodo " + crashedPlayer.getHost().getIP() + " ha fatto crash!! che ha ID: " + crashedPlayer.getId());
		Controller.getInstance().getRoom().removePlayer(crashedPlayer);
		Controller.getInstance().getFrameGUI().removeCrashedNode(crashedPlayer.getId());
	}

}
