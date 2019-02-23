package men.brakh.agario.model.game;

import men.brakh.agario.model.Point;

import java.util.Objects;

/**
 * Класс персонажа игры
 */
public class Person {
    /**
     * ID Персонажа
     */
    private int id;

    /**
     * Размер персонажа
     */
    private int size;

    /**
     * Имя персонажа
     */
    private String username;

    /**
     * Цвет персонажа
     */
    private String color;

    /**
     * Координаты центра персонажа
     */
    private Point center;


    public Person() {
    }

    public Person(String username) {
        this.username = username;
        this.size = 20;
        this.color = "#000";
    }

    /**
     * Съесть другого персонажа
     * @param person другой персонаж
     */
    public void eat(Person person) {
        if(this.size > person.size) {
            this.size += person.size / 2;
            person.size = -1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
