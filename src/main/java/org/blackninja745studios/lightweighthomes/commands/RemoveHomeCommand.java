package org.blackninja745studios.lightweighthomes.commands;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.blackninja745studios.lightweighthomes.CommandError;
import org.blackninja745studios.lightweighthomes.HomeDataManager;
import org.blackninja745studios.lightweighthomes.LightweightHomes;
import org.blackninja745studios.lightweighthomes.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class RemoveHomeCommand extends Command {
    private static final String COMMAND_NAME = "removehome";

    private final HomeDataManager dataManager;

    public RemoveHomeCommand(HomeDataManager dataManager) {
        super(COMMAND_NAME, "Deletes a player's home.", "/removehome [<player>]", List.of());
        this.dataManager = dataManager;
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

                    if (dataManager.removeHome(player.getPersistentDataContainer())) {
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
                    if (dataManager.removeHome(target.getPersistentDataContainer())) {
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
                    .filter(p -> dataManager.hasHomeData(p.getPersistentDataContainer()))
                    .map(Player::getName)
                    .filter(n -> StringUtil.startsWithIgnoreCase(n, args[0]))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
        }

        return ImmutableList.of();
    }
}
