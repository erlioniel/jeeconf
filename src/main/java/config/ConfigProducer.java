package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.InjectionException;
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
import java.util.function.Supplier;

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

	private final ConfigSource source;

	@Inject
	public ConfigProducer(ConfigSource source) {
		this.source = source;
	}

	@Produces
	@Default
	public <T> Config<T> produce(InjectionPoint ip) {
		ParameterizedType type = (ParameterizedType) ip.getType();
		Class targetType = (Class) type.getActualTypeArguments()[0];

		// Class prefix
		String injectionPrefix = getAnnotationKey(ip.getMember());
		String prefix = injectionPrefix != null
			? injectionPrefix
			: getAnnotationKey(targetType);

		return new Config<>(createInstance(targetType, prefix));
	}

	private String getAnnotationKey(Member member) {
		if (member instanceof AnnotatedElement) {
			return getAnnotationKey((AnnotatedElement) member);
		}
		return null;
	}

	private String getAnnotationKey(AnnotatedElement element) {
		ConfigMapping annotation = element.getAnnotation(ConfigMapping.class);
		return annotation != null && !annotation.value().isEmpty()
			? annotation.value()
			: null;
	}

	private <T> T createInstance(Class targetType, String prefix) {
		try {
			Object instance = targetType.newInstance();
			Arrays.stream(targetType.getDeclaredFields())
				.filter(f -> f.getAnnotation(ConfigMapping.class) != null)
				.forEach(f -> set(f, instance, prefix));

			//noinspection unchecked
			return (T) instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new InjectionException("Error in creating instance for configuration " + targetType, e);
		}
	}

	private void set(Field field, Object instance, String prefix) {
		String key = (prefix != null ? prefix + "." : "") + getAnnotationKey((AnnotatedElement) field);
		Class<?> targetClass = field.getType();
		Supplier<?> supplier = null;
		if (converters.containsKey(targetClass)) {
			if (!source.isExists(key)) {
				log.warn("Value for configuration {} and key '{}' not found!", instance, key);
				return;
			}

			String rawValue = source.get(key);
			Function<String, ?> converter = converters.get(targetClass);
			supplier = () -> converter.apply(rawValue);
		} else if (targetClass.getAnnotation(ConfigMapping.class) != null) {
			if (!source.isRootExists(key)) {
				log.warn("Child node for configuration {} and key '{}' not found!", instance, key);
				return;
			}
			supplier = () -> createInstance(targetClass, key);
		}

		if (supplier == null) {
			log.warn("Supplier for type {} in configuration not found!", targetClass, instance);
			return;
		}

		try {
			field.setAccessible(true);
			field.set(instance, supplier.get());
		} catch (IllegalAccessException e) {
			log.error("Illegal access exception in configuration {}", instance);
		}
	}
}
