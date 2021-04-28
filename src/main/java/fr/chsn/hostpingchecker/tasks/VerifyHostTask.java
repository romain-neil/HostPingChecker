package fr.chsn.hostpingchecker.tasks;

import fr.chsn.hostpingchecker.DynamicObjectModel;
import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.MainWindow;
import fr.chsn.hostpingchecker.utils.HostStatusUtil;
import fr.chsn.hostpingchecker.utils.WakeOnLan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

public class VerifyHostTask extends TimerTask {

	private final DynamicObjectModel model;
	private final MainWindow parent;

	private static final Logger logger = LogManager.getLogger("VerifyHostTask");

	private static final List<HostItem> downHostsList = Collections.synchronizedList(new ArrayList<>());

	public VerifyHostTask(MainWindow parent) {
		this.model = parent.getModel();
		this.parent = parent;
	}

	/**
	 * Update the list of host who are down
	 * @throws IOException if the application cannot perform a ping to a target host
	 * @since 1.12.2
	 */
	private void updateDownHostList() throws IOException {
		if(downHostsList.size() != 0) {
			for(HostItem host : downHostsList) { //For each down machine
				HostStatusUtil.Status hostStatus = host.getStatus();

				//If the host was previously reachable
				if(hostStatus != HostStatusUtil.Status.BAD) {
					if(!host.isReachable()) { //If the host is down
						downHostsList.add(host);
						host.setStatus(HostStatusUtil.Status.BAD);

						System.out.println("New host down : " + host.getHostName());
					} else {
						//The host is reachable
						downHostsList.remove(host);
						host.setStatus(HostStatusUtil.Status.OK);
					}
				}
			}
		}
	}

	private void sendWOLToHosts() throws IOException {
		if(downHostsList.size() != 0) {
			for(HostItem host : downHostsList) {
				if(!host.getHostIP().toString().isEmpty() && !host.getMACAddress().isEmpty()) {
					WakeOnLan.sendToHost(host.getMACAddress(), host.getHostIP().toString());
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			updateDownHostList();
		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("Unable to update the list of down list");
		}

		//On envoi l'alerte par mail
		if(parent.isMailSendEnabled() && !downHostsList.isEmpty()) {
			parent.sendMail(downHostsList);
			System.out.println("On envoi un mail");
		}

		if (parent.isWOLEnabled()) {
			try {
				sendWOLToHosts();
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("An error occured when sending WOL to host");
			}
		}

		model.fireTableDataChanged();
	}

}
