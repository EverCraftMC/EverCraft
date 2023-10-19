package io.github.evercraftmc.core.api.events;

public @interface ECHandler {
    ECHandlerOrder order() default ECHandlerOrder.DONTCARE;
}