package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class UserInterface {
    private JFrame frame;
    private JLabel[][] pieces;
    private Map<String, ImageIcon> icons = new HashMap<>();
    private int width, length;
    static volatile boolean restart = false;


    public UserInterface(int width, int length) {
        this.width = width;
        this.length = length;
        setIcons();
        pieces = new JLabel[width][length];
        frame = new JFrame();
        setFrame(frame,  800,  700);
        gridLayout();
    }


    private void setFrame(JFrame frame, final int width, final int length) {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle("Snake");
            frame.setBackground(Color.GREEN);
            frame.setSize(width, length);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }


    public void menu() {
        JDialog dialog = new JDialog();
        JButton restart = new JButton("RESTART");
        JButton stop = new JButton("STOP");

        dialog.setTitle("GAME OVER, SCORE: " + Room.GAME.getSnake().getSections().size());
        dialog.setLayout(new GridLayout(1, 2));
        dialog.setVisible(true);
        dialog.setSize(400, 150);
        dialog.add(restart);
        dialog.add(stop);


        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshFrame();
                SwingUtilities.invokeLater(() -> {
                    dialog.dispose();
                    dialog.setVisible(false);
                });
                SwingUtilities.invokeLater(() -> {
                    UserInterface.restart = true;
                });
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void refreshFrame() {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < length; j++) {
                    pieces[i][j].setIcon(icons.get("grass"));
                }
            }
        });
    }

    private void gridLayout() {
        SwingUtilities.invokeLater(() -> {
            frame.setLayout(new GridLayout(width, length));

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < length; j++) {
                    JLabel label = new JLabel();
                    label.setIcon(icons.get("grass"));
                    pieces[i][j] = label;
                    frame.add(label);
                }
            }
        });
    }



    public static void setRestart(boolean restart) {
        UserInterface.restart = restart;
    }

    private void setIcons() {
        final String dir = "src/GAME/images/";

        icons.put("grass", new ImageIcon(dir + "grass2.png"));
        icons.put("pig", new ImageIcon(dir + "pig2.png"));


        icons.put("body-v", new ImageIcon(dir + "bodyVertical.png"));
        icons.put("body-h", new ImageIcon(dir + "bodyHorisont.png"));

        icons.put("head-down", new ImageIcon(dir + "head-down.png"));
        icons.put("head-up", new ImageIcon(dir + "head-up.png"));
        icons.put("head-right", new ImageIcon(dir + "head-right.png"));
        icons.put("head-left", new ImageIcon(dir + "head-left.png"));

        icons.put("left-down", new ImageIcon(dir + "left-down.png"));
        icons.put("left-up", new ImageIcon(dir + "left-up.png"));
        icons.put("right-down", new ImageIcon(dir + "right-down.png"));
        icons.put("right-up", new ImageIcon(dir + "right-up.png"));

        icons.put("tail-up", new ImageIcon(dir + "tail-up.png"));
        icons.put("tail-down", new ImageIcon(dir + "tail-down.png"));
        icons.put("tail-left", new ImageIcon(dir + "tail-left.png"));
        icons.put("tail-right", new ImageIcon(dir + "tail-right.png"));

        icons.put("dead-head-down", new ImageIcon(dir + "dead-head-down.png"));
        icons.put("dead-head-up", new ImageIcon(dir + "dead-head-up.png"));
        icons.put("dead-head-right", new ImageIcon(dir + "dead-head-right.png"));
        icons.put("dead-head-left", new ImageIcon(dir + "dead-head-left.png"));


    }


    public JLabel[][] getPieces() {
        return pieces;
    }

    public Map<String, ImageIcon> getIcons() {
        return icons;
    }

//    public static void main(String[] args) {
//        UserInterface userInterface = new UserInterface(20, 20);
//    }
}
