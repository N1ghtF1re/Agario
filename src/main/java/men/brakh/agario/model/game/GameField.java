package men.brakh.agario.model.game;

import men.brakh.agario.config.GameConfig;
import men.brakh.agario.model.Point;
import men.brakh.agario.model.communicator.Communicator;
import men.brakh.agario.model.enums.ChangingType;
import men.brakh.agario.model.message.Message;

import java.util.*;

public class GameField {
    private GameConfig config = GameConfig.getInstance();

    private Map<Person, Communicator> persons = new HashMap<>();

    private volatile int lastId = 0;

    private int width = 1080;
    private int height = 720;

    /**
     * Получение рандомного цвета для нового игрока
     */
    private String getRandColor() {
        String[] colors = config.getAvailableColors();
        return colors[new Random().nextInt(colors.length)];
    }

    /**
     * Получение нового id для нового игрока
     */
    private synchronized int getNewId() {
        return ++lastId;
    }

    /**
     * Получение точки, где нет игроков
     */
    private Point getFreePoint() {
        int x = new Random().nextInt(width);
        int y = new Random().nextInt(height);

        final boolean[] isFree = {true};
        persons.forEach(
                (currPerson, ignored) -> {
                    if( (x > currPerson.getCenter().getX() - currPerson.getSize()
                            && x < currPerson.getCenter().getX() + currPerson.getSize()
                        ) || (
                            y > currPerson.getCenter().getY() - currPerson.getSize()
                            && y < currPerson.getCenter().getY() + currPerson.getSize()))  {

                        isFree[0] = false;
                    }
                }
        );

        if(isFree[0]) {
            return new Point(x,y);
        }

        return getFreePoint();
    }

    /**
     * Добавление нового игрока
     * @param username Имя игрока
     * @param communicator Объект для связи с игроком
     * @return добавленный игрок
     */
    public Person add(String username, Communicator communicator) {
        Person person = Person.newBuilder()
                .setId(getNewId())
                .setUsername(username)
                .setSize(config.getSpawnSize())
                .setColor(getRandColor())
                .setCenter(getFreePoint())
                .build();

        communicator.send(new Message(ChangingType.SPAWN, person));
        persons.put(person, communicator);
        return person;
    }

    /**
     * Проверяет, не пересекаются ли персонажи. Если пересекаются - персонажи едят друг друга
     * @param person Персонаж
     * @return true, если запрашиваемый персонаж умер
     */
    public boolean checkForIntersect(Person person) {
        boolean[] isDead = {false};

        persons.forEach(
                (secondPerson, communicator) -> {
                    if(person.isIntersect(secondPerson)) {
                        Person deadPerson;
                        Person extendedPerson;

                        if(person.getSize() >= secondPerson.getSize()) {
                            deadPerson = secondPerson;
                            extendedPerson = person;
                        } else {
                            deadPerson = person;
                            isDead[0] = true;
                            extendedPerson = secondPerson;
                        }

                        extendedPerson.eat(deadPerson);
                        broadcast(new Message(ChangingType.SIZE_CHANGING, extendedPerson));
                        broadcast(new Message(ChangingType.DEAD, deadPerson));
                    }
                }
        );

        return isDead[0];
    }

    /**
     * Перемещение персонажа
     * @param person Персонаж
     * @param deltaPoints Изменение координат
     */
    public void move(Person person, Point deltaPoints) {
        if(deltaPoints.getY() > config.getMaxSpeed() || deltaPoints.getX() > config.getMaxSpeed()) {
            System.out.println("ЧИТЕР!");
        }

        if(checkForIntersect(person)) {
            return;
        }

        person.changeCenter(person.getCenter().add(deltaPoints));

        broadcast(new Message(ChangingType.COORDS_CHANGING, person));
    }

    /**
     * Отправка уведомления всем пользователям
     * @param message Объект сообщения
     */
    private void broadcast(Message message) {
        persons.forEach(
                (person, communicator) -> {
                    communicator.send(message);
                }
        );
    }
}
