package me.pjoh3.monhinemc;

import me.pjoh3.monhinemc.item.listener.ItemListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MonhineApi extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("api successful enabled");
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("api disabled");
    }
}
