package network;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RmiClient {
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
	     
		String IP_SERVER = args[0];
		int PORT = Integer.parseInt(args[1]);
		//int x = Integer.parseInt(args[2]);
		int x;
		System.out.println("NEL CLIENT");
		InterfaceRemoteMethod squareServer = null;
		//squareServer = (ISquareRoot) Naming.lookup ("rmi://"+IP_SERVER+"/RMISquareRoot");
		Registry register = LocateRegistry.getRegistry(IP_SERVER, PORT);
		squareServer = (InterfaceRemoteMethod) register.lookup("MethodService");
		   
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
