package fr.chsn.hostpingchecker;

import fr.chsn.hostpingchecker.actions.LoadListAction;
import fr.chsn.hostpingchecker.actions.OpenPrefsAction;
import fr.chsn.hostpingchecker.actions.SaveListAction;
import fr.chsn.hostpingchecker.actions.ShowHelp;
import fr.chsn.hostpingchecker.core.SendAlert;
import fr.chsn.hostpingchecker.tasks.VerifyHostTask;
import fr.chsn.hostpingchecker.utils.ImageUtil;
import fr.chsn.hostpingchecker.utils.PreferencesManager;
import jakarta.mail.MessagingException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.TimerTask;

public class MainWindow extends JFrame implements KeyListener {

	private final PreferencesManager prefManager;

	ImageIcon iconStart;
	ImageIcon iconStop;

	private int interval;

	private boolean mailSendingEnabled = false;
	private boolean wolEnabled = false;

	private final JButton btnAdd = new JButton("Ajouter");

	private final JTable table;

	private final JButton btnStart;

	private final JLabel lblStatus;

	private final JTextField nomMachine = new JTextField(10);
	private final JTextField adresseIP = new JTextField(10);
	private final JTextField adresseMAC = new JTextField(10);

	private final DynamicObjectModel model = new DynamicObjectModel();

	private java.util.Timer t;

	public void loadPreferences() {
		interval = prefManager.getInt("interval", 30);
	}

