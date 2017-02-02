package source;

import config.ConfigSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
public class PropertiesSource implements ConfigSource {

	private final Properties properties;

	public PropertiesSource() {
		final InputStream stream = PropertiesSource.class
			.getClassLoader().getResourceAsStream("config.properties");
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

	public String get(String key) {
		return properties.getProperty(key);
	}

	@Override
	public boolean isExists(String key) {
		return properties.containsKey(key);
	}

	@Override
	public boolean isRootExists(String key) {
		return properties.keySet().stream()
			.anyMatch(k -> ((String) k).startsWith(key));
	}
}
