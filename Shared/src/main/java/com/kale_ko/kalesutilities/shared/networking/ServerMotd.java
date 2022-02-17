package com.kale_ko.kalesutilities.shared.networking;

import java.util.List;

public class ServerMotd extends ServerMotdComponent {
    private List<ServerMotdComponent> extra;

    public List<ServerMotdComponent> getExtra() {
        return this.extra;
    }
}