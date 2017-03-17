package javax.configuration;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(CdiRunner.class)
@AdditionalClasses({
	TestSource.class,
	Config.class,
	ConfigProducer.class
})
public class ConfigProducerTest {

	@Inject
	private Config<TestEntity> testEntity;

	@Inject
	@ConfigMapping("some.another")
	private Config<TestEntity> testEntityWithRebase;

	@Inject
	@ConfigMapping("random.key")
	private String testString;

	// ToDo Collections / Maps
	// ToDo Enum
//
//	@Inject
//	private Map<String, TestEntity> testConfigurationMap;

	@Inject
	@ConfigMapping(value = "random.value", required = false)
	private int testPrimitive;

	@Test
	public void shoudReadPrimitiveValue() {
		assertEquals(0, testPrimitive);
	}

	@Test
	public void shoudReadStringFromConfiguration() {
		assertEquals("RANDOM!", testString);
	}
}
