package men.brakh.agario.model.game;

import men.brakh.agario.model.communicator.Communicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameField {
    private Map<Person, Communicator> persons = new HashMap<>();

    public Person add(Person person, Communicator communicator) {
        persons.put(person, communicator);



        return person;
    }

}
