package rest;

import config.ConfigMapping;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
@ConfigMapping("some.kind")
public class TestEntity {

	@ConfigMapping("value")
	private int value;

	@ConfigMapping("of")
	private String of;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getOf() {
		return of;
	}

	public void setOf(String of) {
		this.of = of;
	}
}
