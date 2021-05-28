package fr.chsn.hostpingchecker.utils.connector;

import fr.chsn.hostpingchecker.utils.connector.VirtualFileSystemConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class VirtualFileSystemConnectorTest {

	static VirtualFileSystemConnector vFsConnector;

	@BeforeAll
	static void init() {
		vFsConnector = new VirtualFileSystemConnector();
	}

	@AfterEach
	void raz() {
		vFsConnector.write("", "");
	}

	@Test
	void testConstructObj() {
		Assertions.assertDoesNotThrow(() -> vFsConnector.read("/dev/null"));
		assertEquals(vFsConnector.read(""), "");
	}

	@Test
	void testWriteDoesNotThrow() {
		assertDoesNotThrow(() -> vFsConnector.write("", ""));
	}

	@Test
	void testCloseDoesNotThrow() {
		assertDoesNotThrow(() -> vFsConnector.close());
	}

	@Test
	void testWriteContent() {
		byte[] array = new byte[7];
		new Random().nextBytes(array);
		String generatedString = new String(array, StandardCharsets.UTF_8);

		vFsConnector.write("test", generatedString);

		assertEquals(vFsConnector.read("test"), generatedString);
	}

}
