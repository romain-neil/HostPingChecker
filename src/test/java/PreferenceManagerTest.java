import fr.chsn.hostpingchecker.utils.PreferencesManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PreferenceManagerTest {

	public static PreferencesManager preferencesManager;

	public static final String PARAM_NAME = "param_test";

	@BeforeAll
	static void init() {
		preferencesManager = new PreferencesManager();
		preferencesManager.setNode("tests");
	}

	@Test
	void testPrefManagerReturnChainedCall() {
		assertEquals(
				PreferencesManager.class,
				preferencesManager.setBool(PARAM_NAME, false).getClass()
		);
	}

	@Test
	void testIntBehaviour() {
		String key = String.join(PARAM_NAME, "_int");

		assertEquals(0, preferencesManager.getInt(key, 0));

		preferencesManager.setInt(key, 42);

		assertEquals(
				42,
				preferencesManager.getInt(key, 0)
		);

		preferencesManager.removeKey(key);
	}

	@Test
	void testBoolBehaviour() {
		String key = String.join(PARAM_NAME, "_bool");

		assertFalse(preferencesManager.getBool(key, false));

		preferencesManager.setBool(key, true);

		assertTrue(preferencesManager.getBool(key, false));

		preferencesManager.removeKey(key);
	}

	@Test
	void testStringBehaviour() {
		String key = String.join(PARAM_NAME, "_string");
		String expected = "bla";

		assertEquals(
				preferencesManager.getString(key, ""),
				""
		);

		preferencesManager.setString(key, expected);

		assertEquals(
				expected,
				preferencesManager.getString(key, "")
		);

		preferencesManager.removeKey(key);
	}

}
