package javax.configuration.adapter;

import javax.configuration.ConfigSource;
import java.util.Properties;

public class PropertiesSourceAdapter implements ConfigSource {

	private final Properties properties;

	public PropertiesSourceAdapter(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String get(String key) {
		return properties.getProperty(key);
	}

	@Override
	public boolean contains(String key) {
		return properties.containsKey(key) || properties.keySet().stream().anyMatch(k -> ((String) k).startsWith(key));
	}
}
