package men.brakh.agario;

import men.brakh.agario.model.Message;
import men.brakh.agario.model.MessageDecoder;
import men.brakh.agario.model.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(
        value="/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
public class GameEndpoint {

    private Session session;

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) throws IOException {

        this.session = session;
        System.out.println(username);
    }

    @OnMessage
    public void onMessage(Session session, Message message)
            throws IOException {
        /*
        message.setFrom(users.get(session.getId()));
        broadcast(message);
        */
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        /*
        chatEndpoints.remove(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
        */
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    /*
    private static void broadcast(Message message)
            throws IOException, EncodeException {

        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}