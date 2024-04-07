package org.blackninja745studios.lightweightwarps;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.blackninja745studios.lightweightwarps.home.HomeCommand;
import org.blackninja745studios.lightweightwarps.home.RemoveHomeCommand;
import org.blackninja745studios.lightweightwarps.home.SetHomeCommand;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class LightweightWarps extends JavaPlugin {

    public static final String NO_PERMISSIONS = "You do not have permission to use this command.";
    public static final String INVALID_ARGUMENTS = "Invalid arguments.";

    public static final Permission permissionManageHomes = new Permission("lightweightwarps.home.managehomes");

    @Override
    public void onEnable() {
        initializeHomeModule();
        getLogger().log(Level.INFO, "Successfully initialized Lightweight Warps!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void initializeHomeModule() {
        permissionManageHomes.setDefault(PermissionDefault.OP);
        Bukkit.getPluginManager().addPermission(permissionManageHomes);

        getServer().getCommandMap().register("lightweightwarps", new HomeCommand(this));
        getServer().getCommandMap().register("lightweightwarps", new SetHomeCommand(this));
        getServer().getCommandMap().register("lightweightwarps", new RemoveHomeCommand(this));
    }

    public static Component addPluginPrefix(Component c) {
        return Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("Lightweight", NamedTextColor.GREEN))
                .append(Component.text("Warps", NamedTextColor.BLUE))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(c);
    }
}
