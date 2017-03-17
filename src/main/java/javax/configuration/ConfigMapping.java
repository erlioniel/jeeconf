package javax.configuration;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target({TYPE, FIELD, METHOD})
public @interface ConfigMapping {
    @Nonbinding String value() default "";
    @Nonbinding boolean required() default true;
}
