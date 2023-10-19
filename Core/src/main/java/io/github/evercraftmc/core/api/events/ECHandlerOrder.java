package io.github.evercraftmc.core.api.events;

public enum ECHandlerOrder {
    FIRST(1, true),
    BEFORE(2, false),
    DONTCARE(3, false),
    AFTER(4, false),
    LAST(5, true);

    final int order;
    final boolean exclusive;

    ECHandlerOrder(int order, boolean exclusive) {
        this.order = order;
        this.exclusive = exclusive;
    }

    public int getOrder() {
        return this.order;
    }

    public boolean isExclusive() {
        return this.exclusive;
    }
}