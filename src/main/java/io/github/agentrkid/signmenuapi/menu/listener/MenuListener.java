package io.github.agentrkid.signmenuapi.menu.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import io.github.agentrkid.signmenuapi.SignMenuAPI;
import io.github.agentrkid.signmenuapi.menu.SignMenu;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener extends PacketAdapter implements Listener {
    public MenuListener() {
        super(SignMenuAPI.getInstance(), PacketType.Play.Client.UPDATE_SIGN);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();

        SignMenu menu = SignMenu.openedMenus.get(player.getUniqueId());

        if (menu != null) {
            WrappedChatComponent[] components = packet.getChatComponentArrays().read(0);

            // Translate our chat components to text
            final String[] lines = new String[] { ComponentSerializer.parse(components[0].getJson())[0].toPlainText(), ComponentSerializer.parse(components[1].getJson())[0].toPlainText(),
                    ComponentSerializer.parse(components[2].getJson())[0].toPlainText(), ComponentSerializer.parse(components[3].getJson())[0].toPlainText() };

            // Handle the response
            menu.getResponseConsumer().accept(lines);

            // We need to close so it replaces the sign block for the player.
            menu.close(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SignMenu.openedMenus.remove(event.getPlayer().getUniqueId());
    }
}
