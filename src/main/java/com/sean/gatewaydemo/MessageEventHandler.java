package com.sean.gatewaydemo;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageEventHandler {
    public static SocketIOServer socketIoServer;

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        MessageEventHandler.socketIoServer = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println(client.getSessionId() + " connect");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        client.getAllRooms().forEach(client::leaveRoom);
        client.disconnect();
        System.out.println(client.getSessionId() + " disconnect");
    }

    @OnEvent("message")
    public void message(SocketIOClient client, AckRequest request, MessageEntity message) {
        System.out.printf("receive msg: %s %s", message.getIsin(), message.getCorrelationId());
        if(client.getAllRooms().isEmpty()){
            client.sendEvent("!!!!!!!!");
        }else {
            String room = client.getAllRooms().stream().findAny().get();
            socketIoServer.getRoomOperations(room).sendEvent("!!!!!!");
        }
    }

    @OnEvent("room")
    public void onJoinRoom(SocketIOClient client, String room) {
        client.joinRoom(room);
        System.out.println("A client joined room: " + room);
    }
}
