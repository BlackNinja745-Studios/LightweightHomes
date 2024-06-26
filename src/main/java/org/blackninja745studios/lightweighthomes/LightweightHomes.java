package org.blackninja745studios.lightweighthomes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.blackninja745studios.lightweighthomes.commands.UseHomeCommand;
import org.blackninja745studios.lightweighthomes.commands.RemoveHomeCommand;
import org.blackninja745studios.lightweighthomes.commands.SetHomeCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public final class LightweightHomes extends JavaPlugin {

    @Override
    public void onEnable() {
        Permissions.initialize();

        HomeDataManager dataManager = new HomeDataManager(this);

        getServer().getCommandMap().registerAll("lightweighthomes", List.of(
            new UseHomeCommand(dataManager),
            new SetHomeCommand(dataManager),
            new RemoveHomeCommand(dataManager)
        ));

        getLogger().log(Level.INFO, "Finished initializing Lightweight Homes!");
    }

    @Override
    public void onDisable() { }

    public static Component addPluginPrefix(Component c) {
        return Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("Lightweight", NamedTextColor.GREEN))
                .append(Component.text("Homes", NamedTextColor.GOLD))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(c);
    }
}
