package com.sirma.itt.seip.domain.semantic.persistence;

import com.sirma.itt.seip.exception.EmfRuntimeException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests {@link MultiLanguageValue}.
 *
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 24/08/2017
 */
public class MultiLanguageValueTest {

	private MultiLanguageValue cut;

	@Before
	public void init() {
		cut = new MultiLanguageValue();
		MockitoAnnotations.initMocks(this);

		Map<String, Serializable> languageToValueMapping = new HashMap<>();
		languageToValueMapping.put("DEUTSCH", "Das ist ein Titel!");
		languageToValueMapping.put("GERMAN", "Das ist auch ein Titel!");
		Whitebox.setInternalState(cut, "languageToValueMapping", languageToValueMapping);
	}

	@Test
	public void test_getValues_directHit() {
		assertEquals("Das ist ein Titel!", cut.getValues("DEUTSCH").findFirst().orElse(null));
	}

	@Test
	public void test_hasValue_shouldCheckIfLanguageExists() {
		assertFalse(cut.hasValueForLanguage("something"));
		assertTrue(cut.hasValueForLanguage("GERMAN"));
	}

	@Test
	public void test_getValues_nullMap() {
		Map<String, Serializable> languageToValueMapping = null;
		Whitebox.setInternalState(cut, "languageToValueMapping", languageToValueMapping);
		assertEquals(0, cut.getValues("English").count());
	}

	@Test
	public void test_getAllValues() {
		assertEquals(cut.getAllValues().count(), 2);
	}

	@Test
	public void test_addValue_emptyMap() {
		Whitebox.setInternalState(cut, "languageToValueMapping", new HashMap<>());
		cut.addValue("English", "Hello");
		assertEquals(1, cut.getAllValues().count());
	}

	@Test
	public void addValue() {
		cut.addValue("English", "Hello");
		assertEquals(3, cut.getAllValues().count());
		assertEquals("Hello", cut.getValues("English").findFirst().orElse(null));
	}

	@Test
	public void addValue_sameLanguage() {
		cut.addValue("DEUTSCH", "HALLO");
		assertEquals(3, cut.getAllValues().count());
		assertEquals(2L, cut.getValues("DEUTSCH").count());
	}

	@Test
	public void addValue_shouldIgnoreNullOrEmptyValues() {
		MultiLanguageValue multiLanguageValue = new MultiLanguageValue();

		multiLanguageValue.addValue("en", null);
		assertEquals(0, multiLanguageValue.getAllValues().count());

		multiLanguageValue.addValue("en", "");
		assertEquals(0, multiLanguageValue.getAllValues().count());

		multiLanguageValue.addValue("en", "\n  \t");
		assertEquals(0, multiLanguageValue.getAllValues().count());
	}
}
