package de.neocraftr.labychatapi.events;

import com.mojang.authlib.GameProfile;
import net.labymod.labyconnect.packets.PacketPlayFriendStatus;
import net.labymod.labyconnect.user.ServerInfo;

public interface LabyChatServerChangeEvent {
    void onGameChange(GameProfile player, ServerInfo serverInfo, PacketPlayFriendStatus rawPacket);
}
