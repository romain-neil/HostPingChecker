package fr.chsn.hostpingchecker;

import fr.chsn.hostpingchecker.utils.HostStatusUtil;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * Classe qui définie un item d'une machine
 * @author Romain Neil
 * @since 1.0.0
 */
public class HostItem implements Serializable {

	/**
	 * Nom de la machine
	 */
	private String m_hostName;

	/**
	 * Adresse IP de la machine
	 */
	private final InetAddress m_hostIP;

	/**
	 * Dernier état de la machine
	 */
	private HostStatusUtil.Status m_hostStatus;

	public HostItem(String name, InetAddress address) {
		m_hostName = name;
		m_hostIP = address;
		m_hostStatus = HostStatusUtil.Status.UNKNOWN;
	}

	/**
	 * Retourne vrai si l'hôte est accessible en moins de 3 secondes, faux sinon
	 * @return vrai si l'hôte est accessible
	 * @throws IOException si une erreur réseau survient
	 */
	public boolean isReachable() throws IOException {
		return m_hostIP.isReachable(3000);
	}

	public String getHostName() {
		return m_hostName;
	}

	public void setHostName(String newName) {
		m_hostName = newName;
	}

	/**
	 * Retourne l'adresse de la machine de la machine
	 * @return un object InetAddr
	 */
	public InetAddress getHostIP() {
		return m_hostIP;
	}

	/**
	 * Définit l'état de la machine
	 * @param status dernier état de la machine
	 * @since 1.6.8
	 */
	public void setStatus(HostStatusUtil.Status status) {
		m_hostStatus = status;
	}

	/**
	 * Retourne le dernier état de la machine
	 * @return Le dernier état connu de la machine
	 * @since 1.6.8
	 */
	public HostStatusUtil.Status getStatus() {
		return m_hostStatus;
	}

}
