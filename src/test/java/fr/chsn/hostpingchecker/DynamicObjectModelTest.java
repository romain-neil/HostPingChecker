package fr.chsn.hostpingchecker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.ImageIcon;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DynamicObjectModelTest {

	static DynamicObjectModel dom;
	static List<HostItem> testItemList = Collections.synchronizedList(new ArrayList<>());

	@BeforeEach
	void initBeforeEach() throws UnknownHostException {
		HostItem item = new HostItem("test", "127.0.0.2", "");

		dom = new DynamicObjectModel();

		dom.addItem(item);
		testItemList.add(item);
	}

	@Test
	void testAddItem() throws IOException {
		int initialSize = testItemList.size();
		int domHostListSize = dom.getHostList().size();

		HostItem item = new HostItem("fbx", "192.168.0.2", "");

		dom.addItem(item);
		testItemList.add(item);

		assertEquals(domHostListSize + 1, dom.getHostList().size());
		assertEquals(initialSize + 1, testItemList.size());
	}

	@Test
	void testRemoveItem() {
		dom.removeItem(0);

		assertEquals(0, dom.getHostList().size());
	}

	@Test
	void testRemoveAllItem() {
		dom.removeAllItems();

		assertEquals(0, dom.getHostList().size());
	}

	@Test
	void testGetObjectValueAt() {
		dom.setList(testItemList);

		assertEquals(String.class, dom.getValueAt(0, 0).getClass());
		assertEquals(ImageIcon.class, dom.getValueAt(0, 2).getClass());

		assertNull(dom.getValueAt(0, 3));
	}

	@Test
	void testGetColumnClass() {
		assertEquals(String.class, dom.getColumnClass(0));
		assertEquals(String.class, dom.getColumnClass(1));
		assertEquals(ImageIcon.class, dom.getColumnClass(2));

		assertEquals(Object.class, dom.getColumnClass(-1)); //Cover default switch case as columnIndex >= 0
	}

	@Test
	void testCellIsEditable() {
		assertTrue(dom.isCellEditable(0, 0));
		assertTrue(dom.isCellEditable(1, 0));

		assertFalse(dom.isCellEditable(0, 1));
		assertFalse(dom.isCellEditable(1, 1));
	}

	@Test
	void testRefreshBadIpThrow() {
		assertDoesNotThrow(() -> dom.refreshIPs());

		HostItem invalidHost;

		try {
			invalidHost = new HostItem("invalid host", "255.255.255.255", "");
			dom.addItem(invalidHost);
		} catch (Exception e) {
			if(e instanceof UnknownHostException) {
				fail("The method should not perform a dns request");
			}

			fail(e.getMessage());
		}

	}

}