import { useWebSocket } from "../websocket/WebSocketContext";
import { DUMMY_RESPONSE, PREFIX, put } from "./common";

export async function logout() {
  const url = `${PREFIX}/logout`;
  let res;
  try {
    res = await put(url);
    sessionStorage.removeItem("userId");
  } catch (e) {
    console.log(e);
    res = DUMMY_RESPONSE;
  }
  return res;
}