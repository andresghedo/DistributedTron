package network;

import java.net.*;
import java.util.Enumeration;

/*
 *  Classe di supporto alle funzionalità network del progetto.
 *  Adottato il Singleton Pattern -> nel progetto vogliamo che vi sia
 *  una ed una sola istanza di questo oggetto java.
 */
public class NetworkUtility {

	private static NetworkUtility instance;
	
	/* cerca l'istanza, se la trova la torna altrimenti la crea */
	public static NetworkUtility getInstance() {
        if(instance == null)
            instance = new NetworkUtility();
        return instance;
    }
	
    /**
     * Ritorna un indirizzo ip pubblico della macchina
     * @return string
     * @throws UnknownHostException
     */
    public InetAddress getRemoteIP() throws UnknownHostException {
        //Ottengo tutti gli IP della macchina
        String hostName = InetAddress.getLocalHost().getHostName();
        InetAddress addrs[] = InetAddress.getAllByName(hostName);
        // Cerco l'indirizzo che non sia per usi locali
        for (InetAddress addr : addrs) {
            if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
                return addr;
            }
        }
        // Torna nullo altrimenti
        return null;
    }
    
    /**
     * Ritorna un indirizzo ip pubblico della macchina
     * @return string
     * @throws UnknownHostException
     */
    public String getHostAddress() throws UnknownHostException, SocketException {

        for(Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces(); ni.hasMoreElements();) {
            NetworkInterface iface = ni.nextElement();
            if (iface.getName() != "lo") {
                for(Enumeration<InetAddress> addresses = iface.getInetAddresses(); addresses.hasMoreElements();){
                    InetAddress address = addresses.nextElement();
                    if(address instanceof Inet4Address)
                        return address.getHostAddress();
                }
            }
        }
        return null;
    }

}
