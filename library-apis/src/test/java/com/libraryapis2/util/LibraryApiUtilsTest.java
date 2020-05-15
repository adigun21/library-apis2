package com.libraryapis2.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LibraryApiUtilsTest {


	@Test
	public void doesStringValueExist() {
		
		assertTrue(LibraryApiUtils.doesStringValueExist("valueExist"));
		assertTrue(LibraryApiUtils.doesStringValueExist(""));
	    assertTrue(LibraryApiUtils.doesStringValueExist(null));
	}

}
