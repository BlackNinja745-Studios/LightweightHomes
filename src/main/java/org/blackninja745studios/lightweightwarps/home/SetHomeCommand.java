package org.blackninja745studios.lightweightwarps.home;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.blackninja745studios.lightweightwarps.LightweightWarps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetHomeCommand extends Command {

    private final LightweightWarps plugin;
    private final Permission setHome = new Permission("lightweightwarps.home.sethome");

    public SetHomeCommand(LightweightWarps plugin) {
        super("sethome", "Sets a player's home.", "/sethome [<player> <x> <y> <z> <world>]", List.of());
        setHome.setDefault(PermissionDefault.TRUE);
        Bukkit.getPluginManager().addPermission(setHome);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player p) {
                if (!p.hasPermission(setHome)) {
                    p.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text(LightweightWarps.NO_PERMISSIONS, NamedTextColor.RED)
                    ));
                    return true;
                }

                Location l = p.getLocation();
                createHome(p, l);

                p.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text(String.format("Created home at (%.1f, %.1f, %.1f).", l.getX(), l.getY(), l.getZ()), NamedTextColor.WHITE)
                ));
            } else {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text("This command must be executed by a player.", NamedTextColor.RED)
                ));
            }
        } else if (args.length == 1) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text("This command must be executed by a player.", NamedTextColor.RED)
                ));
                return true;
            }

            Player t = Bukkit.getPlayer(args[0]);

            if (t != null && t.getUniqueId().equals(p.getUniqueId())) {
                p.performCommand("sethome");
                return true;
            }

            if (!sender.hasPermission(LightweightWarps.permissionManageHomes)) {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text(LightweightWarps.NO_PERMISSIONS, NamedTextColor.RED)
                ));
                return true;
            }

            if (t == null) {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text("Player must be online.", NamedTextColor.RED)
                ));
            } else {
                Location l = p.getLocation();
                createHome(t, l);
                p.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text(String.format("Created home at (%.1f, %.1f, %.1f).", l.getX(), l.getY(), l.getZ()), NamedTextColor.WHITE)
                ));
            }

        } else if (args.length == 5) {
            if (!sender.hasPermission(LightweightWarps.permissionManageHomes)) {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text(LightweightWarps.NO_PERMISSIONS, NamedTextColor.RED)
                ));
                return true;
            }

            Player p = Bukkit.getPlayer(args[0]);

            if (p == null) {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text("Player must be online.", NamedTextColor.RED)
                ));
                return true;
            }

            Location l = getLocationFromArgs(args);

            if (l == null) {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text(LightweightWarps.INVALID_ARGUMENTS, NamedTextColor.RED)
                ));
                return true;
            }

            createHome(p, l);
            p.sendMessage(LightweightWarps.addPluginPrefix(
                    Component.text(String.format("Created home at (%.1f, %.1f, %.1f).", l.getX(), l.getY(), l.getZ()), NamedTextColor.WHITE)
            ));
        } else {
            sender.sendMessage(LightweightWarps.addPluginPrefix(
                    Component.text(LightweightWarps.INVALID_ARGUMENTS, NamedTextColor.RED)
            ));
        }

        return true;
    }

    private void createHome(Player player, Location l) {
        PersistentDataContainer data = player.getPersistentDataContainer();

        data.set(new NamespacedKey(plugin, "lightweightwarps.home.x"), PersistentDataType.DOUBLE, l.getX());
        data.set(new NamespacedKey(plugin, "lightweightwarps.home.y"), PersistentDataType.DOUBLE, l.getY());
        data.set(new NamespacedKey(plugin, "lightweightwarps.home.z"), PersistentDataType.DOUBLE, l.getZ());
        data.set(new NamespacedKey(plugin, "lightweightwarps.home.world"), PersistentDataType.STRING, l.getWorld().getUID().toString());
    }

    private static Location getLocationFromArgs(String[] args) {
        if (args.length != 5)
            return null;

        double x, y, z;

        try {
            x = Double.parseDouble(args[1]);
            y = Double.parseDouble(args[2]);
            z = Double.parseDouble(args[3]);
        } catch (Exception ignored) {
            return null;
        }

        World w = Bukkit.getWorld(args[4]);

        if (w == null) {
            return null;
        }

        return new Location(w, x, y, z);
    }
}
