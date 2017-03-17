package javax.configuration.sources;

import javax.configuration.ConfigSource;
import javax.enterprise.inject.Alternative;
import java.util.HashMap;

@Alternative
public class HashMapSource extends HashMap<String, String> implements ConfigSource {

	@Override
	public String get(String key) {
		return super.get(key);
	}

	@Override
	public boolean contains(String key) {
		return super.containsKey(key) || super.keySet().stream().anyMatch(k -> k.startsWith(key));
	}

}
