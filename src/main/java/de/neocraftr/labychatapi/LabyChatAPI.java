package de.neocraftr.labychatapi;

import com.mojang.authlib.GameProfile;
import de.neocraftr.labychatapi.events.LabyChatServerChangeEvent;
import de.neocraftr.labychatapi.events.LabyChatMessageEvent;
import de.neocraftr.labychatapi.events.LabyChatPlayerOnlineEvent;
import net.labymod.labyconnect.LabyConnect;
import net.labymod.labyconnect.log.MessageChatComponent;
import net.labymod.labyconnect.log.SingleChat;
import net.labymod.labyconnect.packets.*;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.labyconnect.user.ServerInfo;
import net.labymod.main.LabyMod;

import java.util.*;

public class LabyChatAPI {

    private static LabyChatAPI instance;
    private LabyConnect labyConnect = LabyMod.getInstance().getLabyConnect();

    private List<LabyChatMessageEvent> messageListeners = new ArrayList<>();
    private List<LabyChatServerChangeEvent> gameChangeListeners = new ArrayList<>();
    private List<LabyChatPlayerOnlineEvent> playerOnlineListeners = new ArrayList<>();

    // MessageEvent
    public void registerEvent(LabyChatMessageEvent e) {
        if(!messageListeners.contains(e)) messageListeners.add(e);
    }
    public void callMessageEvent(GameProfile sender, String message, PacketMessage rawPacket) {
        for(LabyChatMessageEvent e : messageListeners) {
            e.onMessage(sender, message, rawPacket);
        }
    }
    public static void handleMessageEvent(PacketMessage packet) {
        if(LabyChatAPI.instance != null) {
            LabyChatAPI.instance.callMessageEvent(packet.getSender().getGameProfile(), packet.getMessage(), packet);
        }
    }

    // GameChangeEvent
    public void registerEvent(LabyChatServerChangeEvent e) {
        if(!gameChangeListeners.contains(e)) gameChangeListeners.add(e);
    }
    public void callGameChangeEvent(GameProfile player, ServerInfo serverInfo, PacketPlayFriendStatus rawPacket) {
        for(LabyChatServerChangeEvent e : gameChangeListeners) {
            e.onGameChange(player, serverInfo, rawPacket);
        }
    }
    public static void handleServerChangeEvent(PacketPlayFriendStatus packet) {
        if(LabyChatAPI.instance != null) {
            LabyChatAPI.instance.callGameChangeEvent(packet.getPlayer().getGameProfile(), packet.getPlayerInfo(), packet);
        }
    }

    // PlayerOnlineStateEvent
    public void registerEvent(LabyChatPlayerOnlineEvent e) {
        if(!playerOnlineListeners.contains(e)) playerOnlineListeners.add(e);
    }
    public void callPlayerOnlineEvent(GameProfile player, boolean isOnline, PacketPlayPlayerOnline rawPacket) {
        for(LabyChatPlayerOnlineEvent e : playerOnlineListeners) {
            e.onOnlineState(player, isOnline, rawPacket);
        }
    }
    public static void handlePlayerOnlineEvent(PacketPlayPlayerOnline packet) {
        if(LabyChatAPI.instance != null) {
            LabyChatAPI.instance.callPlayerOnlineEvent(packet.getPlayer().getGameProfile(), packet.getPlayer().isOnline(), packet);
        }
    }


    public void sendMessage(UUID targetUUID, String message) {
        ChatUser target = labyConnect.getChatUserByUUID(targetUUID);
        SingleChat chat = labyConnect.getChatlogManager().getChat(target);
        MessageChatComponent messageComponent = new MessageChatComponent(LabyMod.getInstance().getPlayerName(), System.currentTimeMillis(), message);
        chat.addMessage(messageComponent);
    }

    public void setCurrentPlaying(boolean show, String gameMode) {
        if(show) {
            labyConnect.updatePlayingOnServerState(gameMode);
        } else {
            labyConnect.updatePlayingOnServerState(null);
        }
    }

    public boolean isConnected() {
        return labyConnect.isOnline();
    }

    public void connect() {
        if(isConnected()) return;
        labyConnect.getClientConnection().connect();
    }

    public void disconnect() {
        if(!isConnected()) return;
        labyConnect.getClientConnection().disconnect(false);
    }

    public static LabyChatAPI getInstance() {
        if(instance == null) {
            instance = new LabyChatAPI();
        }
        return instance;
    }
}
