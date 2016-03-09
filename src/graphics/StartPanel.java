package graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.text.ParseException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import network.NetworkUtility;
import registration.Room;
import start.StartPlay;

public class StartPanel implements ChangeListener, ActionListener {

	/** GRANDEZZE FINESTRA DI IMPOSTAZIONI **/
	private final int WIDTH = 500, HEIGHT = 400;
	Integer[] N_PLAYER = { 1, 2, 3, 4, 5, 6 };
	/** ELEMENTI GRAFICI **/
	private JFrame jFrame;
	private JPanel panel;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel serverIpLabel;
	private JTextField ipField;
	private JCheckBox serverCheckBox;
	private JLabel nplayersLabel;
	private JLabel showIPLabel;
	private JButton connectButton;
	private JComboBox nPlayersSelect;
	private JLabel loading;
	private JLabel informColor;
	private JLabel playerColor;
	private JTextArea textAreaServer;
	/** VARIABILI GLOBALI UTILI PER COMINCIARE LA PARTITA **/
	private String serverIP;
	private String username;
	private int nPlayers;

	public StartPanel() throws ParseException {
		// Instanzazione degli elementi grafici della finestra
		jFrame = new JFrame("Impostazioni di Gioco");
		SpringLayout layout = new SpringLayout();
		panel = new JPanel(layout);
		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(10);
		serverIpLabel = new JLabel("SERVER IP:");
		ipField = new JTextField("130.136.4.0", 10);
		serverCheckBox = new JCheckBox("Sono il server");
		nplayersLabel = new JLabel("N. di giocatori:");
		nPlayersSelect = new JComboBox(N_PLAYER);
		showIPLabel = new JLabel();
		connectButton = new JButton("Connetti");
		loading = new JLabel("");
		informColor = new JLabel("IL MIO COLORE SARÀ: ");
		playerColor = new JLabel("        ");
		textAreaServer = new JTextArea(7,40);
		// impostazione degli elementi grafici
		jFrame.setSize(WIDTH, HEIGHT);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBackground(Color.LIGHT_GRAY);
		nplayersLabel.setVisible(false);
		nPlayersSelect.setVisible(false);
		showIPLabel.setVisible(false);
		loading.setVisible(true);
		informColor.setVisible(false);
		playerColor.setVisible(false);
		textAreaServer.setVisible(false);
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
		panel.add(nPlayersSelect);
		panel.add(showIPLabel);
		panel.add(connectButton);
		panel.add(loading);
		panel.add(informColor);
		panel.add(playerColor);
		panel.add(textAreaServer);
		jFrame.add(panel);

		WindowUtility.centerWindow(jFrame);
		jFrame.setResizable(false);
		jFrame.setVisible(true);
	}

