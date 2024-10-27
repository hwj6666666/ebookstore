package com.reins.bookstore.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/bookstore/{userId}")
@Component
public class WebSocketServer {

  private static final ConcurrentHashMap<String, Session> SESSIONS = new ConcurrentHashMap<>();

  public WebSocketServer() {}

  public void sendMessage(Session toSession, String message) {
    if (toSession != null) {
      try {
        toSession.getBasicRemote().sendText(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("对方不在线");
    }
  }

  public void sendMessageToUser(String userId, String message) {
    System.out.println(userId);
    Session toSession = SESSIONS.get(userId);
    sendMessage(toSession, message);
    System.out.println(message);
  }

  @OnMessage
  public void onMessage(String message) {
    System.out.println("服务器收到消息：" + message);
  }

  @OnOpen
  public void onOpen(Session session, @PathParam("userId") String userId) {
    if (SESSIONS.get(userId) != null) {
      System.out.println("已经上线过了");
      return;
    }
    SESSIONS.put(userId.trim(), session);
    System.out.println(userId + "上线了");
  }

  @OnClose
  public void onClose(@PathParam("userId") String userId) {
    SESSIONS.remove(userId);
    System.out.println(userId + "下线了");
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    System.out.println("发生错误");
    throwable.printStackTrace();
  }
}
