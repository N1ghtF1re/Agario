package men.brakh.agario.model.message;

import men.brakh.agario.model.enums.ChangingType;
import men.brakh.agario.model.game.Person;

/**
 * Класс сообщений для общения с клиентом
 */
public class Message {
    /**
     * Тип изменения в игре (убийство персонажа, перемещение и тд)
     */
    private ChangingType changingType;

    /**
     * Персонаж, с которым случились изменения
     */
    private Person person;

    public Message() {
    }

    public Message(ChangingType changingType, Person person) {
        this.changingType = changingType;
        this.person = person;
    }

    public ChangingType getChangingType() {
        return changingType;
    }

    public void setChangingType(ChangingType changingType) {
        this.changingType = changingType;
    }

    public Person getValue() {
        return person;
    }

    public void setValue(Person person) {
        this.person = person;
    }
}