package pa.greenvox.ru.uhc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pa.greenvox.ru.uhc.event.UHCEvents;
import pa.greenvox.ru.uhc.startup.UHCStartCommand;

public final class UHC extends JavaPlugin {

    public static UHC Instance;

    @Override
    public void onEnable() {
        Instance = this;

        Bukkit.getPluginManager().registerEvents(new UHCEvents(), this);
        Bukkit.getPluginCommand("uhc_start").setExecutor(new UHCStartCommand());

    }

    @Override
    public void onDisable() {

    }
}
