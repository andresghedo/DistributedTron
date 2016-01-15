package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import network.NetworkUtility;
import start.StartPlay;

public class StartPanel implements ChangeListener, ActionListener {

	/** GRANDEZZE FINESTRA DI IMPOSTAZIONI **/
	private final int WIDTH = 500, HEIGHT = 300;
	/** ELEMENTI GRAFICI **/
	private JFrame jFrame;
	private JPanel panel;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel serverIpLabel;
	private JTextField ipField;
	private JCheckBox serverCheckBox;
	private JLabel nplayersLabel;
	private JTextField nplayersField;
	private JLabel showIPLabel;
	private JButton connectButton;
	/** VARIABILI GLOBALI UTILI PER COMINCIARE LA PARTITA **/
	private String serverIP;
	private String username;
	private int nPlayers;

	public StartPanel() {
		// Instanzazione degli elementi grafici della finestra
		jFrame = new JFrame("Impostazioni di Gioco");
		SpringLayout layout = new SpringLayout();
		panel = new JPanel(layout);
		usernameLabel = new JLabel("SERVER Username:");
		usernameField = new JTextField(10);
		serverIpLabel = new JLabel("SERVER IP:");
		ipField = new JTextField(10);
		serverCheckBox = new JCheckBox("Sono il server");
		nplayersLabel = new JLabel("N. di giocatori:");
		nplayersField = new JTextField(2);
		showIPLabel = new JLabel();
		connectButton = new JButton("Connetti");

		// impostazione degli elementi grafici
		jFrame.setSize(WIDTH, HEIGHT);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBackground(Color.LIGHT_GRAY);
		nplayersLabel.setVisible(false);
		nplayersField.setVisible(false);
		showIPLabel.setVisible(false);
		connectButton.setVerticalTextPosition(AbstractButton.CENTER);
		connectButton.setHorizontalTextPosition(AbstractButton.LEADING);
		// aggiunta dei listener alla checkbox ed al button di connessione
		serverCheckBox.addChangeListener(this);
		connectButton.addActionListener(this);
		
		// posizionamento degli elementi grafici
		setLayoutConstraints(layout);
		
		// aggiunta degli elementi grafici al panel ed al frame principale
		panel.add(usernameLabel);
		panel.add(usernameField);
		panel.add(serverIpLabel);
		panel.add(ipField);
		panel.add(serverCheckBox);
		panel.add(nplayersLabel);
		panel.add(nplayersField);
		panel.add(showIPLabel);
		panel.add(connectButton);
		jFrame.add(panel);

		WindowUtility.centerWindow(jFrame);
		jFrame.setVisible(true);
	}
	
	/** 
	 * Metodo che si occupa di impostare il posizionamento degli elementi
	 * grafici nella finestra
	 *  
	 *  @param layout
	 */
	private void setLayoutConstraints(SpringLayout layout) {

		layout.putConstraint(SpringLayout.WEST, usernameLabel, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, usernameLabel, 22, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, usernameField, 65, SpringLayout.EAST, serverIpLabel);
		layout.putConstraint(SpringLayout.NORTH, usernameField, 20, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, serverIpLabel, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, serverIpLabel, 32, SpringLayout.NORTH, usernameLabel);
		layout.putConstraint(SpringLayout.WEST, ipField, 5, SpringLayout.EAST, serverIpLabel);
		layout.putConstraint(SpringLayout.NORTH, ipField, 30, SpringLayout.NORTH, usernameField);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ipField, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, serverCheckBox, 10, SpringLayout.SOUTH, ipField);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, serverCheckBox, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, nplayersLabel, 12, SpringLayout.SOUTH, serverCheckBox);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, nplayersLabel, -80, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, nplayersField, 10, SpringLayout.SOUTH, serverCheckBox);
		layout.putConstraint(SpringLayout.WEST, nplayersField, 5, SpringLayout.EAST, nplayersLabel);
		layout.putConstraint(SpringLayout.NORTH, showIPLabel, 10, SpringLayout.SOUTH, nplayersField);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, showIPLabel, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connectButton, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, connectButton, 10, SpringLayout.SOUTH, showIPLabel);
	}
	
	// listener della checkbox server
	@Override
	public void stateChanged(ChangeEvent e) {

		if (serverCheckBox.isSelected()) {
			nplayersLabel.setVisible(true);
			nplayersField.setVisible(true);
			ipField.setEnabled(false);
			showIPLabel.setVisible(true);

			try {
				showIPLabel.setText("Il tuo IP è: " + NetworkUtility.getInstance().getHostAddress());
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
		} else {
			nplayersLabel.setVisible(false);
			nplayersField.setVisible(false);
			ipField.setEnabled(true);
			showIPLabel.setVisible(false);
			showIPLabel.setText("");
		}
	}
	
	// listener del button di connessione
	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			setUsername(usernameField.getText());
			if (serverCheckBox.isSelected()) {
				setServerIP("SERVER");
				setNPlayers(Integer.parseInt(nplayersField.getText()));
			} else {
				setServerIP(ipField.getText());
			}
			StartPlay.connect(this);
			WindowUtility.closeWindow(jFrame);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (AlreadyBoundException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ServerNotActiveException e1) {
			e1.printStackTrace();
		}
	}
	
	// GETTERS e SETTERS
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public String getServerIP() {
		return this.serverIP;
	}

	public void setNPlayers(int nPlayers) {
		this.nPlayers = nPlayers;
	}

	public int getNPlayers() {
		return this.nPlayers;
	}

}
