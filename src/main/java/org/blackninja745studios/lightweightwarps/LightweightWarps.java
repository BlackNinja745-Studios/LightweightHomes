package org.blackninja745studios.lightweightwarps;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.blackninja745studios.lightweightwarps.home.HomeCommand;
import org.blackninja745studios.lightweightwarps.home.HomePermissions;
import org.blackninja745studios.lightweightwarps.home.RemoveHomeCommand;
import org.blackninja745studios.lightweightwarps.home.SetHomeCommand;
import org.blackninja745studios.lightweightwarps.spawn.SpawnCommand;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class LightweightWarps extends JavaPlugin {
    public static final String PLUGIN_PERMISSION_PREFIX = "lightweightwarps.";
    public static final String FALLBACK_PREFIX = "lightweightwarps";

    @Override
    public void onEnable() {

        boolean homeModuleEnabled = true;
        boolean spawnModuleEnabled = true;
        boolean warpsEnabled = true;

        if (homeModuleEnabled) {
            HomePermissions.initialize();

            registerCommands(new Command[] {
                new HomeCommand(this),
                new SetHomeCommand(this),
                new RemoveHomeCommand(this),
            });

            getLogger().log(Level.INFO, "Initialized home module!");
        }

        if (spawnModuleEnabled) {
            registerCommands(new Command[] { new SpawnCommand() });
            getLogger().log(Level.INFO, "Initialized spawn module!");
        }

        if (warpsEnabled) {
            getLogger().log(Level.INFO, "Initialized warps module!");
        }

        getLogger().log(Level.INFO, "Finished initializing Lightweight Warps!");
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands(Command[] commands) {
        for (Command command : commands)
            getServer().getCommandMap().register(FALLBACK_PREFIX, command);
    }

    public static Component addPluginPrefix(Component c) {
        return Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("Lightweight", NamedTextColor.GREEN))
                .append(Component.text("Warps", NamedTextColor.BLUE))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(c);
    }
}
