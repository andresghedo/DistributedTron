package network;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class StartCommunication {
	
	static int PORT = 1234;
	/**
	 * @param args
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 * @throws RemoteException 
	 * @throws AlreadyBoundException 
	 */
	/*
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
        String ipserver = NetworkUtility.getInstance().getHostAddress();
        System.out.println("[SERVER] IP : " + ipserver + "  in ascolto sulla porta "+ PORT +" ...");
        System.setProperty("java.rmi.server.hostname", ipserver);
        System.setProperty("java.rmi.disableHttp", "true");
        InterfaceRemoteMethod server = new RmiServer();
        //Registry registry1 = LocateRegistry.getRegistry();
        Registry registry1 = LocateRegistry.createRegistry(PORT);
        registry1.bind("MethodService", server);
        
        
        String IP_SERVER = args[0];
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
	}*/
	
		// possibile meccanismo per ping
		// effettua un'azione ogni tot millisecondi
	    public static long myLong = 1234;
	    public static long minut = 60000;

	    public static void main(String[] args) {
	    	/*System.out.println(System.getProperty("HOSTNAME"));
	        Timer timer = new Timer();
	        timer.schedule(new TimerTask() {

	            @Override
	            public void run() {
	                doStuff();
	            }
	        }, 0, minut);*/
	        

	        Properties prop = System.getProperties();
	        Set<String> a = prop.stringPropertyNames();
	        Iterator<String> keys = a.iterator();
	        while (keys.hasNext())
	        {
	            String key = keys.next();
	            String value = System.getProperty(key);
	            System.out.println(key + "=" + value);
	        }
	    }

	    public static void doStuff(){
	        //do stuff here
	    	System.out.println("Ping ...");
	    }
	
}
