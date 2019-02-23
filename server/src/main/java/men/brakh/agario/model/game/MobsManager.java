package men.brakh.agario.model.game;

import men.brakh.agario.config.GameConfig;
import men.brakh.agario.model.communicator.CallbackCommunicator;
import men.brakh.agario.model.communicator.Communicator;
import men.brakh.agario.model.enums.ChangingType;

import java.util.ArrayList;
import java.util.List;

public class MobsManager {
    private GameConfig config = GameConfig.getInstance();
    private GameField field;
    private List<Person> mobs = new ArrayList<>();

    private void spawn() {
        Communicator communicator = new CallbackCommunicator(
                () -> {spawn(); return null;},
                (message, thisCommunicator) -> message.getChangingType() == ChangingType.DEAD &&
                        thisCommunicator.equals(field.getCommunicator(message.getValue()))
        );

        Person mob = field.add("mob", communicator, config.getMobSize());
    }

    MobsManager(GameField field) {
        this.field = field;
        for(int i = 0; i < config.getMobsCount(); i++) {
            spawn();
        }
    }
}
