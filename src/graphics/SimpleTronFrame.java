package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import network.Controller;
import network.RmiMessage;

public class SimpleTronFrame implements ActionListener, MouseListener, KeyListener
{	
	/** DIREZIONI DI MOVIMENTO DELLA MOTO */
	public static String[] direction = {"N", "S", "W", "E"}; 
	/** FINESTRA DI GIOCO */
	public final int WIDTH = 800, HEIGHT = 800;
	/** GRANDEZZA QUADRATO DELLA MOTO */
	public final int SIZE_MOTO = 3;
	/** UPDATE DELLA FINESTRA */
	public final int TIMER_UPDATE = 600;
	/** VELOCITA DELLA MOTO*/
	public final int SPEED = 3;
	/** PANNELLO DI DISEGNO */
	public JPanel panel;
	/** RETTANGOLO CHE RAPPRESENTA LA MOTO */
	public Rectangle motorbike;
	/** BOOLEANO CHE INDICA GIOCO ATTIVO O MENO */
	public boolean started;
	/** DIREZIONE CORRENTE DELLA MOTO */
	public String currentDirection;

	public SimpleTronFrame() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(TIMER_UPDATE, this);
		panel = new JPanel();
		jframe.add(panel);
		panel.setBackground(Color.black);
		jframe.setTitle("DISTRIBUTED TRON");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		motorbike = new Rectangle(Controller.getInstance().getMyPlayer().getStartXPos(), HEIGHT/2, SIZE_MOTO, SIZE_MOTO);
		timer.start();
		panel.getGraphics().setColor(Color.black);
		panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
		this.startGame();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("[ACTION PERFORMED]");
		if (started) {
			this.moveMoto();
		}
		this.repaint();
        String uuid = Controller.getInstance().getMyHost().getUUID();
		RmiMessage m = new RmiMessage(motorbike, uuid);
		try {
			Controller.getInstance().getCommunication().getNextHostInterface().send(m);
		} catch (RemoteException e1) {
			System.out.println("########### REMOTE EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (NotBoundException e1) {
			System.out.println("########### NOTBOUND EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (ServerNotActiveException e1) {
			System.out.println("########### SERVERNOTACTIVE EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		} catch (Exception e1) {
			System.out.println("########### GENERAL EXCEPTION @ SIMPLETRONFRAME.ACTIONPERFORMED ###########");
		}
	}
	
	public void moveMoto() {
		
		if (!started)
		{
			panel.getGraphics().setColor(Color.black);
			panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
			this.currentDirection = "N";
			started = true;
		}
		else if (this.currentDirection.equals("N")) {
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
	
	public void startGame(){
		if (!started) {
			System.out.println("[STARTING GUI FRAME]");
			panel.getGraphics().setColor(Color.black);
			panel.getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
			this.currentDirection = "N";
			started = true;
		}
	}
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
	
	public void repaint(Rectangle motorbike, Color c)
	{
		Graphics g = this.panel.getGraphics();
		g.setColor(c);
		g.fillRect(motorbike.x, motorbike.y, motorbike.width, motorbike.height);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            this.currentDirection = "N";
	            break;
	        case KeyEvent.VK_DOWN:
	        	this.currentDirection = "S";
	            break;
	        case KeyEvent.VK_LEFT:
	        	this.currentDirection = "W";
	            break;
	        case KeyEvent.VK_RIGHT :
	        	this.currentDirection = "E";
	            break;
	        case KeyEvent.VK_ENTER:
	        	this.startGame();
	     }
	}


	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	
	@Override
	public void keyPressed(KeyEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent arg0) {		
	}

}