package javax.configuration;

public interface ConfigSource {
	String get(String key);
	boolean contains(String key);
}
