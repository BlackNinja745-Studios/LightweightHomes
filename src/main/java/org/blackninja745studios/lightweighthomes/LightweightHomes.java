package org.blackninja745studios.lightweighthomes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.blackninja745studios.lightweighthomes.commands.UseHomeCommand;
import org.blackninja745studios.lightweighthomes.commands.RemoveHomeCommand;
import org.blackninja745studios.lightweighthomes.commands.SetHomeCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public final class LightweightHomes extends JavaPlugin {
    public static final String PDS_KEY_PREFIX = "lightweighthomes.";

    // TODO: make global array / enum of keys

    @Override
    public void onEnable() {

        Permissions.initialize();

        getServer().getCommandMap().registerAll("lightweighthomes", List.of(
            new UseHomeCommand(this),
            new SetHomeCommand(this),
            new RemoveHomeCommand(this)
        ));

        getLogger().log(Level.INFO, "Finished initializing Lightweight Homes!");
    }

    @Override
    public void onDisable() { }

    public static boolean hasHomeData(Player owner, LightweightHomes plugin) {
        PersistentDataContainer data = owner.getPersistentDataContainer();

        final String[] keys = { LightweightHomes.PDS_KEY_PREFIX + "x", LightweightHomes.PDS_KEY_PREFIX + "y", LightweightHomes.PDS_KEY_PREFIX + "z" };

        for (String key : keys)
            if (!data.has(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE))
                return false;

        return data.has(new NamespacedKey(plugin, LightweightHomes.PDS_KEY_PREFIX + "world"), PersistentDataType.STRING);
    }

    public static Component addPluginPrefix(Component c) {
        return Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("Lightweight", NamedTextColor.GREEN))
                .append(Component.text("Homes", NamedTextColor.GOLD))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(c);
    }
}
