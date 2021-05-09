package io.github.agentrkid.signmenuapi;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import io.github.agentrkid.signmenuapi.menu.SignMenu;
import io.github.agentrkid.signmenuapi.menu.listener.MenuListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SignMenuAPI extends JavaPlugin implements Listener {
    @Getter private static SignMenuAPI instance;

    @Override
    public void onEnable() {
        instance = this;

        MenuListener menuListener = new MenuListener();

        Bukkit.getPluginManager().registerEvents(menuListener, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(menuListener);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        new SignMenu(lines -> event.getPlayer().sendMessage(lines[0])).open(event.getPlayer(), new WrappedChatComponent[] { SignMenu.EMPTY_CHAT_COMPONENT, WrappedChatComponent.fromText("^^^^^"), WrappedChatComponent.fromText("Gamer?"), WrappedChatComponent.fromText("!!!!!!") });
    }
}
