package javax.configuration.sources;

import javax.configuration.ConfigSource;
import javax.enterprise.inject.Alternative;
import java.util.Properties;

@Alternative
public class PropertiesSource extends Properties implements ConfigSource {

	@Override
	public String get(String key) {
		return super.getProperty(key);
	}

	@Override
	public boolean contains(String key) {
		return super.containsKey(key) || super.keySet().stream().anyMatch(k -> ((String) k).startsWith(key));
	}

}
