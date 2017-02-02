package config;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
public class ConfigProducer {

	@Inject
	private ConfigSource source;

	@Produces
	@Default
	public <T> Config<T> produce(InjectionPoint ip) throws IllegalAccessException, InstantiationException {
		ParameterizedType type = (ParameterizedType) ip.getType();
		Class targetType = (Class) type.getActualTypeArguments()[0];
		Object instance = targetType.newInstance();



		return new Config<>((T) instance);
	}
}
