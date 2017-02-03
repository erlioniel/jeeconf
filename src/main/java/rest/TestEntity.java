package rest;

import config.ConfigMapping;

import javax.annotation.Nullable;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
@ConfigMapping("some.kind")
public class TestEntity {

	@ConfigMapping
	private int value;

	@ConfigMapping
	private String of;

	@ConfigMapping("in")
	@Nullable
	private InnerTestEntity testEntity;

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

	@Nullable
	public InnerTestEntity getTestEntity() {
		return testEntity;
	}

	public void setTestEntity(@Nullable InnerTestEntity testEntity) {
		this.testEntity = testEntity;
	}

	@ConfigMapping
	public static class InnerTestEntity {

		@ConfigMapping
		private boolean debug;

		@ConfigMapping
		private String value;

		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
