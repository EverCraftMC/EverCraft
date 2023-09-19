package io.github.evercraftmc.core.api.events;

public abstract class ECCancelableReasonEvent extends ECCancelableEvent {
    protected String cancelReason = null;

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}