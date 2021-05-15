import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.utils.HostStatusUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class HostItemTest {

	static HostItem testItem;

	@BeforeEach
	void initBeforeEach() throws UnknownHostException {
		testItem = new HostItem("test", "127.0.0.1", "");
	}

	@Test
	void getHostName() {
		assertFalse(testItem.getHostName().isEmpty());
	}

	@Test
	void setHostName() {
		String newHostName = "blabla";

		testItem.setHostName(newHostName);

		assertEquals(newHostName, testItem.getHostName());
	}

	@Test
	void getHostIP() {
		assertFalse(testItem.getHostIP().toString().isEmpty());
	}

	@Test
	void setHostIP() throws UnknownHostException {
		String newIP = "127.2.5.1";

		testItem.setHostIP(InetAddress.getByName(newIP));

		assertEquals(InetAddress.getByName(newIP), testItem.getHostIP());
	}

	@Test
	void testSetHostMAC() {
		String newMAC = "aa:bb";

		testItem.setMACAddress(newMAC);

		assertEquals(newMAC, testItem.getMACAddress());
	}

	@Test
	void testGetHostMAC() {
		assertEquals("", testItem.getMACAddress());
	}

	@Test
	void testSetStatus() {
		HostStatusUtil.Status newStatus = HostStatusUtil.Status.OK;

		testItem.setStatus(newStatus);

		assertEquals(testItem.getStatus(), newStatus);
	}

	@Test
	void testGetStatus() {
		assertEquals(testItem.getStatus(), HostStatusUtil.Status.UNKNOWN);
	}

	@Test
	void testGetDnsHostname() {
		assertFalse(testItem.getDNSHostName().isEmpty());
	}

	@Test
	void testHostIsReachable() {
		assertDoesNotThrow(() -> {
			testItem.isReachable();
		});
	}

}