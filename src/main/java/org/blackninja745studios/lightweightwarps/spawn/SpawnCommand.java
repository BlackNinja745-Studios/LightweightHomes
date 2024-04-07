package org.blackninja745studios.lightweightwarps.spawn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.blackninja745studios.lightweightwarps.LightweightWarps;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpawnCommand extends Command {

    private static final Permission use = new Permission("lightweightwarps.spawn.use");

    public SpawnCommand() {
        super("spawn", "Teleports a player to spawn.", "/spawn", List.of());
        use.setDefault(PermissionDefault.TRUE);
        Bukkit.getPluginManager().addPermission(use);
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player p) {
            p.teleport(p.getWorld().getSpawnLocation());
        } else {
            sender.sendMessage(LightweightWarps.addPluginPrefix(
                    Component.text(LightweightWarps.NOT_PLAYER, NamedTextColor.RED)
            ));
        }

        return false;
    }
}
