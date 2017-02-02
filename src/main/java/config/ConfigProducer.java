package config;

import rest.TestEntity;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
public class ConfigProducer {
	@Produces
	@Default
	public <T> Config<T> produce(InjectionPoint ip) {
		return new Config<>((T) new TestEntity("qqqq", 5));
	}
}
