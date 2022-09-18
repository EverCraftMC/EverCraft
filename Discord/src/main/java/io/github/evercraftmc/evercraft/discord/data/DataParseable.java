package io.github.evercraftmc.evercraft.discord.data;

public class DataParseable {
    private transient DataParser<? extends DataParseable> parser;

    public void setParser(DataParser<? extends DataParseable> parser) {
        this.parser = parser;
    }

    public DataParser<? extends DataParseable> getParser() {
        return this.parser;
    }
}