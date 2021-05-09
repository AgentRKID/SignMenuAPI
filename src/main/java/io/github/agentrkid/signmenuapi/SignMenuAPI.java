package io.github.agentrkid.signmenuapi;

import com.comphenix.protocol.ProtocolLibrary;
import io.github.agentrkid.signmenuapi.menu.listener.MenuListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SignMenuAPI extends JavaPlugin {
    @Getter private static SignMenuAPI instance;

    @Override
    public void onEnable() {
        instance = this;

        MenuListener menuListener = new MenuListener();

        Bukkit.getPluginManager().registerEvents(menuListener, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(menuListener);
    }
}
