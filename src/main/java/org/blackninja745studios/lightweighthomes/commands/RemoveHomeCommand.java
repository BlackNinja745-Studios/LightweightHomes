package org.blackninja745studios.lightweighthomes.commands;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.blackninja745studios.lightweighthomes.CommandError;
import org.blackninja745studios.lightweighthomes.LightweightHomes;
import org.blackninja745studios.lightweighthomes.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class RemoveHomeCommand extends Command {
    private static final String COMMAND_NAME = "removehome";

    private final LightweightHomes plugin;

    public RemoveHomeCommand(LightweightHomes plugin) {
        super(COMMAND_NAME, "Deletes a player's home.", "/removehome [<player>]", List.of());
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        switch (args.length) {
            case 0 -> {
                if (sender instanceof Player player) {
                    if (!player.hasPermission(Permissions.REMOVE_HOME)) {
                        player.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                        return true;
                    }

                    if (removeHome(player)) {
                        player.sendMessage(LightweightHomes.addPluginPrefix(
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

                if (!sender.hasPermission(Permissions.MANAGE_HOMES)) {
                    sender.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                    return true;
                }

                if (target == null) {
                    sender.sendMessage(CommandError.messageOf(CommandError.OFFLINE_PLAYER));
                } else {
                    if (removeHome(target)) {
                        sender.sendMessage(LightweightHomes.addPluginPrefix(Component.text(
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

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission(Permissions.REMOVE_HOME))
            return ImmutableList.of();

        if (sender.hasPermission(Permissions.MANAGE_HOMES) && args.length == 1) {
            return sender.getServer()
                    .getOnlinePlayers()
                    .stream()
                    .filter(p -> LightweightHomes.hasHomeData(p, plugin))
                    .map(Player::getName)
                    .filter(n -> StringUtil.startsWithIgnoreCase(n, args[0]))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
        }

        return ImmutableList.of();
    }

    private boolean removeHome(Player p) {
        PersistentDataContainer data = p.getPersistentDataContainer();

        final String[] keys = { LightweightHomes.PDS_KEY_PREFIX + "x", LightweightHomes.PDS_KEY_PREFIX + "y", LightweightHomes.PDS_KEY_PREFIX + "z" };

        for (String key : keys)
            if (data.has(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE))
                data.remove(new NamespacedKey(plugin, key));

        boolean hasWorldKey = data.has(new NamespacedKey(plugin, LightweightHomes.PDS_KEY_PREFIX + "world"), PersistentDataType.STRING);
        if (hasWorldKey)
            data.remove(new NamespacedKey(plugin, LightweightHomes.PDS_KEY_PREFIX + "world"));

        return hasWorldKey;
    }
}
