import React, {
  createContext,
  useContext,
  useEffect,
  useRef,
  useState,
} from "react";
import { message } from "antd";

const WebSocketContext = createContext(null);

export const WebSocketProvider = ({ children }) => {
  const socketRef = useRef(null);

  const connect = (userId) => {
    if (!socketRef.current || socketRef.current.readyState !== WebSocket.OPEN) {
      const websocketUrl = `${process.env.REACT_APP_WEBSOCKET_URL}/${userId}`;
      const newSocket = new WebSocket(websocketUrl);

      newSocket.onopen = () => {
        console.log("WebSocket 连接已打开");
      };

      newSocket.onmessage = (event) => {
        console.log("收到消息:", event.data);
        message.success(event.data);
      };

      newSocket.onclose = (event) => {
        console.log("WebSocket 连接已关闭", event);
      };

      newSocket.onerror = (error) => {
        console.error("WebSocket 发生错误:", error);
      };

      socketRef.current = newSocket;
    }
  };

  const close = () => {
    if (socketRef.current) {
      socketRef.current.close();
      socketRef.current = null;
    }
  };

  useEffect(() => {
    const userId = sessionStorage.getItem("userId");
    if (userId) {
      connect(userId);
    }

    return () => {
      close();
    };
  }, []);

  return (
    <WebSocketContext.Provider
      value={{ socket: socketRef.current, connect, close }}
    >
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = () => useContext(WebSocketContext);
