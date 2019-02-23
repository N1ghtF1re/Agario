package men.brakh.agario.model.message;

import men.brakh.agario.model.enums.ChangingType;

public class Message {
    private ChangingType changingType;
    private int value;

    public Message() {
    }

    public ChangingType getChangingType() {
        return changingType;
    }

    public void setChangingType(ChangingType changingType) {
        this.changingType = changingType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}