package game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Класс змея
 */
public class Snake {
    // Направление движения змеи
    private SnakeDirection direction;
    // Состояние - жива змея или нет.
    private boolean isAlive;
    // Список кусочков змеи.
    private ArrayList<SnakeSection> sections;

    public Snake(int x, int y, SnakeDirection direction) {
        this.direction = direction;
        sections = new ArrayList<>();
        sections.add(new SnakeSection(x, y, direction));
        sections.add(new SnakeSection(x, y - 1, direction));
        sections.add(new SnakeSection(x, y - 2, direction));
        isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getX() {
        return sections.get(0).getX();
    }

    public int getY() {
        return sections.get(0).getY();
    }

    public SnakeDirection getDirection() {
        return direction;
    }

    public void setDirection(SnakeDirection direction) {
        this.direction = direction;
    }

    public ArrayList<SnakeSection> getSections() {
        return sections;
    }

    /**
     * Метод перемещает змею на один ход.
     * Направление перемещения задано переменной direction.
     */
    public void move() {
        if (!isAlive) return;

        if (direction == SnakeDirection.UP)
            move(0, -1);
        else if (direction == SnakeDirection.RIGHT)
            move(1, 0);
        else if (direction == SnakeDirection.DOWN)
            move(0, 1);
        else if (direction == SnakeDirection.LEFT)
            move(-1, 0);
    }

    /**
     * Метод перемещает змею в соседнюю клетку.
     * Координаты клетки заданы относительно текущей головы с помощью переменных (dx, dy).
     */


    private void printBodyOrTurn(SnakeSection oldHead, SnakeSection head) {
        Map<String, ImageIcon> icons = Room.game.getUserInterface().getIcons();
        JLabel[][] panels = Room.game.getUserInterface().getPieces();

        if (head.getDirection() == oldHead.getDirection()) {
            if (oldHead.getDirection() == SnakeDirection.UP || oldHead.getDirection() == SnakeDirection.DOWN)
                panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("body-v"));
            else if (oldHead.getDirection() == SnakeDirection.LEFT || oldHead.getDirection() == SnakeDirection.RIGHT)
                panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("body-h"));
            return;
        }
        if (head.getDirection() == SnakeDirection.DOWN && oldHead.getDirection() == SnakeDirection.LEFT)
            panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("right-down"));
        else if (head.getDirection() == SnakeDirection.DOWN && oldHead.getDirection() == SnakeDirection.RIGHT)
            panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("left-down"));

        else if (head.getDirection() == SnakeDirection.UP && oldHead.getDirection() == SnakeDirection.LEFT)
            panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("right-up"));
        else if (head.getDirection() == SnakeDirection.UP && oldHead.getDirection() == SnakeDirection.RIGHT)
            panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("left-up"));

        else if (head.getDirection() == SnakeDirection.LEFT && oldHead.getDirection() == SnakeDirection.DOWN)
            panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("left-up"));
        else if (head.getDirection() == SnakeDirection.LEFT && oldHead.getDirection() == SnakeDirection.UP)
            panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("left-down"));


        else if (head.getDirection() == SnakeDirection.RIGHT && oldHead.getDirection() == SnakeDirection.DOWN)
            panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("right-up"));
        else if (head.getDirection() == SnakeDirection.RIGHT && oldHead.getDirection() == SnakeDirection.UP)
            panels[oldHead.getY()][oldHead.getX()].setIcon(icons.get("right-down"));
        oldHead.setDirection(head.getDirection());

    }

    public void printHeadOrTail(SnakeSection piece, String pName) {
        Map<String, ImageIcon> icons = Room.game.getUserInterface().getIcons();
        JLabel[][] panels = Room.game.getUserInterface().getPieces();

        if (piece.getDirection() == SnakeDirection.DOWN)
            panels[piece.getY()][piece.getX()].setIcon(icons.get(pName + "-down"));
        else if (piece.getDirection() == SnakeDirection.UP)
            panels[piece.getY()][piece.getX()].setIcon(icons.get(pName + "-up"));
        else if (piece.getDirection() == SnakeDirection.LEFT)
            panels[piece.getY()][piece.getX()].setIcon(icons.get(pName + "-left"));
        else if (piece.getDirection() == SnakeDirection.RIGHT)
            panels[piece.getY()][piece.getX()].setIcon(icons.get(pName + "-right"));
    }


    private void move(int dx, int dy) {
        Map<String, ImageIcon> icons = Room.game.getUserInterface().getIcons();
        JLabel[][] panels = Room.game.getUserInterface().getPieces();


        // Создаем новую голову - новый "кусочек змеи".
        SnakeSection OldHead = sections.get(0);
        SnakeSection head = new SnakeSection(OldHead.getX() + dx, OldHead.getY() + dy, direction);


        checkBorders(head);
        checkBody(head);
        if (!isAlive){
            printHeadOrTail(OldHead, "dead-head");
            return;
        }


        // меняем старую голову на тело (или поворот)
        printBodyOrTurn(OldHead, head);


        // Проверяем - не съела ли змея мышь.
        Pig pig = Room.game.getPig();
        if (head.getX() == pig.getX() && head.getY() == pig.getY()) // съела
            Room.game.eatMouse();                   // Хвост не удаляем, но создаем новую мышь.
        else // просто движется
        {
            SnakeSection tail = sections.remove(sections.size() - 1);   // удалили последний элемент с хвоста
            panels[tail.getY()][tail.getX()].setIcon(icons.get("grass"));   // поставили траву
        }

        sections.add(0, head);                  // Добавили новую голову
        printHeadOrTail(head, "head");

        // меняем тело на новый хвост
        printHeadOrTail(sections.get(sections.size() - 1), "tail");

    }




    /**
     * Метод проверяет - находится ли новая голова в пределах комнаты
     */
    private void checkBorders(SnakeSection head) {
        if ((head.getX() < 0 || head.getX() >= Room.game.getWidth()) || head.getY() < 0 || head.getY() >= Room.game.getHeight()) {
            isAlive = false;
        }
    }

    /**
     * Метод проверяет - не совпадает ли голова с каким-нибудь участком тела змеи.
     */
    private void checkBody(SnakeSection head) {
        if (sections.contains(head)) {
            isAlive = false;
        }
    }
}
