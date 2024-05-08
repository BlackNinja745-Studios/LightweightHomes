package org.blackninja745studios.lightweightwarps.home;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.blackninja745studios.lightweightwarps.CommandError;
import org.blackninja745studios.lightweightwarps.LightweightWarps;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RemoveHomeCommand extends Command {
    private static final String COMMAND_NAME = "removehome";

    private final LightweightWarps plugin;

    public RemoveHomeCommand(LightweightWarps plugin) {
        super(COMMAND_NAME, "Deletes a player's home.", "/removehome [<player>]", List.of());
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        switch (args.length) {
            case 0 -> {
                if (sender instanceof Player player) {
                    if (!player.hasPermission(HomePermissions.REMOVE_HOME)) {
                        player.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                        return true;
                    }

                    if (removeHome(player)) {
                        player.sendMessage(LightweightWarps.addPluginPrefix(
                                Component.text("Removed your home!", NamedTextColor.WHITE)
                        ));
                    } else {
                        player.sendMessage(CommandError.messageOf("You do not have a home yet!"));
                    }
                } else {
                    sender.sendMessage(CommandError.messageOf("Specify the player whose house to delete."));
                }
            }
            case 1 -> {
                Player target = Bukkit.getPlayer(args[0]);

                if (sender instanceof Player player) {
                    if (target != null && target.getUniqueId().equals(player.getUniqueId())) {
                        return player.performCommand(COMMAND_NAME);
                    }
                }

                if (!sender.hasPermission(HomePermissions.MANAGE_HOMES)) {
                    sender.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                    return true;
                }

                if (target == null) {
                    sender.sendMessage(CommandError.messageOf(CommandError.OFFLINE_PLAYER));
                } else {
                    if (removeHome(target)) {
                        sender.sendMessage(LightweightWarps.addPluginPrefix(Component.text(
                            PlainTextComponentSerializer.plainText().serialize(target.displayName()) + "'s home removed!", NamedTextColor.WHITE
                        )));
                    } else {
                        sender.sendMessage(CommandError.messageOf(
                                PlainTextComponentSerializer.plainText().serialize(target.displayName()) + " doesn't have a home yet."
                        ));
                    }
                }
            }
            default -> sender.sendMessage(CommandError.messageOf(CommandError.INVALID_ARGUMENTS));
        }

        return true;
    }

    private boolean removeHome(Player p) {
        PersistentDataContainer data = p.getPersistentDataContainer();

        final String KEY_PREFIX = "lightweightwarps.home.";
        String[] keys = { KEY_PREFIX + "x", KEY_PREFIX + "y", KEY_PREFIX + "z" };

        for (String key : keys)
            if (data.has(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE))
                data.remove(new NamespacedKey(plugin, key));

        boolean hasWorldKey = data.has(new NamespacedKey(plugin, KEY_PREFIX + "world"), PersistentDataType.STRING);
        if (hasWorldKey)
            data.remove(new NamespacedKey(plugin, KEY_PREFIX + "world"));

        return hasWorldKey;
    }
}
