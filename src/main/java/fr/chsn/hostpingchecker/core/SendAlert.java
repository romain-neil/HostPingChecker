package fr.chsn.hostpingchecker.core;

import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.MainWindow;
import fr.chsn.hostpingchecker.utils.PreferencesManager;
import fr.chsn.hostpingchecker.utils.ResourceUtil;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

//TODO: handle smtp server authentication

/**
 * Classe Pour envoyer une alerte
 * @author Romain Neil
 * @since 1.10.3
 */
public class SendAlert {

	private final PreferencesManager prefManager;

	private final static Logger logger = LogManager.getLogger("SendAlert");

	private final List<HostItem> liste;

	private final MainWindow parent;

	public SendAlert(MainWindow parent, List<HostItem> itemList) {
		this.prefManager = parent.getPrefManager();
		this.parent = parent;

		this.liste = itemList;
	}

	public boolean send() throws MessagingException {
		boolean result = false;

		try {
			Properties prop = new Properties();
			prop.put("mail.smtp.host", InetAddress.getByName(prefManager.getString("smtp_addr", "")));
			prop.put("mail.smtp.port", prefManager.getInt("smtp_port", 25));
			prop.put("mail.smtp.ssl.enable", prefManager.getBool("smtp_use_ssl", false));

			Session session;

			if(!prefManager.getString("smtp_username", "").equals("")) {
				//Authentification required
				prop.put("mail.smtp.auth", "true");

				session = Session.getInstance(prop,
					new Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									prefManager.getString("smtp_username", ""),
									prefManager.getString("smtp_password", ""));
						}
					});
			} else {
				session = Session.getDefaultInstance(prop);
			}

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(prefManager.getString("smtp_sender", "")));
			message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(prefManager.getString("smtp_dest", ""))[0]);
			message.setSubject("Alerte monitoring");

			StringBuilder msg = new StringBuilder();
			msg.append(ResourceUtil.getResourceContent(parent, "/mails/alert_header.html"));

			for (HostItem host : liste) {
				msg.append("<li>").append(host.getHostName()).append(" -  ").append(host.getHostIP().getHostAddress()).append("</li>");
			}

			msg.append(ResourceUtil.getResourceContent(parent, "/mails/alert_footer.html"));

			message.setContent(msg.toString(), "text/html");
			message.setHeader("X-Mailer", "Java " + System.getProperty("java.version"));
			message.setSentDate(new Date());
			Transport.send(message);

			result = true;
		} catch (UnknownHostException e) {
			logger.warn("The smtp server seems to be down, cannot send alert");
			parent.getPrefManager().setString("smtp_addr", "");
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.fatal("Error while sending message : {}", e.getMessage());
			parent.getPrefManager().setString("smtp_addr", "");
		}

		return result;
	}

}
