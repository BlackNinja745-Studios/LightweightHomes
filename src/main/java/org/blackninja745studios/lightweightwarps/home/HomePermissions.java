package org.blackninja745studios.lightweightwarps.home;

import org.blackninja745studios.lightweightwarps.LightweightWarps;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class HomePermissions {
    private static final String PREFIX = LightweightWarps.PLUGIN_PERMISSION_PREFIX + "home.";

    public static final Permission USE_HOME = new Permission(PREFIX + "use");
    public static final Permission SET_HOME = new Permission(PREFIX + "sethome");
    public static final Permission REMOVE_HOME = new Permission(PREFIX + "removehome");
    public static final Permission MANAGE_HOMES = new Permission(PREFIX + "managehomes");

    public static void initialize() {
        USE_HOME.setDefault(PermissionDefault.TRUE);
        SET_HOME.setDefault(PermissionDefault.TRUE);
        REMOVE_HOME.setDefault(PermissionDefault.TRUE);
        MANAGE_HOMES.setDefault(PermissionDefault.OP);

        Bukkit.getPluginManager().addPermission(USE_HOME);
        Bukkit.getPluginManager().addPermission(SET_HOME);
        Bukkit.getPluginManager().addPermission(REMOVE_HOME);
        Bukkit.getPluginManager().addPermission(MANAGE_HOMES);
    }
}
