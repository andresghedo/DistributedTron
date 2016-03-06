package graphics;

import java.awt.AWTException;
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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import network.Controller;
import network.RmiMessage;

public class SimpleTronFrame implements ActionListener, KeyListener
{	
	/** DIREZIONI DI MOVIMENTO DELLA MOTO */
	public static String[] direction = {"N", "S", "W", "E"};
	/** FINESTRA DI GIOCO */
	public final int WIDTH = Controller.getInstance().WIDTH, HEIGHT = Controller.getInstance().HEIGHT;
	/** GRANDEZZA QUADRATO DELLA MOTO */
	public final int SIZE_MOTO = Controller.getInstance().SIZE_MOTO;
	/** UPDATE DELLA FINESTRA */
	public final int TIMER_UPDATE = Controller.getInstance().TIMER_UPDATE;
	/** VELOCITA DELLA MOTO*/
	public final int SPEED = Controller.getInstance().SPEED;
	/** X MASSIMA DELLA MATRICE DI GIOCO DELLE MOTO */
	public final int MATRIX_X_SIZE = Controller.getInstance().MATRIX_X_SIZE;
	/** Y MASSIMA DELLA MATRICE DI GIOCO DELLE MOTO */
	public final int MATRIX_Y_SIZE = Controller.getInstance().MATRIX_Y_SIZE;
	/** PANNELLO DI DISEGNO */
	public JPanel panel;
	/** RETTANGOLO CHE RAPPRESENTA LA MOTO */
	public Rectangle motorbike;
	/** BOOLEANO CHE INDICA GIOCO ATTIVO O MENO */
	public boolean started;
	/** DIREZIONE CORRENTE DELLA MOTO */
	public String currentDirection;
	/** TIMER PER L'UPDATE DEL JPANEL */
	public Timer timer;
	/** JFRAME DEL GIOCO */
	public JFrame jframe;
	/** MATRICE CHE CONTIENE LE POSIZIONI DELLE SCIE DEI GIOCATORI */
	public char[][] matrix;
	// Contiene il mio nome da giocaotore
	public String MyName;
	public int score;
	
	public static final int NumLines = 2;

	/** 
	 *  COSTRUTTORE DI CLASSE 
	 *	setta il JFrame ed il Jpanel per la grafica e fa partire il gioco,
	 *  ovvero il timer per l'update della grafica e dei movimenti della moto.
	 * 
	 */
	public SimpleTronFrame() {
		score =0;
		jframe = new JFrame();
		timer = new Timer(TIMER_UPDATE, this);
		panel = new JPanel();
		jframe.add(panel);
		// Il mio nome
		MyName = Controller.getInstance().getMyPlayer().getUsername();
		JLabel userLabel = new JLabel(MyName.toUpperCase());
		userLabel.setForeground(Color.WHITE);
		panel.add(userLabel);
		panel.setBackground(Color.black);
		panel.setBorder(BorderFactory.createMatteBorder(NumLines, NumLines, NumLines, NumLines, Controller.getInstance().getMyPlayer().getColor()));
		jframe.setTitle("DISTRIBUTED TRON");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		
		motorbike = new Rectangle(Controller.getInstance().getMyPlayer().getStartXPos(), Controller.getInstance().getMyPlayer().getStartYPos(), SIZE_MOTO, SIZE_MOTO);
		System.out.println("POSIZIONI DI PARTENZA  X:" + Controller.getInstance().getMyPlayer().getStartXPos() + "  Y:" + (HEIGHT-36));
		panel.getGraphics().setColor(Color.black);
		panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
		matrix = new char[MATRIX_X_SIZE][MATRIX_Y_SIZE];
		for(int i=0; i < MATRIX_X_SIZE; i++)
			for(int j=0; j < MATRIX_Y_SIZE; j++)
				matrix[i][j] = '0';
		WindowUtility.centerWindow(jframe);
		this.startGame();
	}

