package men.brakh.agario.model;

public class Person {
    private int size;
    private String username;

    public Person() {
    }

    public Person(String username) {
        this.username = username;
        this.size = 20;
    }

    public void eat(Person person) {
        if(this.size > person.size) {
            this.size += person.size;
            person.size = -1;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
