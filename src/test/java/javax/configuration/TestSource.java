package javax.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestSource implements ConfigSource {

	private final Properties properties;

	public TestSource() {
		final InputStream stream = getClass().getClassLoader()
			.getResourceAsStream("config.properties");
		if (stream == null) {
			throw new RuntimeException("No properties!!!");
		}
		try {
			this.properties = new Properties();
			this.properties.load(stream);
		} catch (final IOException e) {
			throw new RuntimeException("Configuration could not be loaded!");
		}
	}

	@Override
	public String get(String key) {
		return properties.getProperty(key);
	}

	@Override
	public boolean isExists(String key) {
		return properties.containsKey(key)
			|| properties.keySet().stream()
			.anyMatch(k -> ((String) k).startsWith(key));
	}
}
