import fr.chsn.hostpingchecker.utils.connector.VirtualFileSystemConnector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class VirtualFileSystemConnectorTest {

	@Test
	void testConstructObj() {
		VirtualFileSystemConnector connector = new VirtualFileSystemConnector();

		Assertions.assertDoesNotThrow(() -> connector.read("/dev/null"));
		assertNull(connector.read(""));
	}

}
