import websocketService from "../websocket/WebSocketService";
import { DUMMY_RESPONSE, PREFIX, put } from "./common";

export async function logout() {
    const url = `${PREFIX}/logout`;
    let res;
    try {
        res = await put(url);
        websocketService.close();
    } catch (e) {
        console.log(e);
        res = DUMMY_RESPONSE;
    }
    return res;
}