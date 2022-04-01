package io.github.evercraftmc.evercraft.spigot.util.types;

import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import org.bukkit.Location;

public class SerializableLocation {
    private String world;

    private Double x;
    private Double y;
    private Double z;

    private Float yaw;
    private Float pitch;

    private SerializableLocation(String world, Double x, Double y, Double z, Float yaw, Float pitch) {
        this.world = world;

        this.x = x;
        this.y = y;
        this.z = z;

        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getWorld() {
        return this.world;
    }

    public Double getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }

    public Double getZ() {
        return this.z;
    }

    public Float getYaw() {
        return this.yaw;
    }

    public Float getPitch() {
        return this.pitch;
    }

    public Location toBukkitLocation() {
        return new Location(SpigotMain.getInstance().getServer().getWorld(this.getWorld()), this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
    }

    public static SerializableLocation fromBukkitLocation(Location location) {
        return new SerializableLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}