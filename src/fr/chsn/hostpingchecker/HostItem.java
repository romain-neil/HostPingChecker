package fr.chsn.hostpingchecker;

import fr.chsn.hostpingchecker.utils.HostStatusUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Classe qui définie un item d'une machine
 * @author Romain Neil
 * @since 1.0.0
 */
public class HostItem {

	/**
	 * Nom de la machine
	 */
	private String m_hostName;

	/**
	 * Nom dns de la machine
	 */
	private final String m_dnsHostName;

	/**
	 * Adresse IP de la machine
	 */
	private InetAddress m_hostIP;

	/**
	 * Dernier état de la machine
	 */
	private HostStatusUtil.Status m_hostStatus;

	public HostItem(String name, String address) throws UnknownHostException {
		m_hostName = name;
		m_hostIP = InetAddress.getByName(address);
		m_dnsHostName = address;
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

	public String getDNSHostName() {
		return m_dnsHostName;
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
	 * Set the new ip address of the host
	 * @param addr new ip address of the host
	 */
	public void setHostIP(InetAddress addr) {
		m_hostIP = addr;
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
