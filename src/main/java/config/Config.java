package config;

import javax.enterprise.inject.Alternative;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
@Alternative
public class Config<T> {

	private T entity;

	public Config() {
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
