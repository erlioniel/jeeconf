package javax.configuration;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.configuration.adapter.PropertiesSourceAdapter;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(CdiRunner.class)
@AdditionalClasses(ConfigProducer.class)
public class ConfigProducerMethodTest {

	@Produces
	private static ConfigSource source;

	static {
		Properties properties = new Properties();
		properties.put("testString", "Test");
		properties.put("root.inner", "Inner");
		properties.put("root.active", "TRUE");
		source = new PropertiesSourceAdapter(properties);
	}

	@Inject
	private TestBean bean;

	@Test
	public void shouldInjectIntoContructor() {
		assertEquals("Test", bean.string);
	}

	@Test
	public void shouldInjectObject() {
		assertEquals("Inner", bean.configuration.inner);
		assertTrue(bean.configuration.active);
	}

	public static class TestBean {

		private final String string;
		private final TestConfiguration configuration;

		@Inject
		public TestBean(@ConfigMapping("testString") String string,
						@ConfigMapping("root") Config<TestConfiguration> configuration) {
			this.string = string;
			this.configuration = configuration.get();
		}
	}

	@ConfigMapping
	public static class TestConfiguration {

		@ConfigMapping
		private String inner;

		@ConfigMapping
		private boolean active;
	}
}
