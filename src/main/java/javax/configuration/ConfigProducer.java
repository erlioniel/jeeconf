package javax.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigProducer {

	// ToDo Wrapper primitive test
	// ToDo Collections
	// ToDo Optional test, optional is really optional?
	// ToDo Enum processing
	// ToDo Custom converter supports

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
	@ConfigMapping
	public <T> Config<T> produce(InjectionPoint ip) {
		ParameterizedType type = (ParameterizedType) ip.getType();
		Class targetType = (Class) type.getActualTypeArguments()[0];

		// Class prefix
		String prefix = getAnnotationKey(ip.getMember(), targetType, findQualifier(ip.getQualifiers()));
		return new Config<>(createInstance(targetType, prefix));
	}

	@Produces
	@ConfigMapping
	public <T> Optional<T> produceOptional(InjectionPoint ip) {
		//noinspection unchecked
		return Optional.of((T) produce(ip));
	}

	@Produces
	@ConfigMapping
	public String produceString(InjectionPoint ip) {
		return produceT(String.class, ip);
	}

	@Produces
	@ConfigMapping
	public Integer produceInteger(InjectionPoint ip) {
		return produceT(Integer.class, ip);
	}

	@Produces
	@ConfigMapping
	public Long produceLong(InjectionPoint ip) {
		return produceT(Long.class, ip);
	}

	@Produces
	@ConfigMapping
	public Short produceShort(InjectionPoint ip) {
		return produceT(Short.class, ip);
	}

	@Produces
	@ConfigMapping
	public Double produceDouble(InjectionPoint ip) {
		return produceT(Double.class, ip);
	}

	@Produces
	@ConfigMapping
	public Float produceFloat(InjectionPoint ip) {
		return produceT(Float.class, ip);
	}

	@Produces
	@ConfigMapping
	public Boolean produceBoolean(InjectionPoint ip) {
		return produceT(Boolean.class, ip);
	}

	@Produces
	@ConfigMapping
	public Character produceCharacter(InjectionPoint ip) {
		return produceT(Character.class, ip);
	}

	private <T> T produceT(Class<T> targetClass, InjectionPoint ip) {
		String key = getAnnotationKey(ip.getMember(), targetClass, findQualifier(ip.getQualifiers()));
		return isExists(key, ip.getMember())
			? getValue(targetClass, key)
			: null;
	}

	private <T> T getValue(Class<T> targetClass, String key) {
		String rawValue = source.get(key);
		Function<String, ?> converter = converters.get(targetClass);

		//noinspection unchecked
		return (T) converter.apply(rawValue);
	}

	private String getAnnotationKey(Member member, @Nullable AnnotatedElement target, @Nullable ConfigMapping annotation) {
		// Use element annotation as primary
		String key = annotation != null && !annotation.value().isEmpty()
			? annotation.value()
			: null;
		if (key == null) {
			// Use target annotation as secondary
			if (target != null) {
				annotation = target.getAnnotation(ConfigMapping.class);
				key = annotation != null && !annotation.value().isEmpty()
					? annotation.value()
					: null;
			}

			// If nothing is found - use field name
			if (key == null && member instanceof Field) {
				key = member.getName();
			}
		}
		return key;
	}

	@Nullable
	private ConfigMapping findQualifier(Set<Annotation> qualifiers) {
		for (Annotation qualifier : qualifiers) {
			if(qualifier instanceof ConfigMapping) {
				return (ConfigMapping) qualifier;
			}
		}
		return null;
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
		Class<?> targetClass = field.getType();
		String key = (prefix != null ? prefix + "." : "") + getAnnotationKey(field, targetClass, field.getAnnotation(ConfigMapping.class));

		if (!isExists(key, (AnnotatedElement) field)) {
			return;
		}

		Supplier<?> supplier;
		if (converters.containsKey(targetClass)) {
			supplier = () -> getValue(targetClass, key);
		} else {
			supplier = () -> createInstance(targetClass, key);
		}

		try {
			field.setAccessible(true);
			field.set(instance, supplier.get());
		} catch (IllegalAccessException e) {
			throw new InjectionException("Illegal access exception in configuration " + instance, e);
		}
	}

	private boolean isExists(String key, Member member) {
		return isExists(key, (AnnotatedElement) member);
	}

	private boolean isExists(String key, AnnotatedElement target) {
		if (source.contains(key)) {
			return true;
		}

		if (target.getAnnotation(ConfigMapping.class).required()) {
			throw new InjectionException("Child node key '" + key + "' not found!");
		}

		log.warn("Child node key '{}' not found!", key);
		return false;
	}
}
