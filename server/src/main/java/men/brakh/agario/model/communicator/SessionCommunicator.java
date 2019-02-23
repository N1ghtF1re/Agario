package men.brakh.agario.model.communicator;

import men.brakh.agario.model.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

public class SessionCommunicator implements Communicator {
    private Session session;
    private static Logger logger = LoggerFactory.getLogger(SessionCommunicator.class);

    public SessionCommunicator(Session session) {
        this.session = session;
    }

    @Override
    public void send(Message message) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            logger.error("Session communication error", e);
        }
    }
}
