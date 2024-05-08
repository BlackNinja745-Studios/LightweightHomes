package org.blackninja745studios.lightweighthomes;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Permissions {
    private static final String PREFIX = "lightweighthomes.";

    public static final Permission USE_HOME = new Permission(PREFIX + "usehome");
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
