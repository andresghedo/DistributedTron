package network;

import java.net.*;
import java.util.*;

/**
 *  Classe di supporto alle funzionalità network del progetto.
 *  Adottato il Singleton Pattern -> nel progetto vogliamo che vi sia
 *  una ed una sola istanza di questo oggetto java.
 */
public class NetworkUtility {

	private static NetworkUtility instance;

	/** cerca l'istanza, se la trova la torna altrimenti la crea */
	public static NetworkUtility getInstance() {
        if(instance == null)
            instance = new NetworkUtility();
        return instance;
    }

    /**
     * Ritorna un indirizzo ip pubblico della macchina.
     * Quello che inizia con 192.168
     * @return string
     * @throws UnknownHostException
     */
    public String getHostAddress() throws UnknownHostException, SocketException {

        for(Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces(); ni.hasMoreElements();) {
            NetworkInterface iface = ni.nextElement();
            if (iface.getName() != "lo") {
                for(Enumeration<InetAddress> addresses = iface.getInetAddresses(); addresses.hasMoreElements();){
                    InetAddress address = addresses.nextElement();

                    if(address instanceof Inet4Address){
                    	String strHostIp = address.getHostAddress();
                		if (strHostIp.startsWith("192.168"))
                			return strHostIp;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Torna un uuid univoco in modo da identificare, appunto univocamente,
     * il messaggio e l'host stesso.
     * @return
     */
    public String getRandomUUID() {
    	return UUID.randomUUID().toString();
    }

}
