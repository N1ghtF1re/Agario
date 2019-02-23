package men.brakh.agario.model.game;

import men.brakh.agario.config.GameConfig;
import men.brakh.agario.model.Point;
import men.brakh.agario.model.communicator.Communicator;
import men.brakh.agario.model.communicator.EmptyCommunicator;
import men.brakh.agario.model.enums.ChangingType;
import men.brakh.agario.model.message.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameField {
    private GameConfig config = GameConfig.getInstance();

    private Map<Communicator, Person> persons = new ConcurrentHashMap<>();

    private volatile int lastId = 0;

    private int width = 1080;
    private int height = 720;

    public Person spawnMob() {
        Person mob = Person.newBuilder()
                .setUsername("mob")
                .setCenter(getFreePoint())
                .setId(getNewId())
                .setColor(getRandColor())
                .setSize(10)
                .build();

        persons.put(new EmptyCommunicator(), mob);
        broadcast(new Message(ChangingType.SPAWN, mob));
        return mob;
    }

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
                (communicator, currPerson) -> {
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

        persons.put(communicator, person);
        broadcast(new Message(ChangingType.SPAWN, person));

        persons.forEach(
                (otherCommunicator, otherPerson) -> {
                    if(!person.equals(otherPerson)) {
                        communicator.send(new Message(ChangingType.SPAWN, otherPerson));
                    }
                }
        );

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
                (communicator, secondPerson) -> {
                    if(person.isIntersect(secondPerson) && !person.equals(secondPerson)) {
                        Person deadPerson;
                        Person extendedPerson;

                        if(person.getSize() > secondPerson.getSize()) {
                            deadPerson = secondPerson;
                            extendedPerson = person;
                        } else if(person.getSize() < secondPerson.getSize()){
                            deadPerson = person;
                            isDead[0] = true;
                            extendedPerson = secondPerson;
                        } else {
                            return;
                        }

                        extendedPerson.eat(deadPerson);
                        broadcast(new Message(ChangingType.SIZE_CHANGING, extendedPerson));
                        broadcast(new Message(ChangingType.DEAD, deadPerson));
                    }
                }
        );

        return isDead[0];
    }

    public Communicator getCommunicator(Person person) {
        Communicator[] communicator = {null};
        persons.forEach(
                (communicator1, person1) -> {
                    if(person.equals(person1)) {
                        communicator[0] = communicator1;
                    }
                }
        );

        return communicator[0];
    }

    /**
     * Перемещение персонажа
     */
    public void move(Communicator communicator, Point newPoint) {
        Person person = persons.get(communicator);
        // TODO: CHEATING CHECKING
        person.changeCenter(newPoint);

        if(checkForIntersect(person)) {
            return;
        }

        broadcast(new Message(ChangingType.COORDS_CHANGING, person));
    }

    /**
     * Отправка уведомления всем пользователям
     * @param message Объект сообщения
     */
    private void broadcast(Message message) {
        persons.forEach(
                (communicator, person) -> {
                    communicator.send(message);
                }
        );
    }
}
