package org.blackninja745studios.lightweighthomes.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.blackninja745studios.lightweighthomes.CommandError;
import org.blackninja745studios.lightweighthomes.LightweightHomes;
import org.blackninja745studios.lightweighthomes.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetHomeCommand extends Command {
    private static final String COMMAND_NAME = "sethome";

    private final LightweightHomes plugin;

    public SetHomeCommand(LightweightHomes plugin) {
        super(COMMAND_NAME, "Sets a player's home.", "/sethome [<player>] [<x> <y> <z> <world>]", List.of());
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        switch (args.length) {
            case 0 -> {
                if (sender instanceof Player player) {
                    if (!player.hasPermission(Permissions.SET_HOME)) {
                        player.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                        return true;
                    }

                    Location loc = player.getLocation();
                    createHome(player, loc);

                    player.sendMessage(LightweightHomes.addPluginPrefix(Component.text(String.format(
                        "Created home at (%.1f, %.1f, %.1f).",
                        loc.getX(), loc.getY(), loc.getZ()
                    ), NamedTextColor.WHITE)));
                } else {
                    sender.sendMessage(CommandError.messageOf(CommandError.NOT_PLAYER));
                }
            }
            case 1 -> {
                if (sender instanceof Player player) {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target != null && target.getUniqueId().equals(player.getUniqueId())) {
                        return player.performCommand(COMMAND_NAME);
                    }

                    if (!sender.hasPermission(Permissions.MANAGE_HOMES)) {
                        sender.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                        return true;
                    }

                    if (target != null) {
                        Location loc = player.getLocation();
                        createHome(target, loc);

                        player.sendMessage(LightweightHomes.addPluginPrefix(Component.text(String.format(
                                "Created home for %s at (%.1f, %.1f, %.1f).",
                                PlainTextComponentSerializer.plainText().serialize(target.displayName()),
                                loc.getX(), loc.getY(), loc.getZ()
                        ), NamedTextColor.WHITE)));
                    } else {
                        sender.sendMessage(CommandError.messageOf(CommandError.OFFLINE_PLAYER));
                    }
                } else {
                    sender.sendMessage(CommandError.messageOf(CommandError.NOT_PLAYER));
                }
            }
            case 5 -> {
                if (!sender.hasPermission(Permissions.MANAGE_HOMES)) {
                    sender.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                Location loc = parseLocationFromArgs(args);

                if (target == null) {
                    sender.sendMessage(CommandError.messageOf(CommandError.OFFLINE_PLAYER));
                    return true;
                }

                if (loc == null) {
                    sender.sendMessage(CommandError.messageOf(CommandError.INVALID_ARGUMENTS));
                    return true;
                }

                createHome(target, loc);

                sender.sendMessage(LightweightHomes.addPluginPrefix(Component.text(String.format(
                        "Created home for %s at (%.1f, %.1f, %.1f) in world \"%s.\"",
                        PlainTextComponentSerializer.plainText().serialize(target.displayName()),
                        loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName()
                ), NamedTextColor.WHITE)));
            }
            default -> sender.sendMessage(CommandError.messageOf(CommandError.INVALID_ARGUMENTS));
        }

        return true;
    }

    private void createHome(Player player, Location l) {
        PersistentDataContainer data = player.getPersistentDataContainer();

        data.set(new NamespacedKey(plugin, LightweightHomes.PDS_KEY_PREFIX + "x"), PersistentDataType.DOUBLE, l.getX());
        data.set(new NamespacedKey(plugin, LightweightHomes.PDS_KEY_PREFIX + "y"), PersistentDataType.DOUBLE, l.getY());
        data.set(new NamespacedKey(plugin, LightweightHomes.PDS_KEY_PREFIX + "z"), PersistentDataType.DOUBLE, l.getZ());
        data.set(new NamespacedKey(plugin, LightweightHomes.PDS_KEY_PREFIX + "world"), PersistentDataType.STRING, l.getWorld().getUID().toString());
    }

    private static Location parseLocationFromArgs(String[] args) {
        if (args.length != 5)
            return null;

        double x, y, z;

        try {
            x = Double.parseDouble(args[1]);
            y = Double.parseDouble(args[2]);
            z = Double.parseDouble(args[3]);
        } catch (NumberFormatException nfe) {
            return null;
        }

        World world = Bukkit.getWorld(args[4]);

        return world == null ? null : new Location(world, x, y, z);
    }
}
