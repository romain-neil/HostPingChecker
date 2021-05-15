package fr.chsn.hostpingchecker.utils;

import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.utils.connector.VirtualFileSystemConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class HostListUtilTest {

	static VirtualFileSystemConnector fsConnector;

	@BeforeEach
	void init() {
		fsConnector = new VirtualFileSystemConnector();
	}

	@Test
	void testSaveHostList() {
		List<HostItem> list = new ArrayList<>();
		//HostListUtil.save(list, fsConnector);
	}

	@Test
	void testLoadHostList() {}

}
