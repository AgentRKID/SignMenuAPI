package io.github.agentrkid.signmenuapi.menu;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class SignMenu {
    public static final Map<UUID, SignMenu> openedMenus = new HashMap<>();

    public static final WrappedChatComponent EMPTY_CHAT_COMPONENT = WrappedChatComponent.fromText("");

    private static final BlockPosition POS_ZERO = new BlockPosition(0, 0, 0);

    private static final PacketContainer SIGN_PACKET;
    private static final PacketContainer OPEN_SIGN_PACKET;
    private static final PacketContainer REPLACE_SIGN_PACKET;

    @Getter private final Consumer<String[]> responseConsumer;

    public SignMenu(Consumer<String[]> responseConsumer) {
        this.responseConsumer = responseConsumer;
    }

    /**
     * Opens a sign gui
     *
     * @param player the player to open the gui for.
     * @param components the components for lines to show.
     */
    public void open(Player player, WrappedChatComponent[] components) {
        if (components.length < 4) {
            throw new RuntimeException("Lines must be greater or equal to four.");
        }

        PacketContainer updateSignPacket = new PacketContainer(PacketType.Play.Server.UPDATE_SIGN);
        updateSignPacket.getBlockPositionModifier().write(0, POS_ZERO);
        updateSignPacket.getChatComponentArrays().write(0, components);

        try {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

            protocolManager.sendServerPacket(player, SIGN_PACKET);
            protocolManager.sendServerPacket(player, updateSignPacket);
            protocolManager.sendServerPacket(player, OPEN_SIGN_PACKET);

            openedMenus.put(player.getUniqueId(), this);
        } catch (Exception ignored) {}
    }

    /**
     * Replaces the block that was used to bedrock when done.
     *
     * @param player the player to send the change to
     */
    public void close(Player player) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, REPLACE_SIGN_PACKET);

            openedMenus.remove(player.getUniqueId());
        } catch (Exception ignored) {}
    }

    static {
        SIGN_PACKET = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
        SIGN_PACKET.getBlockPositionModifier().write(0, POS_ZERO);
        SIGN_PACKET.getBlockData().write(0, WrappedBlockData.createData(Material.SIGN_POST));

        OPEN_SIGN_PACKET = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        OPEN_SIGN_PACKET.getBlockPositionModifier().write(0, POS_ZERO);

        REPLACE_SIGN_PACKET = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
        REPLACE_SIGN_PACKET.getBlockPositionModifier().write(0, POS_ZERO);
        REPLACE_SIGN_PACKET.getBlockData().write(0, WrappedBlockData.createData(Material.BEDROCK));
    }
}
