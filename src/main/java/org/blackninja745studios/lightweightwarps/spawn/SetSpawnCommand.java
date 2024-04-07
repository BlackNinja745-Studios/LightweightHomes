package org.blackninja745studios.lightweightwarps.spawn;

import org.blackninja745studios.lightweightwarps.LightweightWarps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetSpawnCommand extends Command {
    private final LightweightWarps plugin;
    private final Permission setSpawn = new Permission("lightweightwarps.spawn.set");

    public SetSpawnCommand(LightweightWarps plugin) {
        super("setspawn", "Sets the server or world's spawn.", "/setspawn [<x> <y> <z>] [world]", List.of());
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return false;
    }
}
