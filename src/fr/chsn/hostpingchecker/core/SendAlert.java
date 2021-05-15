package fr.chsn.hostpingchecker.core;

import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.MainWindow;
import fr.chsn.hostpingchecker.utils.PreferencesManager;
import fr.chsn.hostpingchecker.utils.ResourceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

	public boolean send() {
		boolean result = false;

		try {
			Properties prop = System.getProperties();
			prop.put("mail.smtp.host", InetAddress.getByName(prefManager.getString("smtp_addr", "")));
			prop.put("mail.smtp.port", prefManager.getInt("smtp_port", 25));
			prop.put("mail.smtp.ssl.enable", prefManager.getBool("smtp_use_ssl", false));
			prop.put("mail.smtp.auth", "true");

			Session session = Session.getDefaultInstance(prop, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(prefManager.getString("smtp_user", ""), prefManager.getString("smtp_pass", ""));
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(prefManager.getString("smtp_sender", "")));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(prefManager.getString("smtp_dest", "")));
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
		}

		return result;
	}

}
