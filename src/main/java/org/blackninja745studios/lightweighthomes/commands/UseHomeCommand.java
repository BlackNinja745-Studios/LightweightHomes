package org.blackninja745studios.lightweighthomes.commands;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.blackninja745studios.lightweighthomes.CommandError;
import org.blackninja745studios.lightweighthomes.HomeDataManager;
import org.blackninja745studios.lightweighthomes.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UseHomeCommand extends Command {

    private static final String COMMAND_NAME = "home";
    private final HomeDataManager dataManager;

    public UseHomeCommand(HomeDataManager dataManager) {
        super(COMMAND_NAME, "Returns a calling player to their set home.", "/home [<player>]", List.of());
        this.dataManager = dataManager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (args.length) {
                case 0 -> {
                    if (!player.hasPermission(Permissions.USE_HOME)) {
                        player.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                        return true;
                    }

                    if (!sendPlayerHome(player, player)) {
                        player.sendMessage(CommandError.messageOf("You do not have a home yet!"));
                    }
                }
                case 1 -> {
                    Player owner = Bukkit.getPlayer(args[0]);

                    if (owner != null && owner.getUniqueId().equals(player.getUniqueId())) {
                        return player.performCommand(COMMAND_NAME);
                    }

                    if (!player.hasPermission(Permissions.MANAGE_HOMES)) {
                        player.sendMessage(CommandError.messageOf(CommandError.NO_PERMISSIONS));
                        return true;
                    }

                    if (owner == null) {
                        player.sendMessage(CommandError.messageOf(CommandError.OFFLINE_PLAYER));
                    } else {
                        if (!sendPlayerHome(player, owner)) {
                            player.sendMessage(CommandError.messageOf(
                                    PlainTextComponentSerializer.plainText().serialize(owner.displayName()) + " does not have a home yet!"
                            ));
                        }
                    }
                }
                default -> player.sendMessage(CommandError.messageOf(CommandError.INVALID_ARGUMENTS));
            }
        } else {
            sender.sendMessage(CommandError.messageOf(CommandError.NOT_PLAYER));
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission(Permissions.USE_HOME))
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

    private boolean sendPlayerHome(Player sender, Player owner) {
        Location targetHome = dataManager.getHomeLocation(owner.getPersistentDataContainer());
        return targetHome != null && sender.teleport(targetHome);
    }
}
