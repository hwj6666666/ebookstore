import { message as antdMessage } from "antd";

class WebSocketService {
  constructor() {
    this.socket = null;
  }

  connect(userId) {
    if (!this.socket || this.socket.readyState !== WebSocket.OPEN) {
      this.socket = new WebSocket(`wss://localhost:8080/bookstore/${userId}`);

      this.socket.onopen = () => {
        console.log("WebSocket 连接已打开");
      };

      this.socket.onmessage = (event) => {
        console.log("收到消息:", event.data);
        this.onMessage(event.data);
      };

      this.socket.onclose = () => {
        console.log("WebSocket 连接已关闭");
      };

      this.socket.onerror = (error) => {
        console.error("WebSocket 发生错误:", error);
      };
    }
  }

  onMessage(message) {
    // 这个方法将在收到消息时被调用
    console.log("收到消息:", message);

    antdMessage.success(message);
  }

  sendMessage(message) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(message);
    } else {
      console.error("WebSocket 连接未打开");
    }
  }

  close() {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }
}

const websocketService = new WebSocketService();
export default websocketService;
