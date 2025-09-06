package com.hui.config;

/**
 * 向客户端发送信息,可能暂时用不上
 * */

import com.hui.websocket.WebSocketServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConfiguration {
    public static final Map<String, WebSocketServer> serverClients = new ConcurrentHashMap<String, WebSocketServer>();

    public static Boolean sendMessageByUser(String userName, String text) {
        try {
            for (WebSocketServer server : serverClients.values()) {
                if(userName.equals(server.getUserName()) && server.getSession() != null
                        && server.getSession().isOpen()){
                    server.getSession().getAsyncRemote().sendText(text);
                }
            }

        } catch (Exception e) {

        }
        return false;
    }

    public static void sendAllMessage(String text) {
        for (WebSocketServer server : serverClients.values()) {
            if (server.getSession() != null && server.getSession().isOpen()) {
                server.getSession().getAsyncRemote().sendText(text);
            }
        }
    }
}