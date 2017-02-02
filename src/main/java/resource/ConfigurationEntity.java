package resource;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
@ConfigurationMapping("qqq")
public class ConfigurationEntity {

	@ConfigurationMapping("value")
	private int value;

	@ConfigurationMapping("of")
	private String of;

	public ConfigurationEntity(String of, int value) {
		this.value = value;
		this.of = of;
	}

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
