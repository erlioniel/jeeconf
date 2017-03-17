package javax.configuration;


import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.configuration.sources.HashMapSource;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(CdiRunner.class)
@AdditionalClasses(ConfigProducer.class)
public class ConfigProducerObjectTest {

	@Produces
	private static HashMapSource source;

	static {
		source = new HashMapSource();
		source.put("entry.exampleString", "Valid!");
		source.put("entry.renameExample", "3");
		source.put("entry.inner.exampleChar", "z");
		source.put("another.root.exampleString", "AnotherRoot!");
		source.put("another.root.renameExample", "6");
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
