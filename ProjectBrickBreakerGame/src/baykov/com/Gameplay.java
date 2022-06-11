package baykov.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int totalBricks = 48;
    private final Timer timer;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private Map map;

    public Gameplay() {
        map = new Map(4, 12);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delay = 8;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.BLACK);
        g.fillRect(3, 3, 697, 597);

        // map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 3, 597);
        g.fillRect(0, 0, 697, 3);
        g.fillRect(697, 0, 3, 597);

        // score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        // ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballposX, ballposY, 20, 20);

        // win game
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // lose game
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            g.drawString("GAME OVER", 250, 300);

            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Score: " + score, 300, 330);
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 48;
                map = new Map(4, 12);
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        timer.start();
        if (play) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8))) {
                ballYdir = -ballYdir;
                ballXdir = -2;
            } else if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8))) {
                ballYdir = -ballYdir;
                ballXdir += 1;
            } else if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8))) {
                ballYdir = -ballYdir;
            }

            // map collision with the ball
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rectangle =  new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRectangle =new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballRectangle.intersects(rectangle)) {
                            map.setBrickValue(0, i, j);
                            score += 5;
                            totalBricks--;

                            // ball hits left or right of brick
                            if (ballposX + 19 <= rectangle.x || ballposX + 1 >= rectangle.x + rectangle.width) {
                                ballXdir = -ballXdir;
                            } else {
                                // ball hits top or bottom of brick
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }

            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }

            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }

            repaint();
        }
    }
}
