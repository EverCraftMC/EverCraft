package io.github.evercraftmc.core.api.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ECHandler {
    ECHandlerOrder order() default ECHandlerOrder.DONTCARE;
}