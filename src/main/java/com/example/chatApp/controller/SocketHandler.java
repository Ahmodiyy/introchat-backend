package com.example.chatApp.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final List<Message> messages = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        System.out.println("payload  "+ message.getPayload());
        Message msg = gson.fromJson(message.getPayload(), Message.class);
        System.out.println("msf from "+ msg.username);
        System.out.println("msg text  "+ msg.message);
        messages.add(msg);
        for(WebSocketSession webSocketSession : sessions) {
            System.out.println("messages "+ gson.toJson(messages));
            webSocketSession.sendMessage(new TextMessage(gson.toJson(messages)));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

}