package men.brakh.agario;

import men.brakh.agario.model.communicator.Communicator;
import men.brakh.agario.model.communicator.SessionCommunicator;
import men.brakh.agario.model.enums.ChangingType;
import men.brakh.agario.model.game.GameField;
import men.brakh.agario.model.message.Message;
import men.brakh.agario.model.message.MessageDecoder;
import men.brakh.agario.model.message.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(
        value="/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
public class GameEndpoint {
    private static GameField gameField = new GameField();

    private Communicator communicator;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        communicator = new SessionCommunicator(session);
        gameField.add(username, communicator);
        System.out.println(username + communicator.toString());
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        System.out.println(message);

        System.out.println(message.getChangingType());

        switch (message.getChangingType()) {
            case COORDS_CHANGING:
                gameField.move(communicator, message.getValue().getCenter());

            default:
                System.out.println("BUG");
        }
        System.out.println(message.getValue().getUsername() + communicator.toString());
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

}