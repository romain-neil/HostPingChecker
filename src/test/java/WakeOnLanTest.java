import fr.chsn.hostpingchecker.utils.WakeOnLan;
import org.junit.jupiter.api.Test;

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
		String badHost = "domain-not-valid";
		byte[] mac = WakeOnLan.getMACBytes("00:0D:61:08:22:4A");

		assertThrows(UnknownHostException.class, () -> WakeOnLan.sendToHost(mac, badHost));
	}

}