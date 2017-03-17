package javax.configuration;

import javax.enterprise.inject.Alternative;

@Alternative
public class Config<T> {

	private T entity;

	private Config() {
	}

	public Config(T entity) {
		this.entity = entity;
	}

	public T get() {
		return entity;
	}

	public void set(T entity) {
		this.entity = entity;
	}
}
