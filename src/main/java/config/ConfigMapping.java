package config;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * © 2017 weld-boot
 * Created by Vladimir Kryukov on 02.02.2017
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, FIELD, METHOD})
public @interface ConfigMapping {
    @Nonbinding String value() default "";
}
