package de.neocraftr.labychatapi.events;

import com.mojang.authlib.GameProfile;
import net.labymod.labyconnect.packets.PacketMessage;

public interface LabyChatMessageEvent {
    void onMessage(GameProfile sender, String message, PacketMessage rawPacket);
}
