import fr.chsn.hostpingchecker.DynamicObjectModel;
import fr.chsn.hostpingchecker.HostItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicObjectModelTest {

	static DynamicObjectModel dom;
	static List<HostItem> testItemList = Collections.synchronizedList(new ArrayList<>());

	@BeforeAll
	static void init() throws UnknownHostException {
		dom = new DynamicObjectModel();

		testItemList.add(new HostItem("test", "127.0.0.2", ""));
	}

	@Test
	@Order(1)
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
	@Order(2)
	void testRemoveItem() {
		dom.getHostList().remove(0);

		assertEquals(0, dom.getHostList().size());
	}

	@Test
	@Order(3)
	void testRemoveAllItem() {
		dom.removeAllItems();

		assertEquals(0, dom.getHostList().size());
	}


}