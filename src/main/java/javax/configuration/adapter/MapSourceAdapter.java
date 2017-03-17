package javax.configuration.adapter;

import javax.configuration.ConfigSource;
import java.util.Map;

public class MapSourceAdapter implements ConfigSource {
	private final Map<String, String> map;

	public MapSourceAdapter(Map<String, String> map) {
		this.map = map;
	}

	@Override
	public String get(String key) {
		return map.get(key);
	}

	@Override
	public boolean contains(String key) {
		return map.containsKey(key) || map.keySet().stream().anyMatch(k -> k.startsWith(key));
	}
}
