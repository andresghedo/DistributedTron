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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import registration.Player;

import network.Controller;
import network.RmiMessage;

import graphics.StartPanel;

public class SimpleTronFrame implements ActionListener, KeyListener {
	/** DIREZIONI DI MOVIMENTO DELLA MOTO */
	public static String[] direction = { "N", "S", "W", "E" };
	/** FINESTRA DI GIOCO */
	public final int WIDTH = 1000, HEIGHT = 600;
	/** GRANDEZZA QUADRATO DELLA MOTO */
	public final int SIZE_MOTO = 3;
	/** UPDATE DELLA FINESTRA */
	public final int TIMER_UPDATE = 20;
	/** VELOCITA DELLA MOTO */
	public final int SPEED = 3;
	/** PANNELLO DI DISEGNO */
	public JPanel panel;
	/** RETTANGOLO CHE RAPPRESENTA LA MOTO */
	public Rectangle motorbike;
	/** BOOLEANO CHE INDICA GIOCO ATTIVO O MENO */
	public boolean started = false;
	/** DIREZIONE CORRENTE DELLA MOTO */
	public String currentDirection;
	// Si ricorda della direzzione attuale della moto
	public String OldDirection;
	public Timer timer;

	private int i = 0;
	// Imposti la larghezza dei bordi
	private int NumLines = 5;
	// Contiene il mio nome da giocaotore
	public String MyName;
	// contiene il mio attuale punteggio
	private int Score = 0;
	// numero di giocatori
	private ArrayList<Player> AllPlayers;

	private int NumPlayer = 0;

	JFrame jframe;

	private ArrayList<Positions> OponentPositions = new ArrayList<Positions>();

	private ArrayList<Positions> OnlyMyPositions = new ArrayList<Positions>();

	/**
	 * COSTRUTTORE DI CLASSE setta il JFrame ed il Jpanel per la grafica e fa
	 * partire il gioco, ovvero il timer per l'update della grafica e dei
	 * movimenti della moto.
	 * 
	 */
	public SimpleTronFrame() {
		jframe = new JFrame();
		timer = new Timer(TIMER_UPDATE, this);
		panel = new JPanel();
		jframe.add(panel);
		panel.setBackground(Color.black);
		jframe.setTitle("DISTRIBUTED TRON");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		WindowUtility.centerWindow(jframe);
		jframe.setVisible(true);

		motorbike = new Rectangle(Controller.getInstance().getMyPlayer()
				.getStartXPos(), HEIGHT - 32, SIZE_MOTO, SIZE_MOTO);
		panel.getGraphics().setColor(Color.black);
		panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
		// Il mio nome
		MyName = Controller.getInstance().getMyPlayer().getUsername();

		AllPlayers = Controller.getInstance().getRoom().getPlayers();

		NumPlayer = Controller.getInstance().getRoom().getPlayers().size();
		this.startGame(timer);
	}

	/**
	 * Metodo che viene eseguito ad ogni frame update(TIMER_UPDATE), si
	 * preoccupa di inviare la posizione della moto agli altri host e di
	 * muoverla verso la direzione corrente.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// System.out.println("[" + Calendar.getInstance().getTimeInMillis() +
		// "][ACTION PERFORMED]");

		String uuid = Controller.getInstance().getMyHost().getUUID();
		RmiMessage m = new RmiMessage(motorbike, uuid);

		try {
			Controller.getInstance().getCommunication().getNextHostInterface()
					.send(m);
		} catch (RemoteException e1) {
			// System.out.println("[" + Calendar.getInstance().getTimeInMillis()
			// +
			// "]########### REMOTE EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (NotBoundException e1) {
			// System.out.println("[" + Calendar.getInstance().getTimeInMillis()
			// +
			// "]########### NOTBOUND EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (ServerNotActiveException e1) {
			// System.out.println("[" + Calendar.getInstance().getTimeInMillis()
			// +
			// "]########### SERVERNOTACTIVE EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (NullPointerException e1) { // return perchè nessuno parte
											// quando tutti non sono pronti ad
											// implementare la grafica
			// e1.printStackTrace();
			// System.out.println("[" + Calendar.getInstance().getTimeInMillis()
			// +
			// "]########### NULLPOINTER EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
			return;
		}

		if (started) {
			this.moveMoto();
		}

		this.repaint();
	}

	/**
	 * Metodo che muove la moto nella direzione corrente di quanto richiede la
	 * velocità (SPEED).
	 */
	public void moveMoto() {

		if (this.currentDirection.equals("N")) {
			motorbike.y -= this.SPEED;
		} else if (this.currentDirection.equals("S")) {
			motorbike.y += this.SPEED;
		} else if (this.currentDirection.equals("W")) {
			motorbike.x -= this.SPEED;
		} else if (this.currentDirection.equals("E")) {
			motorbike.x += this.SPEED;
		}
		OldDirection = currentDirection;
	}

