package org.blackninja745studios.lightweightwarps.home;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.blackninja745studios.lightweightwarps.LightweightWarps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.List;

public class HomeCommand extends Command {
    private final Permission use = new Permission("lightweightwarps.home.use");

    private final LightweightWarps plugin;

    public HomeCommand(LightweightWarps plugin) {
        super("home", "Returns a calling player to their set home.", "/home", List.of());
        use.setDefault(PermissionDefault.TRUE);
        Bukkit.getPluginManager().addPermission(use);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 0) {
                if (!p.hasPermission(use)) {
                    p.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text(LightweightWarps.NO_PERMISSIONS, NamedTextColor.RED)
                    ));
                    return true;
                }

                Location l = buildLocationFromData(p.getPersistentDataContainer());

                if (l == null)
                    p.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text("You do not have a home yet!", NamedTextColor.RED)
                    ));
                else
                    p.teleport(l);
            } else if (args.length == 1) {
                Player t = Bukkit.getPlayer(args[0]);

                if (t != null && t.getUniqueId().equals(p.getUniqueId())) {
                    p.performCommand("home");
                    return true;
                }

                if (!p.hasPermission(LightweightWarps.permissionManageHomes)) {
                    p.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text(LightweightWarps.NO_PERMISSIONS, NamedTextColor.RED)
                    ));
                    return true;
                }

                if (t == null) {
                    sender.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text("That player isn't online.", NamedTextColor.RED)
                    ));
                } else {
                    Location l = buildLocationFromData(t.getPersistentDataContainer());

                    if (l == null)
                        p.sendMessage(LightweightWarps.addPluginPrefix(
                                Component.text(PlainTextComponentSerializer.plainText().serialize(t.displayName()) + " does not have a home yet!", NamedTextColor.RED)
                        ));
                    else
                        p.teleport(l);
                }
            } else {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text(LightweightWarps.INVALID_ARGUMENTS, NamedTextColor.RED)
                ));
            }
        } else {
            sender.sendMessage(LightweightWarps.addPluginPrefix(
                    Component.text("This command must be executed by a player.", NamedTextColor.RED)
            ));
        }

        return true;
    }

    private Location buildLocationFromData(PersistentDataContainer data) {
        double[] loc = new double[3];
        String[] keys = {"lightweightwarps.home.x", "lightweightwarps.home.y", "lightweightwarps.home.z"};

        for (int i = 0; i < 3; ++i) {
            if (data.has(new NamespacedKey(plugin, keys[i]), PersistentDataType.DOUBLE))
                loc[i] = data.get(new NamespacedKey(plugin, keys[i]), PersistentDataType.DOUBLE);
            else
                return null;
        }

        String worldUID;

        if (data.has(new NamespacedKey(plugin, "lightweightwarps.home.world"), PersistentDataType.STRING))
            worldUID = data.get(new NamespacedKey(plugin, "lightweightwarps.home.world"), PersistentDataType.STRING);
        else
            return null;

        return new Location(
                plugin.getServer().getWorld(UUID.fromString(worldUID)),
                loc[0], loc[1], loc[2]
        );
    }
}
