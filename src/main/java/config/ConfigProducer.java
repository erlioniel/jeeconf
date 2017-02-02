package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
public class ConfigProducer {

	private static final Logger log = LoggerFactory.getLogger(ConfigProducer.class);

	private static HashMap<Class<?>, Function<String, ?>> converters;
	static {
		converters = new HashMap<>();
		converters.put(String.class, t -> t);
		converters.put(Integer.class, Integer::new);
		converters.put(int.class, Integer::new);
		converters.put(Double.class, Double::new);
		converters.put(double.class, Double::new);
		converters.put(Boolean.class, Boolean::new);
		converters.put(boolean.class, Boolean::new);
		converters.put(Character.class, t -> t.charAt(0));
		converters.put(char.class, t -> t.charAt(0));
		converters.put(Short.class, Short::new);
		converters.put(short.class, Short::new);
		converters.put(Byte.class, Byte::new);
		converters.put(byte.class, Byte::new);
		converters.put(Long.class, Long::new);
		converters.put(long.class, Long::new);
		converters.put(Float.class, Float::new);
		converters.put(float.class, Float::new);
	}

	@Inject
	private ConfigSource source;

	@Produces
	@Default
	public <T> Config<T> produce(InjectionPoint ip) throws IllegalAccessException, InstantiationException {
		ParameterizedType type = (ParameterizedType) ip.getType();
		Class targetType = (Class) type.getActualTypeArguments()[0];
		Object instance = targetType.newInstance();

		// Class prefix
		ConfigMapping classMapping = (ConfigMapping) targetType.getAnnotation(ConfigMapping.class);
		String prefix = classMapping != null && !classMapping.value().isEmpty() ? classMapping.value() + "." : "";

		Member member = ip.getMember();
		if(member instanceof AnnotatedElement) {
			AnnotatedElement annotated = (AnnotatedElement) member;
			ConfigMapping memberMapping = annotated.getAnnotation(ConfigMapping.class);
			prefix = memberMapping != null && !memberMapping.value().isEmpty() ? memberMapping.value() + "." : prefix;
		}

		String finalPrefix = prefix;
		Arrays.stream(targetType.getDeclaredFields())
			.filter(f -> f.getAnnotation(ConfigMapping.class) != null)
			.forEach(f -> set(f, instance, finalPrefix));

		//noinspection unchecked
		return new Config<>((T) instance);
	}

	private void set(Field field, Object instance, String prefix) {
		String key = prefix + field.getAnnotation(ConfigMapping.class).value();
		String rawValue = source.get(key);
		if(rawValue == null) {
			log.warn("Value for configuration {} and key {} not found!", instance, key);
			return;
		}

		Class<?> targetClass = field.getType();
		Function<String, ?> converter = converters.get(targetClass);
		if(converter == null) {
			log.warn("Value converter for type {} in configuration not found!", targetClass, instance);
			return;
		}

		try {
			field.setAccessible(true);
			field.set(instance, converter.apply(rawValue));
		} catch (IllegalAccessException e) {
			log.error("Illegal access exception in configuration {}", instance);
		}
	}
}