	public MainWindow() {
		prefManager = new PreferencesManager();

		loadPreferences();

		iconStart = ImageUtil.getScaledImage(ImageUtil.createImageIcon(this, "/UI/icons/start.png"), 32, 32);
		iconStop = ImageUtil.getScaledImage(ImageUtil.createImageIcon(this, "/UI/icons/stop.png"), 32, 32);

		btnStart = new JButton(iconStart);
		JButton btnStop = new JButton(iconStop);
		btnStop.setEnabled(false);

		JButton btnRefreshIP = new JButton("Rafraichir les ip");

		lblStatus = new JLabel("");

		JMenuBar menuBar = getMenu();

		setSize(500, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Vérification du ping de machines");

		table = new JTable(model);

		JScrollPane jPane = new JScrollPane(table);

		JLabel labelHostName = new JLabel("Nom de la machine :");
		JLabel labelHostAddress = new JLabel("Adresse IP de la machine :");
		JLabel labelHostMAC = new JLabel("Adresse MAC de la machine :");

		/* Panneau du formulaire */
		JPanel formPanel = new JPanel();
		//formPanel.setLayout(new GridLayout(3, 2));
		formPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		formPanel.add(labelHostName, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 2.0;
		c.gridx = 1;
		c.gridy = 0;
		formPanel.add(nomMachine, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		formPanel.add(labelHostAddress, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 2.0;
		c.gridx = 1;
		c.gridy = 1;
		formPanel.add(adresseIP, c);

		c.gridx = 0;
		c.gridy = 2;
		formPanel.add(labelHostMAC, c);

		c.weightx = 2.0;
		c.gridx = 1;
		c.gridy = 2;
		formPanel.add(adresseMAC, c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 3;
		formPanel.add(btnAdd, c);

		/* Panneau des boutons */
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new BorderLayout());

		panelButtons.add(btnStart, BorderLayout.WEST);
		panelButtons.add(btnStop, BorderLayout.EAST);
		panelButtons.add(btnRefreshIP, BorderLayout.SOUTH);

		/* Barre de status */
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(lblStatus);

		/* Panneau principal */
		JPanel mainPanel = new JPanel();
		mainPanel.add(formPanel);
		mainPanel.add(panelButtons);
		mainPanel.add(jPane, BorderLayout.CENTER);
		mainPanel.add(statusPanel, BorderLayout.CENTER);

		setContentPane(mainPanel);
		setJMenuBar(menuBar);

		btnAdd.addActionListener((ActionEvent e) -> {
			btnAdd.setEnabled(false);

			try {
				String ip = adresseIP.getText();
				String machine = nomMachine.getText();
				String mac = adresseMAC.getText();

				if(!ip.isEmpty() && !machine.isEmpty()) {
					model.addItem(new HostItem(machine, ip ,mac));
				}
			} catch (UnknownHostException unknownHostException) {
				unknownHostException.printStackTrace();
				JOptionPane.showMessageDialog(this, "L'hôte est inconnu", "Erreur", JOptionPane.ERROR_MESSAGE);
			}

			SwingUtilities.invokeLater(new Thread(() -> {
				nomMachine.setText("");
				adresseIP.setText("");

				btnAdd.setEnabled(true);
			}));
		});

		table.addKeyListener(this);

		btnStart.addActionListener((ActionEvent e) -> {
			if(model.getRowCount() != 0) {
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				enableInputs(false);

				mailSendingEnabled = (!prefManager.getString("smtp_addr", "").equals(""));
				wolEnabled = (prefManager.getBool("enableWOL", false));

				VerifyHostTask verifyHostTask = new VerifyHostTask(this);

				t = new java.util.Timer();

				t.scheduleAtFixedRate(verifyHostTask, 1000, (interval * 1000L));
			}
		});

		btnStop.addActionListener((ActionEvent e) -> {
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);

			t.cancel(); //On annule toutes les tâches
			enableInputs(true);
		});

		btnRefreshIP.addActionListener((ActionEvent e) -> {
			if(model.getRowCount() != 0) {
				btnRefreshIP.setEnabled(false);

				try {
					model.refreshIPs();
				} catch (UnknownHostException unknownHostException) {
					unknownHostException.printStackTrace();

					JOptionPane.showMessageDialog(this, "Erreur", "La mise a jour de l'adresse ip a échoué sur une machine", JOptionPane.ERROR_MESSAGE);
				}

				btnRefreshIP.setEnabled(true);
			}
		});
	}

	private JMenuBar getMenu() {
		JMenuBar menuBar = new JMenuBar();

		//Création des menus
		JMenu menuFile = new JMenu("Fichier");
		JMenu menuPrefs = new JMenu("Paramètres");
		JMenu menuHelp = new JMenu("Aide");

		JMenuItem loadList = new JMenuItem(new LoadListAction(this, "Charger une liste"));
		JMenuItem saveList = new JMenuItem(new SaveListAction(this, "Sauvegarder la liste"));

		menuFile.add(loadList);
		menuFile.add(saveList);

		JMenuItem showPrefs = new JMenuItem(new OpenPrefsAction(this, "Editer les préférences"));

		menuPrefs.add(showPrefs);

		JMenuItem showHelp = new JMenuItem(new ShowHelp(this, "A propos"));

		menuHelp.add(showHelp);

		menuBar.add(menuFile);
		menuBar.add(menuPrefs);
		menuBar.add(menuHelp);

		return menuBar;
	}

	/**
	 * Défini le status avec le temps par défaut
	 * @param text texte à afficher dans la barre de status
	 * @since 1.10.3
	 */
	public void setStatus(String text) {
		setStatus(text, 5);
	}

	/**
	 * Défini le status avec le temps donné
	 * @param text texte à afficher dans la barre de status
	 * @param seconds nombre de secondes pendant lequelles afficher le message
	 */
	public void setStatus(String text, int seconds) {
		lblStatus.setText(text);

		new java.util.Timer().schedule(
				new TimerTask() {
					@Override
					public void run() {
						lblStatus.setText("");
					}
				},
				seconds * 1000L
		);
	}

	/**
	 * Send an alert email
	 * @param liste la liste des machines qui ne répondent pas
	 * @since 1.10.3
	 */
	public void sendMail(java.util.List<HostItem> liste) {
		SendAlert alerte = new SendAlert(this, liste);

		try {
			if(!alerte.send()) {//Silent warning
				setStatus("Error: unable to send alert mail at " + new Date() + " !");
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Is mail sending is enabled ?
	 * @return true if a smtp server address is know by the application
	 * @since 1.10.3
	 */
	public boolean isMailSendEnabled() {
		return mailSendingEnabled;
	}

	public boolean isWOLEnabled() {
		return wolEnabled;
	}

	/**
	 * Permet ou non d'utiliser les différents champs textes
	 * @param val Activer les champs ou non
	 */
	private void enableInputs(boolean val) {
		nomMachine.setEnabled(val);
		adresseIP.setEnabled(val);
		btnAdd.setEnabled(val);
	}

	public DynamicObjectModel getModel() {
		return model;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DELETE && table.getSelectedRow() != -1) {
			model.removeItem(table.getSelectedRow());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	public int getInterval() {
		return interval;
	}

	public PreferencesManager getPrefManager() {
		return prefManager;
	}
}
