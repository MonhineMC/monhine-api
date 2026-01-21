package me.vidosya.atwapi;

import me.vidosya.atwapi.item.listener.ItemListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class AtwApi extends JavaPlugin {

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
