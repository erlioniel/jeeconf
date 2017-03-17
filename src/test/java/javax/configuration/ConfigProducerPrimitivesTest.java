package javax.configuration;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.configuration.sources.HashMapSource;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(CdiRunner.class)
@AdditionalClasses(ConfigProducer.class)
public class ConfigProducerPrimitivesTest {

	@Produces
	private static HashMapSource source;

	static {
		source = new HashMapSource();
		source.put("primitive.string", "Test");
		source.put("primitive.number", "3");
		source.put("primitive.double", "3.2");
		source.put("primitive.boolean", "false");
		source.put("primitive.char", "c");
	}

	@Inject
	@ConfigMapping("primitive.string")
	private String testString;

	@Inject
	@ConfigMapping("primitive.number")
	private int testInteger;

	@Inject
	@ConfigMapping("primitive.number")
	private long testLong;

	@Inject
	@ConfigMapping("primitive.number")
	private short testShort;

	@Inject
	@ConfigMapping("primitive.double")
	private double testDouble;

	@Inject
	@ConfigMapping("primitive.double")
	private float testFloat;

	@Inject
	@ConfigMapping("primitive.boolean")
	private char testBoolean;

	@Inject
	@ConfigMapping("primitive.char")
	private char testChar;

	@Test
	public void shouldInjectString() {
		assertEquals("Test", testString);
	}

	@Test
	public void shouldInjectInteger() {
		assertEquals(3, testInteger);
	}

	@Test
	public void shouldInjectShort() {
		assertEquals((short) 3, testInteger);
	}

	@Test
	public void shouldInjectLong() {
		assertEquals(3L, testLong);
	}

	@Test
	public void shouldInjectDouble() {
		assertEquals(3.2, testDouble, 0);
	}

	@Test
	public void shouldInjectFloat() {
		assertEquals(3.2F, testFloat, 0);
	}
}
