package baykov.com;

import javax.swing.*;

public class Frame {

    JFrame frame = new JFrame("Brick Breaker");
    Gameplay game = new Gameplay();

    public Frame() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(10, 10, 700, 600);
        frame.setResizable(false);


        frame.add(game);
        frame.setVisible(true);

    }
}
