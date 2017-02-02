package config;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
@Retention(RUNTIME)
@Target({TYPE,FIELD})
public @interface ConfigMapping {
	String value() default "";
}