	/**
	 * Metodo che disegna graficamente la moto sul JPanel e controlla di non
	 * andare a sbattere sulla propria scia
	 */
	public void drawMoto(Graphics g) {
		Score++;
		// // String visualPunteggio = Integer.toString(Score);
		// // TODO
		// g.drawString(" : "+Integer.toString(Score),
		// (WIDTH/2)+MyName.length(), 15);
		Positions CurrentPositions = new Positions(motorbike.x, motorbike.y);
		System.out.println("Coordinate Mie Iniziali " + motorbike.x + " , "
				+ motorbike.y);

		// Se esco fuori dai bordi ho perso
		if (motorbike.x < (0 + NumLines) || motorbike.x > (WIDTH - NumLines)
				|| motorbike.y < (0 + NumLines)
				|| motorbike.y > (HEIGHT - NumLines)) {
			started = false;

			clearPositions(g, OnlyMyPositions);
			JOptionPane.showMessageDialog(new JFrame(), "Punteggio : " + Score,
					"Hai Perso", JOptionPane.INFORMATION_MESSAGE);
			// System.exit(0);
		}

		// altrimenti vuol dire che sono all'interno del riquadro giusto e posso
		// continuare a giocare
		if (this.currentDirection.equals("N")) {
			for (int i = 0; i < SPEED; i++) {
				// System.out.println("Coordinate Mie Nord "+motorbike.x+" , "+motorbike.y+"-1");

				g.fillRect(motorbike.x, motorbike.y - i, motorbike.width,
						motorbike.height);

			}
			for (int i = 0; i < SPEED; i++) {
				Positions newPositions = new Positions(motorbike.x, motorbike.y - i);
				for (int j = 0; j < AllPlayers.size(); j++) {

					System.out.println("Ciclo i player");
					ArrayList<Positions> myPositions = AllPlayers.get(j).getCoordinatesPlayer();
					if (findPositions(newPositions, myPositions) || findPositions(newPositions,OnlyMyPositions)) {
						System.out.println("Un Player ha perso");
						clearPositions(g, OnlyMyPositions);
//						player.getCoordinatesPlayer().clear();
//						Controller.getInstance().getRoom().removePlayer(player);
						started = false;
						clearPositions(g, OnlyMyPositions);
						JOptionPane.showMessageDialog(new JFrame(), "Punteggio : "
								+ Score, "Hai Perso",
								JOptionPane.INFORMATION_MESSAGE);
						// System.exit(0);

					}
				}
				
			}

		} else if (this.currentDirection.equals("S")) {
			for (int i = 0; i < SPEED; i++) {
				// System.out.println("Coordinate Mie Sud "+motorbike.x+" , "+motorbike.y+i);

				g.fillRect(motorbike.x, motorbike.y + i, motorbike.width,
						motorbike.height);
			}

			for (int i = 0; i < SPEED; i++) {
				Positions newPositions = new Positions(motorbike.x, motorbike.y + i);
				for (int j = 0; j < AllPlayers.size(); j++) {

					System.out.println("Ciclo i player");
					ArrayList<Positions> myPositions = AllPlayers.get(j).getCoordinatesPlayer();
					if (findPositions(newPositions, myPositions) || findPositions(newPositions,OnlyMyPositions)) {
						System.out.println("Un Player ha perso");
						clearPositions(g, OnlyMyPositions);
//						player.getCoordinatesPlayer().clear();
//						Controller.getInstance().getRoom().removePlayer(player);
						started = false;
						clearPositions(g, OnlyMyPositions);
						JOptionPane.showMessageDialog(new JFrame(), "Punteggio : "
								+ Score, "Hai Perso",
								JOptionPane.INFORMATION_MESSAGE);
						// System.exit(0);

					}
				}
				
			}

		} else if (this.currentDirection.equals("W")) {
			for (int i = 0; i < SPEED; i++) {

				g.fillRect(motorbike.x - i, motorbike.y, motorbike.width,
						motorbike.height);
			}
			for (int i = 0; i < SPEED; i++) {
				Positions newPositions = new Positions(motorbike.x-i, motorbike.y );
				for (int j = 0; j < AllPlayers.size(); j++) {

					System.out.println("Ciclo i player");
					ArrayList<Positions> myPositions = AllPlayers.get(j).getCoordinatesPlayer();
					if (findPositions(newPositions, myPositions) || findPositions(newPositions,OnlyMyPositions)) {
						System.out.println("Un Player ha perso");
						clearPositions(g, OnlyMyPositions);
//						player.getCoordinatesPlayer().clear();
//						Controller.getInstance().getRoom().removePlayer(player);
						started = false;
						clearPositions(g, OnlyMyPositions);
						JOptionPane.showMessageDialog(new JFrame(), "Punteggio : "
								+ Score, "Hai Perso",
								JOptionPane.INFORMATION_MESSAGE);
						// System.exit(0);

					}
				}
				
			}

		} else if (this.currentDirection.equals("E")) {
			for (int i = 0; i < SPEED; i++) {

				g.fillRect(motorbike.x + i, motorbike.y, motorbike.width,
						motorbike.height);
			}
			for (int i = 0; i < SPEED; i++) {
				Positions newPositions = new Positions(motorbike.x +i, motorbike.y );
				for (int j = 0; j < AllPlayers.size(); j++) {

					System.out.println("Ciclo i player");
					ArrayList<Positions> myPositions = AllPlayers.get(j).getCoordinatesPlayer();
					if (findPositions(newPositions, myPositions) || findPositions(newPositions,OnlyMyPositions)) {
						System.out.println("Un Player ha perso");
						clearPositions(g, OnlyMyPositions);
//						player.getCoordinatesPlayer().clear();
//						Controller.getInstance().getRoom().removePlayer(player);
						started = false;
						clearPositions(g, OnlyMyPositions);
						JOptionPane.showMessageDialog(new JFrame(), "Punteggio : "
								+ Score, "Hai Perso",
								JOptionPane.INFORMATION_MESSAGE);
						// System.exit(0);

					}
				}
				
			}

		}

		// MyOldestPositions.add(CurrentPositions);

		OnlyMyPositions.add(CurrentPositions);
		// Player p = Controller.getInstance().getMyPlayer();
		// p.getCoordinatesPlayer().add(new Positions(motorbike.x,motorbike.y));

	}

