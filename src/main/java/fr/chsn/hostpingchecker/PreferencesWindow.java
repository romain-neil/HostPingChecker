package fr.chsn.hostpingchecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Romain Neil
 * @since 1.9.6
 */
public class PreferencesWindow extends JFrame {

	JCheckBox boxSSLEnabled;
	JCheckBox boxEnableSendMail;
	JCheckBox boxEnableWOL;

	JTextField smtpAddress;
	JTextField smtpUsername;
	JTextField sender;
	JTextField dest;

	JPasswordField smtpPass;

	JSpinner spinnerInterval;
	JSpinner spinnerPort;

	public PreferencesWindow(MainWindow parent) {

		ActionListener savePreferences = e -> {
			//TODO: try to make this functionnal with array of parameter
			parent.getPrefManager()
				.setString("smtp_addr", smtpAddress.getText())
				.setString("smtp_username", smtpUsername.getText())
				.setString("smtp_sender", sender.getText())
				.setString("smtp_dest", dest.getText())
				.setInt("smtp_port", Integer.parseInt(spinnerPort.getValue().toString()))
				.setBool("smtp_use_ssl", boxSSLEnabled.isSelected())
				.setInt("interval", Integer.parseInt(spinnerInterval.getValue().toString()))
				.setBool("enableMailSending", boxEnableSendMail.isSelected())
				.setBool("enableWOL", boxEnableWOL.isSelected());

			parent.getPrefManager().setString("smtp_pass", new String(smtpPass.getPassword()));

			parent.loadPreferences();

			this.dispose();
		};

		JPanel panel = new JPanel();

		JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;

		/* Onglet préférences du serveur smtp */
		JPanel ongletPrefsSmtp = new JPanel(new GridBagLayout());

		JLabel lblServerIp = new JLabel("Adresse du serveur smtp :");
		c.gridx = 0;
		c.gridy = 0;
		ongletPrefsSmtp.add(lblServerIp, c);

		smtpAddress = new JTextField(10);
		smtpAddress.setText(parent.getPrefManager().getString("smtp_addr", ""));
		c.gridx = 1;
		c.gridy = 0;
		//c.weightx = 1;
		ongletPrefsSmtp.add(smtpAddress, c);

		JLabel lblSmtpUser = new JLabel("Utilisateur smtp :");
		c.gridx = 0;
		c.gridy = 1;
		//c.weightx = 0;
		ongletPrefsSmtp.add(lblSmtpUser, c);

		smtpUsername = new JTextField(10);
		smtpUsername.setText(parent.getPrefManager().getString("smtp_username", ""));
		c.gridx = 1;
		c.gridy = 1;
		//c.weightx = 1;
		ongletPrefsSmtp.add(smtpUsername, c);

		JLabel lblPassword = new JLabel("Mot de passe :");
		c.gridx = 0;
		c.gridy = 2;
		//c.weightx = 0;
		ongletPrefsSmtp.add(lblPassword, c);

		smtpPass = new JPasswordField(parent.getPrefManager().getString("smtp_pass", ""));
		c.gridx = 1;
		c.gridy = 2;
		//c.weightx = 1;
		ongletPrefsSmtp.add(smtpPass, c);

		JLabel lblSender = new JLabel("Expéditeur :");
		c.gridx = 0;
		c.gridy = 3;
		//c.weightx = 0;
		ongletPrefsSmtp.add(lblSender, c);

		sender = new JTextField(10);
		sender.setToolTipText("Expéditeur du mail d'alerte");
		sender.setText(parent.getPrefManager().getString("smtp_sender", ""));
		c.gridx = 1;
		c.gridy = 3;
		//c.weightx = 1;
		ongletPrefsSmtp.add(sender, c);

		JLabel lblDest = new JLabel("Destinataire :");
		c.gridx = 0;
		c.gridy = 4;
		//c.weightx = 0;
		ongletPrefsSmtp.add(lblDest, c);

		dest = new JTextField(10);
		dest.setToolTipText("Destinataire du mail d'alerte");
		dest.setText(parent.getPrefManager().getString("smtp_dest", ""));
		c.gridx = 1;
		c.gridy = 4;
		//c.weightx = 1;
		ongletPrefsSmtp.add(dest, c);

		JLabel lblSmtpPort = new JLabel("Port du serveur smtp :");
		c.gridx = 0;
		c.gridy = 5;
		ongletPrefsSmtp.add(lblSmtpPort, c);

		spinnerPort = new JSpinner(new SpinnerNumberModel(parent.getPrefManager().getInt("smtp_port", 25), 1, 65535, 1));
		c.gridx = 1;
		c.gridy = 5;
		ongletPrefsSmtp.add(spinnerPort, c);

		boxSSLEnabled = new JCheckBox("Connexion SSL");
		boxSSLEnabled.setSelected(parent.getPrefManager().getBool("smtp_use_ssl", false));
		c.gridx = 0;
		c.gridy = 6;
		ongletPrefsSmtp.add(boxSSLEnabled, c);

		boxEnableSendMail = new JCheckBox("Envoyer un mail de rapport");
		boxEnableSendMail.setSelected(parent.getPrefManager().getBool("enableMailSending", false));
		c.gridx = 0;
		c.gridy = 7;
		ongletPrefsSmtp.add(boxEnableSendMail);

		ongletPrefsSmtp.setPreferredSize(new Dimension(300, 135));
		onglets.addTab("SMTP", ongletPrefsSmtp);

		/* Onglet des paramètres divers */
		JPanel ongletMisc = new JPanel(new GridBagLayout());

		JLabel lblInterval = new JLabel("Intervale de ping (en secondes) :");
		c.gridx = 0;
		c.gridy = 0;
		ongletMisc.add(lblInterval, c);

		spinnerInterval = new JSpinner(new SpinnerNumberModel(parent.getInterval(), 10, 3600, 1));
		c.gridx = 1;
		c.gridy = 0;
		ongletMisc.add(spinnerInterval);

		boxEnableWOL = new JCheckBox("Activer le Wake-On-Lan");
		boxEnableWOL.setSelected(parent.getPrefManager().getBool("enableWOL", false));
		c.gridx = 0;
		c.gridy = 1;
		ongletMisc.add(boxEnableWOL);

		onglets.addTab("Divers", ongletMisc);

		onglets.setOpaque(true);
		panel.add(onglets);

		JButton btnSavePrefs = new JButton("Sauvegarder");
		btnSavePrefs.addActionListener(savePreferences);
		panel.add(btnSavePrefs, BorderLayout.CENTER);

		setContentPane(panel);
		setSize(new Dimension(350, 250));
		setVisible(true);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

}