	/**
	 * Metodo che si occupa di impostare il posizionamento degli elementi
	 * grafici nella finestra
	 * 
	 * @param layout
	 */
	private void setLayoutConstraints(SpringLayout layout) {

		layout.putConstraint(SpringLayout.WEST, usernameLabel, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, usernameLabel, 22, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, usernameField, 10, SpringLayout.EAST, serverIpLabel);
		layout.putConstraint(SpringLayout.NORTH, usernameField, 20, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, serverIpLabel, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, serverIpLabel, 32, SpringLayout.NORTH, usernameLabel);
		layout.putConstraint(SpringLayout.WEST, ipField, 10, SpringLayout.EAST, serverIpLabel);
		layout.putConstraint(SpringLayout.NORTH, ipField, 30, SpringLayout.NORTH, usernameField);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ipField, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, serverCheckBox, 10, SpringLayout.SOUTH, ipField);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, serverCheckBox, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, nplayersLabel, 12, SpringLayout.SOUTH, serverCheckBox);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, nplayersLabel, -80, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, nPlayersSelect, 10, SpringLayout.SOUTH, serverCheckBox);
		layout.putConstraint(SpringLayout.WEST, nPlayersSelect, 5, SpringLayout.EAST, nplayersLabel);
		layout.putConstraint(SpringLayout.NORTH, showIPLabel, 10, SpringLayout.SOUTH, nPlayersSelect);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, showIPLabel, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connectButton, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, connectButton, 10, SpringLayout.SOUTH, showIPLabel);
		layout.putConstraint(SpringLayout.WEST, informColor, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, informColor, 10, SpringLayout.SOUTH, connectButton);
		layout.putConstraint(SpringLayout.WEST, playerColor, 10, SpringLayout.EAST, informColor);
		layout.putConstraint(SpringLayout.NORTH, playerColor, 10, SpringLayout.SOUTH, connectButton);
		layout.putConstraint(SpringLayout.WEST, loading, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, loading, 22, SpringLayout.NORTH, informColor);
		layout.putConstraint(SpringLayout.WEST, textAreaServer, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, textAreaServer, 22, SpringLayout.NORTH, loading);
	}

	// listener della checkbox server
	@Override
	public void stateChanged(ChangeEvent e) {

		if (serverCheckBox.isSelected()) {
			nplayersLabel.setVisible(true);
			nPlayersSelect.setVisible(true);
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
			nPlayersSelect.setVisible(false);
			ipField.setEnabled(true);
			showIPLabel.setVisible(false);
			showIPLabel.setText("");
		}
	}

	// listener del button di connessione
	@Override
	public void actionPerformed(ActionEvent e) {

		if (checkErrors()) {
			connectButton.setEnabled(false);
			try {
				setUsername(usernameField.getText());
				if (serverCheckBox.isSelected()) {
					this.informColorPlayer(Color.red);
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								loading.setText("In attesa di giocatori ");
								for (int i = 1; i <= 10; i++){
									Thread.sleep(100);
									if(i == 10) {
										i = 0;
										loading.setText("In attesa di giocatori ");
									} else {
										String l = loading.getText();
										l = l + ".";
										loading.setText(l);
									}
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					      }
					    });
					    t.start();
					setServerIP("SERVER");
					setNPlayers(nPlayers);
				} else {
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								loading.setText("Attendere ");
								for (int i = 1; i <= 10; i++){
									Thread.sleep(100);
									if(i == 10) {
										i = 0;
										loading.setText("Attendere ");
									} else {
										String l = loading.getText();
										l = l + ".";
										loading.setText(l);
									}
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				    t.start();
					setServerIP(ipField.getText());
				}
				StartPlay.connect(this);
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
	}

	public void informColorPlayer(Color c) {
		informColor.setVisible(true);
		playerColor.setVisible(true);
		playerColor.setBackground(c);
		playerColor.setOpaque(true);
	}

	public void informServerHostRegistred(Room r) {
		textAreaServer.setVisible(true);
		textAreaServer.setEditable(false);
		textAreaServer.setText("NUMERO DI GIOCATORI REGISTRATI:     " + r.getCurrentPlayers() + " / " + r.getStartPlayers());
		for (int i=0; i<r.getPlayers().size();i++) {
			textAreaServer.append("\n" + r.getPlayers().get(i).getId() + ")" + r.getPlayers().get(i).getUsername() + "  -  " + r.getPlayers().get(i).getHost().getIP());
		}
	}

	private boolean checkErrors() {
		boolean ok = true;
		String errorText = "";
		String username = usernameField.getText();
		String ip = ipField.getText();

		if (username.isEmpty()) {
			errorText += "Inserire l'username\n";
			ok = false;
		}

		if (serverCheckBox.isSelected()) {
			nPlayers = Integer.parseInt(nPlayersSelect.getSelectedItem().toString());
		} else {
			if (ip.isEmpty()) {
				errorText += "Inserire un l'IP del SERVER\n";
				ok = false;
			} else {
				String[] st = ip.split("\\.");
				boolean ipIncorrect = false;
				if (st.length < 4) {
					ipIncorrect = true;
				} else {
					for (int i = 0; i < st.length; i++) {
						try {
							int value = Integer.parseInt(st[i]);
							if (value < 0 || value > 255) {
								ipIncorrect = true;
							}
						} catch (NumberFormatException e1) {
							ipIncorrect = true;
						}
					}
				}
				
				if (ipIncorrect) {
					errorText += "Inserire un IP valido!\n";
					ok = false;
				}
			}
		}

		if (!ok) {
			JOptionPane.showMessageDialog(new JFrame(), errorText, "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
		}

		return ok;
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

	public JFrame getJFrame() {
		return this.jFrame;
	}
}