	/** 
	 *  Metodo che viene eseguito ad ogni frame update(TIMER_UPDATE), 
	 *  si preoccupa di inviare la posizione della moto agli altri host e di
	 *  muoverla verso la direzione corrente.
	 */
	@Override
	public void actionPerformed(ActionEvent e){

		System.out.println("[ACTION PERFORMED]");

		// preparo il messaggio della uova posizione della moto da inviare nell'anello
		String uuid = Controller.getInstance().getMyHost().getUUID();
		RmiMessage m = new RmiMessage(motorbike, uuid);
		m.setId(Controller.getInstance().getMyPlayer().getId());

		// invio il messaggio
		try {
			Controller.getInstance().getCommunication().getNextHostInterface().send(m);
		} catch (RemoteException e1) {
			System.out.println("########### REMOTE EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (NotBoundException e1) {
			System.out.println("########### NOTBOUND EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (ServerNotActiveException e1) {
			System.out.println("########### SERVERNOTACTIVE EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (NullPointerException e1) { // return perchè nessuno parte quando tutti non sono pronti ad implementare la grafica
			System.out.println("########### NULLPOINTER EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
			return;
		}

		if (started) {
			this.moveMoto();
		}

		try {
			this.repaint();
		} catch (AWTException e1) {
		}
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
	}

	/**
	 * Metodo che disegna graficamente la moto sul JPanel
	 */
	public void drawMoto(Graphics g) {
		score++;

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

	    System.out.println("POSIZIONE MOTO PATCH X:" + motorbike.x/SIZE_MOTO + "  Y:" + motorbike.y/SIZE_MOTO);
	    if (this.checkBorder(motorbike.x/SIZE_MOTO, motorbike.y/SIZE_MOTO) || (matrix[motorbike.x/SIZE_MOTO][motorbike.y/SIZE_MOTO] != '0')) {
	    	if(this.checkBorder(motorbike.x/SIZE_MOTO, motorbike.y/SIZE_MOTO)){
	    		System.out.println("[EXIT] SONO USCITO DAI BORDI!");
	    	}	    		
	    	else { 
	    		System.out.println("[EXIT] SCONTRO CON UN'ALTRA MOTO!");
	    	}
	    		
	    	System.exit(0);
	    }
	    // Setto la posizione come occupata dalla moto con un suo identificativo
	    matrix[motorbike.x/SIZE_MOTO][motorbike.y/SIZE_MOTO] = Controller.getInstance().getMyPlayer().getId();
	}

	/** Metodo privato che controlla se la patch su cui voglio muovere la moto esce dai bordi */
	private boolean checkBorder(int patchX, int patchY) {

		if ((patchX >= MATRIX_X_SIZE) || (patchX < 0))
			return true;
		if ((patchY >= MATRIX_Y_SIZE) || (patchY < 0))
			return true;
		return false;
	}

	/**
	 * Metodo che decreta l'inizio del gioco.
	 */
	public void startGame() {

		if (!started) {
			timer.start();
			System.out.println("[STARTING GUI FRAME]");
			panel.getGraphics().setColor(Color.black);
			panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
			this.currentDirection = Controller.getInstance().getMyPlayer().getStartDirection();
			started = true;
		}
	}

	/**
	 * Metodo che stampa un messaggio al vincitore.
	 */
	public void setWinnerLabel() {

		timer.stop();
		System.out.println("[WINNER]");
		JOptionPane.showMessageDialog(null, "Hai Vinto! "+ Integer.toString(score), " DISTRIBUTED TRON", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	/** Metodo che rimuove dalla matrice di gioco la scia relativa ad un giocatore che ha perso/andato in crash */
	public void removeCrashedNode(char id) {

		Graphics g = this.panel.getGraphics();

		for(int i=0; i < MATRIX_X_SIZE; i++)
			for(int j=0; j < MATRIX_Y_SIZE; j++) 
				if (matrix[i][j] == id) {
					matrix[i][j] = '0';
					g.setColor(Color.black);
					g.fillRect(i*4, j*4, motorbike.width, motorbike.height);
			}
	}

	/**
	 *  Metodo di repaint del JPanel, aggiorna il movimento della moto.
	 * @throws AWTException 
	 */
	public void repaint() throws AWTException
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
	public void repaint(Rectangle motorbike, Color c, char idPlayer)
	{
		System.out.println("[REPAINT DEBUG]");
		Graphics g = this.panel.getGraphics();
		g.setColor(c);
		g.fillRect(motorbike.x, motorbike.y, motorbike.width, motorbike.height);
		matrix[motorbike.x/SIZE_MOTO][motorbike.y/SIZE_MOTO] = idPlayer;
	}

	/**
	 *  Metodo che implementa l'ascoltatre alla pressioni delle frecce.
	 *  Cambia solo la direzione corrente della moto, dato che il movimento 
	 *  è automatico
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	        	if (!this.currentDirection.equals("S"))
	        		this.currentDirection = "N";
	            break;
	        case KeyEvent.VK_DOWN:
	        	if (!this.currentDirection.equals("N"))
	        		this.currentDirection = "S";
	            break;
	        case KeyEvent.VK_LEFT:
	        	if (!this.currentDirection.equals("E"))
	        		this.currentDirection = "W";
	            break;
	        case KeyEvent.VK_RIGHT :
	        	if (!this.currentDirection.equals("W"))
	        		this.currentDirection = "E";
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