package com.hui.websocket;

/**
 * WebSocket 支持 双向通信，即服务器可以主动向客户端推送数据，而客户端也可以随时向服务器发送数据
 * */

import com.hui.config.WebSocketConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/ws/{sid}")
@Slf4j
@Component
public class WebSocketServer {
    private String userName;
    private Session session;

    //存放会话对象
    private static Map<String, Session> sessionMap = new HashMap();

    /**
     * To execute when it connected
     * @param
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("user_name") String userName, Session session) {
        this.userName = userName;
        this.session = session;
        log.info("客户端:{} 建立练习",session);
        WebSocketConfiguration.serverClients.put(session.getId(), this);
    }

    /**
     * To execute when get message
     * @param message
     * @param session
     * @return
     */
    @OnMessage
    public String onMessage(String message, Session session) {
        System.out.println(this.userName + "：" + message);
        return this.userName + "：" + message;
    }

    /**
     * To execute when closed the connector
     * @param session
     * @param closeReason
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        WebSocketConfiguration.serverClients.remove(session.getId());
        System.out.println(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    /**
     * To execute when an error occurs
     * @param t
     */
    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 群发
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}