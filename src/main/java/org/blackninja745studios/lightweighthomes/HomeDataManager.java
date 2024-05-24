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
        double[] loc = new double[3];

        for (int i = 0; i < 3; ++i) {
            if (data.has(KEYS[i]))
                loc[i] = data.get(KEYS[i], PersistentDataType.DOUBLE);
            else
                return null;
        }

        String worldUID;

        if (data.has(KEYS[3]))
            worldUID = data.get(KEYS[3], PersistentDataType.STRING);
        else
            return null;

        World world = Bukkit.getServer().getWorld(UUID.fromString(worldUID));

        return world == null ? null : new Location(world, loc[0], loc[1], loc[2]);
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