package men.brakh.agario.model.communicator;

import men.brakh.agario.model.message.Message;

public class EmptyCommunicator implements Communicator {
    @Override
    public void send(Message message) {}
}
