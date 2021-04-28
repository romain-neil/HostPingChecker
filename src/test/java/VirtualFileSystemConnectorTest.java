import fr.chsn.hostpingchecker.utils.connector.VirtualFileSystemConnector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VirtualFileSystemConnectorTest {

	static VirtualFileSystemConnector vFsConnector;

	@BeforeAll
	static void init() {
		vFsConnector = new VirtualFileSystemConnector();
	}

	@Test
	void testConstructObj() {
		Assertions.assertDoesNotThrow(() -> vFsConnector.read("/dev/null"));
		assertNull(vFsConnector.read(""));
	}

	@Test
	void testWriteDoesNotThrow() {
		assertDoesNotThrow(() -> vFsConnector.write("", ""));
	}

	@Test
	void testCloseDoesNotThrow() {
		assertDoesNotThrow(() -> vFsConnector.close());
	}

}
