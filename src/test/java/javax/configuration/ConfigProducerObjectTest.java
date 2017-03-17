package javax.configuration;


import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.configuration.adapter.MapSourceAdapter;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(CdiRunner.class)
@AdditionalClasses(ConfigProducer.class)
public class ConfigProducerObjectTest {

	@Produces
	private static ConfigSource source;

	static {
		HashMap<String, String> sourceMap = new HashMap<>();
		sourceMap.put("entry.exampleString", "Valid!");
		sourceMap.put("entry.renameExample", "3");
		sourceMap.put("entry.inner.exampleChar", "z");
		sourceMap.put("another.root.exampleString", "AnotherRoot!");
		sourceMap.put("another.root.renameExample", "6");
		source = new MapSourceAdapter(sourceMap);
	}

	@Inject
	private Config<TestEntry> simpleObject;

	@Inject
	@ConfigMapping("another.root")
	private Config<TestEntry> anotherObject;

	@Test
	public void shouldReadSimpleObject() {
		assertEquals("Valid!", simpleObject.get().exampleString);
		assertEquals(3, simpleObject.get().exampleInt);

		assertEquals('z', simpleObject.get().inner.exampleChar);
	}

	@Test
	public void shouldReadAnotherRootObject() {
		assertEquals("AnotherRoot!", anotherObject.get().exampleString);
		assertEquals(6, anotherObject.get().exampleInt);

		assertNull(anotherObject.get().inner);
	}

	@ConfigMapping("entry")
	public static class TestEntry {

		@ConfigMapping
		private String exampleString;

		@ConfigMapping("renameExample")
		private int exampleInt;

		@ConfigMapping(required = false)
		private InnerEntry inner;
	}

	@ConfigMapping
	public static class InnerEntry {

		@ConfigMapping
		private char exampleChar;
	}
}
