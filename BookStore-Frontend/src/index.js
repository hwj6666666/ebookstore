import ReactDOM from "react-dom/client";
import App from "./App";
import "./css/global.css";
import { WebSocketProvider } from "./websocket/WebSocketContext";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <WebSocketProvider>
    <App />
  </WebSocketProvider>
);
