package fr.chsn.hostpingchecker.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ImageUtilTest {

	@Test
	void testCreateImageIcon() {
		assertNull(ImageUtil.createImageIcon(this, null));
	}

	@Test
	void testCreateImageIconWithNonExistentFile() {
		assertNull(ImageUtil.createImageIcon(this, "DoesNotExist"));
	}

	@Test
	@Disabled
	void testGetScaledImage() {}

}