	/**
	 * Metodo che decreta l'inizio del gioco.
	 */
	public void startGame(Timer t) {
		if (!started) {
			// t.setInitialDelay(1000); // messo un delay iniziale per evitare
			// che qualche nodo non abbia istanziato il frame
			t.start();
			System.out.println("[" + Calendar.getInstance().getTimeInMillis()
					+ "][STARTING GUI FRAME]");
			panel.getGraphics().setColor(Color.black);
			// panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
			this.currentDirection = "N";

			started = true;

		}
	}

	/**
	 * Metodo di repaint del JPanel, aggiorna il movimento della moto.
	 */
	public void repaint() {

		Graphics g = this.panel.getGraphics();
		g.setColor(Controller.getInstance().getMyPlayer().getColor());
		// scrivo il mio nome a video
		g.drawString(MyName, WIDTH / 2, 15);

		// Disegna i bordi della pista
		for (i = 0; i < NumLines; i++) {
			g.drawLine(i, 0, i, HEIGHT);
			g.drawLine(0, i, WIDTH, i);
			g.drawLine(WIDTH - i, 0, WIDTH - i, HEIGHT - 1);
			g.drawLine(0, HEIGHT - i, WIDTH - 2, HEIGHT - i);
		}

		if (started) {

			this.drawMoto(g);
		} else {
			g.setColor(Controller.getInstance().getMyPlayer().getColor());
			g.setColor(Color.red);
			g.setFont(new Font("Arial", 1, 20));
			g.drawString("Hai Perso", WIDTH / 10, HEIGHT / 10);

			// if(NumPlayer <= 0){
			// WindowUtility.closeWindow(jframe);
			//
			// }

		}
	}

