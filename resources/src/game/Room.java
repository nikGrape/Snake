package game;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Основной класс программы.
 */
public class Room {
    static Room GAME;

    private int width;
    private int height;
    private Snake snake;
    private Pig pig;
    private UserInterface userInterface;
    private boolean booster = false;


    public Room(int width, int height, Snake snake) {
        this.width = width;
        this.height = height;
        this.snake = snake;
        this.userInterface = new UserInterface(width, height);
        try {
            Thread.sleep(3000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GAME = this;
    }

    public Snake getSnake() {
        return snake;
    }

    public Pig getPig() {
        return pig;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public void setPig(Pig pig) {
        this.pig = pig;
    }

    public void boost() {
        booster = true;
    }

    public void unboost() {
        booster = false;
    }

    /**
     * Основной цикл программы.
     * Тут происходят все важные действия
     */
    public void run() {
        //Создаем объект "наблюдатель за клавиатурой" и стартуем его.
        KeyboardObserver keyboardObserver = new KeyboardObserver();
        keyboardObserver.start();
        System.out.println("start");
        //пока змея жива
        while (snake.isAlive()) {

            if (keyboardObserver.hasKeyEvents()) { //"наблюдатель" содержит события о нажатии клавиш?
                KeyEvent event = keyboardObserver.getEventFromTop();
                //Если равно символу 'q' - выйти из игры.
                if (event.getKeyChar() == 'q') return;

                //Если "стрелка влево" - сдвинуть фигурку влево
                if (event.getKeyCode() == KeyEvent.VK_LEFT && snake.getDirection() != SnakeDirection.RIGHT)
                    snake.setDirection(SnakeDirection.LEFT);
                    //Если "стрелка вправо" - сдвинуть фигурку вправо
                else if (event.getKeyCode() == KeyEvent.VK_RIGHT && snake.getDirection() != SnakeDirection.LEFT)
                    snake.setDirection(SnakeDirection.RIGHT);
                    //Если "стрелка вверх" - сдвинуть фигурку вверх
                else if (event.getKeyCode() == KeyEvent.VK_UP && snake.getDirection() != SnakeDirection.DOWN)
                    snake.setDirection(SnakeDirection.UP);
                    //Если "стрелка вниз" - сдвинуть фигурку вниз
                else if (event.getKeyCode() == KeyEvent.VK_DOWN && snake.getDirection() != SnakeDirection.UP)
                    snake.setDirection(SnakeDirection.DOWN);
            }

            SwingUtilities.invokeLater(snake::move);   //двигаем змею
            SwingUtilities.invokeLater(this::printPig);        //отображаем текущее состояние игры
            sleep();        //пауза между ходами
        }

        System.out.println("end");
        //Выводим сообщение "Game Over"
        userInterface.menu();
        while (!userInterface.restart) {}


        UserInterface.restart = false;
        booster = false;
        snake = new Snake(0, 3, SnakeDirection.DOWN);
        GAME.createMouse();
        GAME.run();


    }

    /**
     * Выводим на экран текущее состояние игры
     */
    public void printPig() {
        JLabel[][] panels = userInterface.getPieces();
        panels[pig.getY()][pig.getX()].setIcon(userInterface.getIcons().get("pig"));
    }

    /**
     * Метод вызывается, когда мышь съели
     */
    public void eatMouse() {
        createMouse();
    }

    /**
     * Создает новую мышь
     */
    public void createMouse() {
        Pig pig;
        do {
            pig = new Pig();
        } while (checkSnake(pig));

        this.pig = pig;
    }

    // check for the pig will not appear on the snakes body
    private boolean checkSnake(Pig pig) {
        return snake.getSections().stream().anyMatch(el->el.getX()== pig.getX() && el.getY()== pig.getY());
    }




    public static void main(String[] args) {
        GAME = new Room(15, 15, new Snake(0, 3, SnakeDirection.DOWN));
        GAME.createMouse();
        GAME.run();
    }


    /**
     * Программа делает паузу, длинна которой зависит от длинны змеи.
     */
    public void sleep() {
        int initialDelay = 520;
        int delayStep = 20;
        int level = snake.getSections().size();

        int delay = level > 15 ? 200 : (initialDelay - delayStep * level);
        if (booster) {
            delay = 100;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignore) {
        }
    }
}
