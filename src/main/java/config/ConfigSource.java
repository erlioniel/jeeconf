package config;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
public interface ConfigSource {
	String get(String key);
}
