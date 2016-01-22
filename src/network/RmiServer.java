package network;

import graphics.SimpleTronFrame;

import java.awt.Rectangle;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;

import crash.CrashControl;
import registration.Player;
import registration.Room;


public class RmiServer extends UnicastRemoteObject implements InterfaceRemoteMethod {

	private static final long serialVersionUID = 1L;
	static int PORT = 1234;
	
	public RmiServer()throws RemoteException {}

	public void setRingConfiguration(Room room) {
		Controller.getInstance().setRoom(room);
		Room r = Controller.getInstance().getRoom();
		
		for (int i=0; i<r.getCurrentPlayers();i++) {
			Player corrente = r.getPlayers().get(i);
			Player successivo = r.getNext(corrente);
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][SETTING RING CONFIGURAZTION]NODO "+i+" , ip: "+corrente.getHost().getIP()+"  ==> next ip: "+successivo.getHost().getIP());
		}
	}

	@Override
	public void send(RmiMessage message) {
		try{
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][MSG RECEIVED] MSG FROM :"+Controller.getInstance().getIpPlayerFromUUid(message.getUuid()) + "  -  TYPE:"+ message.getPayload().getClass());
		} catch (Exception e) {
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][MSG RECEIVED] MSG FROM UUID:" + message.getUuid());
		}
		if (!message.getUuid().equals(Controller.getInstance().getMyHost().getUUID()))
			processPackage(message);
		else if(message.getPayload() instanceof Room) { // se il messaggio è per me ed è una Room
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][MSG CONFIGURATION RETURN] OGNI HOST HA LA CONFIGURAZIONE DELL'ANELLO UNIDIREZIONALE!");

			if (!Controller.getInstance().getIsGaming())
				if (Controller.getInstance().getShowGUI()) {
					this.startGUI();
					}
				else
					(new InputThread()).start();
			
		}
//		else // il messaggio è per me lo scarto 			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][MSG RETURNED] IL MESS HA FATTO IL GIRO DELL'ANELLO COMPLETO SENZA ERRORI!");
	}
	
	public void processPackage(RmiMessage message) {
		if (message.getPayload() instanceof Room) { // se il messaggio contiene una configurazione della Room settala
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][MSG RECEIVED] ARRIVATO IL MESSAGGIO DI CONFIGURAZIONE DELL'ANELLO, ORA LO SETTO.");
			this.setRingConfiguration((Room) message.getPayload());
			
			if (!Controller.getInstance().getIsGaming()) {
				if (Controller.getInstance().getShowGUI())  
					this.startGUI();
				else 
					(new InputThread()).start();
				}
//			else 				System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][MSG RECEIVED] ARRIVATO IL MESSAGGIO DI CONFIGURAZIONE DELL'ANELLO IN SEGUITO AD UN CRASH, ORA LO SETTO.");
		}
		if (message.getPayload() instanceof String) { // il messaggio contiene una stringa	
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][MSG FROM REMOTE] String: " + (String)message.getPayload());
		}
		if (message.getPayload() instanceof Rectangle) {
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][RECTANGLE MESSAGE] X:"+((Rectangle) message.getPayload()).x + "  Y:"+((Rectangle) message.getPayload()).y);
			Controller.getInstance().getFrameGUI().repaint((Rectangle) message.getPayload(), Controller.getInstance().getColorPlayerFromUUid(message.getUuid()));
		}
		
		try {
			this.getNextHostInterface().send(message);
		} catch (NotBoundException e) {
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### NOTBOUND EXCEPTION @ RMISERVER.PROCESSPACKAGE ###########");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### REMOTE EXCEPTION @ RMISERVER.PROCESSPACKAGE ###########");
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### SERVER NOT ACTIVE EXCEPTION @ RMISERVER.PROCESSPACKAGE ###########");
		}
	}
	
	public InterfaceRemoteMethod getNextHostInterface() throws NotBoundException, ServerNotActiveException, RemoteException {
		Player my = Controller.getInstance().getMyPlayer();
		Player next = Controller.getInstance().getRoom().getNext(my);	
		InterfaceRemoteMethod remoteServer = null;
		Registry register = null; 
		try {
			register = LocateRegistry.getRegistry(next.getHost().getIP(), PORT);
			remoteServer = (InterfaceRemoteMethod) register.lookup("MethodService");
		} catch (RemoteException e) {
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### REMOTE EXCEPTION @ RMISERVER.GETNEXTHOSTINTERFACE ###########");
			return CrashControl.getInstance().repairCrash(next);	
		} catch (Exception e) {
//			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### GENERAL EXCEPTION @ RMISERVER.GETNEXTHOSTINTERFACE ###########");
		}
		return remoteServer;
	}
	
	private void startGUI() {
		Controller.getInstance().setIsGaming(true);
		Controller.getInstance().setInitalXPlayerFromUUid();
//		System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][COORDINATE X]: "+Controller.getInstance().getMyPlayer().getStartXPos());
		Controller.getInstance().setFrameGUI(new SimpleTronFrame());
	}
}