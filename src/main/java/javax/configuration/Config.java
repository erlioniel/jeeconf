package javax.configuration;

public class Config<T> {

	private final T entity;

	public Config(T entity) {
		this.entity = entity;
	}

	public T get() {
		return entity;
	}
}
