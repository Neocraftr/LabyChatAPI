package de.neocraftr.labychatapi.events;

import com.mojang.authlib.GameProfile;
import net.labymod.labyconnect.packets.PacketPlayPlayerOnline;

public interface LabyChatPlayerOnlineEvent {
    void onOnlineState(GameProfile player, boolean isOnline, PacketPlayPlayerOnline rawPacket);
}
