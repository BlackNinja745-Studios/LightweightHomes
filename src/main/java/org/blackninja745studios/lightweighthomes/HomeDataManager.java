package org.blackninja745studios.lightweighthomes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.UUID;

public class HomeDataManager {

    public static final String KEY_PREFIX = "lightweighthomes.";
    public final NamespacedKey[] KEYS;

    public HomeDataManager(LightweightHomes plugin) {
        KEYS = new NamespacedKey[] {
            new NamespacedKey(plugin, KEY_PREFIX + "x"),
            new NamespacedKey(plugin, KEY_PREFIX + "y"),
            new NamespacedKey(plugin, KEY_PREFIX + "z"),
            new NamespacedKey(plugin, KEY_PREFIX + "world")
        };
    }

    public boolean hasHomeData(PersistentDataContainer data) {
        return Arrays.stream(KEYS).allMatch(data::has);
    }

    public Location getHomeLocation(PersistentDataContainer data) {
        if (!hasHomeData(data))
            return null;

        World world = Bukkit.getServer().getWorld(
            UUID.fromString(data.get(KEYS[3], PersistentDataType.STRING))
        );

        if (world == null)
            return null;

        return new Location(
            world,
            data.get(KEYS[0], PersistentDataType.DOUBLE),
            data.get(KEYS[1], PersistentDataType.DOUBLE),
            data.get(KEYS[2], PersistentDataType.DOUBLE)
        );
    }

    public void createHome(PersistentDataContainer data, Location l) {
        data.set(KEYS[0], PersistentDataType.DOUBLE, l.getX());
        data.set(KEYS[1], PersistentDataType.DOUBLE, l.getY());
        data.set(KEYS[2], PersistentDataType.DOUBLE, l.getZ());
        data.set(KEYS[3], PersistentDataType.STRING, l.getWorld().getUID().toString());
    }

    public boolean removeHome(PersistentDataContainer data) {
        for (NamespacedKey key : KEYS) {
            if (data.has(key))
                data.remove(key);
            else
                return false;
        }

        return true;
    }
}