	/**
	 * Metodo che disegna una moto di un colore c, passati come parametri. Viene
	 * utilizzato all'arrivo di messaggi esterni per disegnare moto avversarie.
	 */
	public void repaint(Rectangle motorbike, Color c) {
		// System.out.println("[" + Calendar.getInstance().getTimeInMillis() +
		// "][REPAINT DEBUG]");
		Graphics g = this.panel.getGraphics();
		g.setColor(c);
		// il mio colore
		Color mycolor = Controller.getInstance().getMyPlayer().getColor();
		Positions pos = new Positions(motorbike.x, motorbike.y);
		Player player = Controller.getInstance().getPlayerByColor(c);

		g.fillRect(motorbike.x, motorbike.y, motorbike.width, motorbike.height);
		
		if (player != null) {
			// if (!mycolor.equals(c)) {
			System.out.println("Player diverso dal null");
			for (int i = 0; i < AllPlayers.size(); i++) {

				System.out.println("Ciclo i player");
				ArrayList<Positions> myPositions = AllPlayers.get(i)
						.getCoordinatesPlayer();
				if (findPositions(pos, myPositions) || findPositions(pos,OnlyMyPositions)) {
					System.out.println("Un Player ha perso");
					clearPositions(g, player.getCoordinatesPlayer());
					player.getCoordinatesPlayer().clear();
					Controller.getInstance().getRoom().removePlayer(player);

				}
			}
		}
		
		// Player p = Controller.getInstance().getPlayerByColor(c);
		// p.getCoordinatesPlayer().add(new Positions(motorbike.x,motorbike.y));
		// in questo array ci tengo le posizioni di tutti i miei avversari
		player.getCoordinatesPlayer().add(pos);
		// OponentPositions.add(pos);
		System.out.println("Coordinate Ospiti" + motorbike.x + " , "
				+ motorbike.y);

	}

	// Controlla che le attuali coordinate di tron siano inesplorate
	private boolean findPositions(Positions currentPositions,
			ArrayList<Positions> positions) {
		boolean find = false;
		for (Positions p : positions) {
			if (p.getX() == currentPositions.getX()
					&& p.getY() == currentPositions.getY()) {
				// System.out.println(currentPositions.getX()+","+currentPositions.getY());
				// System.out.println(p.getX()+","+p.getY());
				find = true;
				break;
			}
		}
		return find;

	}

	// elimina le coordinate dei giocatori che perdono
	public void clearPositions(Graphics g, ArrayList<Positions> positions) {
		g.setColor(Color.black);
		for (Positions p : positions) {
			int a = p.getX();
			int b = p.getY();
			g.fillRect(a, b, motorbike.width, motorbike.height);

		}
		NumPlayer--;

	}

	/**
	 * Metodo che implementa l'ascoltatre alla pressioni delle frecce. Cambia
	 * solo la direzione corrente della moto, dato che il movimento è
	 * automatico, Faccio il controllo e non permetto alla moto di ritornare
	 * indietro
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			if (OldDirection != "S")
				this.currentDirection = "N";
			break;
		case KeyEvent.VK_DOWN:
			if (OldDirection != "N")
				this.currentDirection = "S";
			break;
		case KeyEvent.VK_LEFT:
			if (OldDirection != "E")
				this.currentDirection = "W";
			break;
		case KeyEvent.VK_RIGHT:
			if (OldDirection != "W")
				this.currentDirection = "E";
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}
}