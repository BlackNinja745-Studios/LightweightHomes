package org.blackninja745studios.lightweighthomes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandError {
    public static final String NO_PERMISSIONS = "You do not have permission to use this command.";
    public static final String INVALID_ARGUMENTS = "Invalid arguments.";
    public static final String NOT_PLAYER = "This command can only be executed by a player.";
    public static final String OFFLINE_PLAYER = "That player isn't online.";

    public static Component messageOf(String message) {
        return LightweightHomes.addPluginPrefix(
                Component.text(message, NamedTextColor.RED)
        );
    }
}
