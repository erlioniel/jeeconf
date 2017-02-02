package resource;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import javax.ws.rs.ext.Provider;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
@Named
@Provider
public class ConfigurationProducer {
	@Produces
	@Default
	public <T> Configuration<T> produce(InjectionPoint ip) {
		return new Configuration<>((T) new ConfigurationEntity("qqqq", 5));
	}
}
