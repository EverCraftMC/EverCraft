package io.github.evercraftmc.evercraft.discord.data.types.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.github.evercraftmc.evercraft.discord.data.DataParseable;

public class Data extends DataParseable {
    public List<ModCase> history = new ArrayList<ModCase>();
    public Map<String, List<Warning>> warnings = new HashMap<String, List<Warning>>();

    public List<ReactionRole> reactions = new ArrayList<ReactionRole>();
}