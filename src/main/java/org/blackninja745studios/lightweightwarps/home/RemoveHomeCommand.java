package org.blackninja745studios.lightweightwarps.home;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.blackninja745studios.lightweightwarps.LightweightWarps;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RemoveHomeCommand extends Command {

    private final LightweightWarps plugin;
    private final Permission removeHome = new Permission("lightweightwarps.home.removehome");

    public RemoveHomeCommand(LightweightWarps plugin) {
        super("removehome", "Deletes a player's home.", "/removehome [<player>]", List.of());
        removeHome.setDefault(PermissionDefault.TRUE);
        Bukkit.getPluginManager().addPermission(removeHome);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player p) {
                if (!p.hasPermission(removeHome)) {
                    p.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text(LightweightWarps.NO_PERMISSIONS, NamedTextColor.RED)
                    ));
                    return true;
                }

                if ( removeHomeIfExists(p.getPersistentDataContainer()) ) {
                    p.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text("Removed your home!", NamedTextColor.WHITE)
                    ));
                } else {
                    p.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text("You do not have a home yet!", NamedTextColor.RED)
                    ));
                }


            } else {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text("Specify the player whose house to delete.", NamedTextColor.RED)
                ));
            }
        } else if (args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);

            if (sender instanceof Player ps) {
                if (p != null && p.getUniqueId().equals(ps.getUniqueId())) {
                    p.performCommand("removehome");
                    return true;
                }
            }

            if (!sender.hasPermission(LightweightWarps.permissionManageHomes)) {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text(LightweightWarps.NO_PERMISSIONS, NamedTextColor.RED)
                ));
                return true;
            }

            if (p == null) {
                sender.sendMessage(LightweightWarps.addPluginPrefix(
                        Component.text("That player isn't online.", NamedTextColor.RED)
                ));
            } else {
                if (removeHomeIfExists(p.getPersistentDataContainer())) {
                    sender.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text(PlainTextComponentSerializer.plainText().serialize(p.displayName()) + "'s home removed!", NamedTextColor.WHITE)
                    ));
                } else {
                    sender.sendMessage(LightweightWarps.addPluginPrefix(
                            Component.text(PlainTextComponentSerializer.plainText().serialize(p.displayName()) + " doesn't have a home yet.", NamedTextColor.RED)
                    ));
                }
            }
        } else {
            sender.sendMessage(LightweightWarps.addPluginPrefix(
                    Component.text(LightweightWarps.INVALID_ARGUMENTS, NamedTextColor.RED)
            ));
        }
        return true;
    }

    private boolean removeHomeIfExists(PersistentDataContainer data) {
        String[] keys = {"lightweightwarps.home.x", "lightweightwarps.home.y", "lightweightwarps.home.z"};

        for (String key : keys)
            if (data.has(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE))
                data.remove(new NamespacedKey(plugin, key));
        boolean hasWorldKey = data.has(new NamespacedKey(plugin, "lightweightwarps.home.world"), PersistentDataType.STRING);
        if (hasWorldKey)
            data.remove(new NamespacedKey(plugin, "lightweightwarps.home.world"));

        return hasWorldKey;
    }
}
