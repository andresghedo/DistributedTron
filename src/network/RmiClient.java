package network;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import registration.InterfaceRemoteMethodRegistration;

public class RmiClient {
	
	public static void main(String[] args) throws RemoteException, NotBoundException, UnknownHostException, SocketException {
	     
		String IP_SERVER = args[0];
		int PORT = Integer.parseInt(args[1]);
		//int x = Integer.parseInt(args[2]);
		int x;
		System.out.println("NEL CLIENT");
		InterfaceRemoteMethod squareServer = null;
		InterfaceRemoteMethodRegistration registrationServer = null;
		//squareServer = (ISquareRoot) Naming.lookup ("rmi://"+IP_SERVER+"/RMISquareRoot");
		Registry register = LocateRegistry.getRegistry(IP_SERVER, PORT);
		squareServer = (InterfaceRemoteMethod) register.lookup("MethodService");
		registrationServer = (InterfaceRemoteMethodRegistration) register.lookup("RegistrationService");    
		Host myHost = new Host(NetworkUtility.getInstance().getHostAddress(), 1234);
		registrationServer.addPlayer(myHost);
		Scanner scanner=new Scanner(System.in);
    	while (true) {
    		System.out.println("[INPUT] Insert code operations:");
	        String question = scanner.nextLine();
	        if(question.equals("q")){
	            break;
	        }
	        else if(question.equals("1")){
	        	System.out.println("[INPUT] Insert number for operations 1:");
	            x = Integer.parseInt(scanner.nextLine());
	            double result = squareServer.calculateSquareRoot(x) ;
	            System.out.println(result);
	        }
	        else if(question.equals("2")){
	        	System.out.println("[INPUT] Insert number for operations 2:");
	        	x = Integer.parseInt(scanner.nextLine());
	        	double result = squareServer.calculatePowerTwo(x) ;
	            System.out.println(result);
	        }
	    } 
	}

}
