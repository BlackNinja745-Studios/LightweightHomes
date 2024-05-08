package org.blackninja745studios.lightweightwarps.home;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.blackninja745studios.lightweightwarps.CommandError;
import org.blackninja745studios.lightweightwarps.LightweightWarps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.List;

public class HomeCommand extends Command {

    private static final String COMMAND_NAME = "home";
    private final LightweightWarps plugin;

    public HomeCommand(LightweightWarps plugin) {
        super(COMMAND_NAME, "Returns a calling player to their set home.", "/home", List.of());
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (args.length) {
                case 0 -> {
                    if (!player.hasPermission(HomePermissions.USE_HOME)) {
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

                    if (!player.hasPermission(HomePermissions.MANAGE_HOMES)) {
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

    private boolean sendPlayerHome(Player sender, Player owner) {
        Location targetHome = buildLocationFromData(owner.getPersistentDataContainer());
        return targetHome != null && sender.teleport(targetHome);
    }

    private Location buildLocationFromData(PersistentDataContainer data) {
        final String KEY_PREFIX = "lightweightwarps.home.";
        String[] keys = { KEY_PREFIX + "x", KEY_PREFIX + "y", KEY_PREFIX + "z"};

        double[] loc = new double[3];

        for (int i = 0; i < 3; ++i) {
            if (data.has(new NamespacedKey(plugin, keys[i]), PersistentDataType.DOUBLE))
                loc[i] = data.get(new NamespacedKey(plugin, keys[i]), PersistentDataType.DOUBLE);
            else
                return null;
        }

        String worldUID;

        if (data.has(new NamespacedKey(plugin, KEY_PREFIX + "world"), PersistentDataType.STRING))
            worldUID = data.get(new NamespacedKey(plugin, KEY_PREFIX + "world"), PersistentDataType.STRING);
        else
            return null;

        return new Location(
                plugin.getServer().getWorld(UUID.fromString(worldUID)),
                loc[0], loc[1], loc[2]
        );
    }
}
