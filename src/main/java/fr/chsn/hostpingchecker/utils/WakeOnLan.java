package fr.chsn.hostpingchecker.utils;

import java.io.IOException;
import java.net.*;

/**
 * Wake On Lan
 * @author jibble.org
 * @since 1.12.3
 */
public class WakeOnLan {

	public static final int PORT = 9;

	/**
	 * Get the mac address as bytes array
	 * @param MAC the mac address
	 * @return a byte array representing the mac address
	 */
	public static byte[] getMACBytes(String MAC) {
		byte[] bytes = new byte[6];
		String[] hex = MAC.split("([:\\-])");

		if(hex.length != 6) {
			throw new IllegalArgumentException("Invalid MAC Address");
		}

		try {
			for(int i = 0; i < 6; i++) {
				bytes[i] = (byte) Integer.parseInt(hex[i], 16);
			}
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("Invalid hex digit in MAC address");
		}

		return bytes;
	}

	/**
	 * Send WOL packet to host
	 * @author Romain Neil
	 * @param mac the host mac address
	 * @param ip the host ip
	 */
	public static void sendToHost(byte[] mac, String ip) throws IOException {
		sendToHost(InetAddress.getByName(ip), mac);
	}

	public static void sendToHost(String mac, String ip) throws IOException {
		sendToHost(getMACBytes(mac), ip);
	}

	protected static void sendToHost(InetAddress host, byte[] mac) throws IOException {
		DatagramPacket packet = new DatagramPacket(mac, mac.length, host, PORT);
		DatagramSocket socket = new DatagramSocket();

		socket.send(packet);
		socket.close();
	}

}
