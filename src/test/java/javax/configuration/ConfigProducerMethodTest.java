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

@RunWith(CdiRunner.class)
@AdditionalClasses(ConfigProducer.class)
public class ConfigProducerMethodTest {

	@Produces
	private static ConfigSource source;

	static {
		Properties properties = new Properties();
		properties.put("testString", "Test");
		source = new PropertiesSourceAdapter(properties);
	}

	@Inject
	private TestBean bean;

	@Test
	public void shouldInjectIntoContructor() {
		assertEquals("Test", bean.string);
	}

	public static class TestBean {

		private final String string;

		@Inject
		public TestBean(@ConfigMapping("testString") String string) {
			this.string = string;
		}
	}
}
