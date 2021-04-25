package fr.chsn.hostpingchecker.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class WakeOnLanTest {

	@Test
	void testMacParsing() {
		String goodMAC = "00:0D:61:08:22:4A";

		assertDoesNotThrow(() -> {
			WakeOnLan.getMACBytes(goodMAC);
		});
	}

	@Test
	void testBadMacAddrThrow() {
		String badMAC = "JJ-KK-61-08-22-4A";
		String anotherBadMAC = "ABC-DCD-BGF";

		assertThrows(IllegalArgumentException.class, () -> WakeOnLan.getMACBytes(badMAC));
		assertThrows(IllegalArgumentException.class, () -> WakeOnLan.getMACBytes(anotherBadMAC));
	}

	@Test
	void testSendToBadHostThrow() {
		String badHost = "0.0.1";
		byte[] mac = WakeOnLan.getMACBytes("00:0D:61:08:22:4A");

		try {
			WakeOnLan.sendToHost(mac, badHost);
		} catch (IOException e) {
			if (e instanceof UnknownHostException) {
				fail();
			}
		}
	}

}