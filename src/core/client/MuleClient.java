package core.client;

import core.Utils;
import core.models.Response;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MuleClient extends WebSocketClient {

    public MuleClient(URI serverURi) {
        super(serverURi);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connection opened");
    }

    @Override
    public void onMessage(String json) {
        Response response = Utils.getGson().fromJson(json, Response.class);
        if (response.hasSupplies()) {
            System.out.println("has supplies");
//            API.muling = true;
//            Utils.setMuleName(response.getName());
        } else {
            System.out.println("no supplies");
//            API.muling = false;
//            API.logout();
//            close();
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Connection closed");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Error occurred");
        e.printStackTrace();
    }
}
