package fr.chsn.hostpingchecker.tasks;

import fr.chsn.hostpingchecker.DynamicObjectModel;
import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.MainWindow;
import fr.chsn.hostpingchecker.utils.HostStatusUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class VerifyHostTask extends TimerTask {

	private final DynamicObjectModel model;
	private final MainWindow parent;

	private static final Logger logger = LogManager.getLogger("VerifyHostTask");

	public VerifyHostTask(MainWindow parent) {
		this.model = parent.getModel();
		this.parent = parent;
	}

	@Override
	public void run() {
		List<HostItem> machines = model.getHostList();

		List<HostItem> downHosts = new ArrayList<>();

		//Run task here, i.e. verify host reachability
		for (HostItem host : machines) {
			try {
				if (!host.isReachable()) {
					logger.info("Host {} is not reachable", host.getHostName());

					//Si le dernier status n'état pas Bad, alors cet hôte est devenu injoingable
					if(host.getStatus() != HostStatusUtil.Status.BAD) {
						downHosts.add(host);
						host.setStatus(HostStatusUtil.Status.BAD);

						System.out.println("New host down : " + host.getHostName());
					}
				} else {
					//La cible est atteignable
					host.setStatus(HostStatusUtil.Status.OK);
				}
			} catch (IOException e) {
				logger.error("Error during test accessibility of host {} : " + e.getMessage(), host.getHostName());
				e.printStackTrace();
			}
		}

		//On envoi l'alerte par mail
		if(parent.isMailSendEnabled() && !downHosts.isEmpty()) {
			parent.sendMail(downHosts);
			System.out.println("On envoi un mail");
		}

		model.fireTableDataChanged();
	}

}
