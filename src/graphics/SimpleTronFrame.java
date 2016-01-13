package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import network.Controller;
import network.RmiMessage;

public class SimpleTronFrame implements ActionListener, KeyListener
{	
	/** DIREZIONI DI MOVIMENTO DELLA MOTO */
	public static String[] direction = {"N", "S", "W", "E"}; 
	/** FINESTRA DI GIOCO */
	public final int WIDTH = 1000, HEIGHT = 600;
	/** GRANDEZZA QUADRATO DELLA MOTO */
	public final int SIZE_MOTO = 3;
	/** UPDATE DELLA FINESTRA */
	public final int TIMER_UPDATE = 20;
	/** VELOCITA DELLA MOTO*/
	public final int SPEED = 4;
	/** PANNELLO DI DISEGNO */
	public JPanel panel;
	/** RETTANGOLO CHE RAPPRESENTA LA MOTO */
	public Rectangle motorbike;
	/** BOOLEANO CHE INDICA GIOCO ATTIVO O MENO */
	public boolean started;
	/** DIREZIONE CORRENTE DELLA MOTO */
	public String currentDirection;
	//Si ricorda della direzzione attuale della moto
	public String OldDirection;
	public Timer timer;
	
	/** 
	 *  COSTRUTTORE DI CLASSE 
	 *	setta il JFrame ed il Jpanel per la grafica e fa partire il gioco,
	 *  ovvero il timer per l'update della grafica e dei movimenti della moto.
	 * 
	 */
	public SimpleTronFrame() {
		JFrame jframe = new JFrame();
		timer = new Timer(TIMER_UPDATE, this);
		panel = new JPanel();
		jframe.add(panel);
		panel.setBackground(Color.black);
		jframe.setTitle("DISTRIBUTED TRON");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		motorbike = new Rectangle(Controller.getInstance().getMyPlayer().getStartXPos(), HEIGHT-32, SIZE_MOTO, SIZE_MOTO);
		panel.getGraphics().setColor(Color.black);
		panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
		this.startGame(timer);
	}
	
	/** 
	 *  Metodo che viene eseguito ad ogni frame update(TIMER_UPDATE), 
	 *  si preoccupa di inviare la posizione della moto agli altri host e di
	 *  muoverla verso la direzione corrente.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][ACTION PERFORMED]");
		
		String uuid = Controller.getInstance().getMyHost().getUUID();
		RmiMessage m = new RmiMessage(motorbike, uuid);
		
		try {
			Controller.getInstance().getCommunication().getNextHostInterface().send(m);
		} catch (RemoteException e1) {
			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### REMOTE EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (NotBoundException e1) {
			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### NOTBOUND EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (ServerNotActiveException e1) {
			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### SERVERNOTACTIVE EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (NullPointerException e1) { // return perchè nessuno parte quando tutti non sono pronti ad implementare la grafica
			//e1.printStackTrace();
			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### NULLPOINTER EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
			return;
		}
		
		if (started) {
			this.moveMoto();
		}
		
		this.repaint();
	}
	
	/** 
	 *  Metodo che muove la moto nella direzione corrente di 
	 *  quanto richiede la velocità (SPEED).
	 */
	public void moveMoto() {
		
		if (this.currentDirection.equals("N")) {
			motorbike.y -= this.SPEED;
		}
		else if (this.currentDirection.equals("S")) {
			motorbike.y += this.SPEED;
		}
		else if (this.currentDirection.equals("W")) {
			motorbike.x -= this.SPEED;
		}
		else if (this.currentDirection.equals("E")) {
			motorbike.x += this.SPEED;
		}
		OldDirection = currentDirection;
	}
	
	/**
	 * Metodo che disegna graficamente la moto sul JPanel
	 */
	public void drawMoto(Graphics g) {
	
		if (this.currentDirection.equals("N")) {
			g.fillRect(motorbike.x, motorbike.y, motorbike.width, motorbike.height+SPEED);
		}
		else if (this.currentDirection.equals("S")) {
			g.fillRect(motorbike.x, motorbike.y-SPEED, motorbike.width, motorbike.height+SPEED);
		}
		else if (this.currentDirection.equals("W")) {
			g.fillRect(motorbike.x, motorbike.y, motorbike.width+SPEED, motorbike.height);
		}
		else if (this.currentDirection.equals("E")) {
			g.fillRect(motorbike.x-SPEED, motorbike.y, motorbike.width+SPEED, motorbike.height);
		}
	}
	
	/**
	 * Metodo che decreta l'inizio del gioco.
	 */
	public void startGame(Timer t){
		if (!started) {
			//t.setInitialDelay(1000);	// messo un delay iniziale per evitare che qualche nodo non abbia istanziato il frame
			t.start();
			System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][STARTING GUI FRAME]");
			panel.getGraphics().setColor(Color.black);
			panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
			this.currentDirection = "N";
			started = true;
		}
	}
	
	/**
	 *  Metodo di repaint del JPanel, aggiorna il movimento della moto.
	 */
	public void repaint()
	{
		Graphics g = this.panel.getGraphics();
		
		if(started) {
			g.setColor(Controller.getInstance().getMyPlayer().getColor());
			this.drawMoto(g);
		}
		else {
			g.setColor(Controller.getInstance().getMyPlayer().getColor());
			g.fillRect(motorbike.x, motorbike.y, motorbike.width, motorbike.height);
			g.setColor(Color.red);
			g.setFont(new Font("Arial", 1, 20));
			g.drawString("KEY UP TO START!", 15, HEIGHT / 10);
		}
	}
	
	/**
	 *  Metodo che disegna una moto di un colore c, passati come parametri.
	 *  Viene utilizzato all'arrivo di messaggi esterni per disegnare moto avversarie.
	 */
	public void repaint(Rectangle motorbike, Color c)
	{
		System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "][REPAINT DEBUG]");
		Graphics g = this.panel.getGraphics();
		g.setColor(c);
		
		g.fillRect(motorbike.x, motorbike.y, motorbike.width, motorbike.height);
	}
	
	/**
	 *  Metodo che implementa l'ascoltatre alla pressioni delle frecce.
	 *  Cambia solo la direzione corrente della moto, dato che il movimento 
	 *  è automatico, Faccio il controllo e non permetto alla moto di ritornare indietro
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            if(OldDirection != "S") this.currentDirection = "N";
	            break;
	        case KeyEvent.VK_DOWN:
	        	if(OldDirection != "N") this.currentDirection = "S";
	            break;
	        case KeyEvent.VK_LEFT:
	        	if (OldDirection != "E")this.currentDirection = "W";
	            break;
	        case KeyEvent.VK_RIGHT :
	        	if(OldDirection != "W")this.currentDirection = "E";
	            break;
	     }
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	
	@Override
	public void keyPressed(KeyEvent e)
	{
	}
}