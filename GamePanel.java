import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.System;

public class GamePanel extends JPanel implements ActionListener {
    public static final int CELL_COUNT_Y = 14;
    public static final int CELL_COUNT_X = 14;
    public static final int CELL_SIZE = 50;
    public static final int TICK_COUNT = 200;
    public static final int TIME_TICK_MILLIS = 10;
    public static final int WIDTH = CELL_COUNT_X * CELL_SIZE;
    public static final int HEIGHT = CELL_COUNT_Y * CELL_SIZE;
    private static final int DELAY = 40;
    
    private static final Color BACKGROUND_COLOR_LIGHT = new Color(120, 204, 110);
    private static final Color BACKGROUND_COLOR_DARK = new Color(91, 153, 83);

    private enum GameState {
        Start, Running, Paused, GameOver
    };

    private Timer timer = new Timer(DELAY, this);
    private Snake snake = new Snake();
    private Apple apple = new Apple(snake);
    private long lastTimeStamp = 0;
    private long unaccountedMillis = 0;
    private GameState gameState = GameState.Start;

    static private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    
    static public int toScreenSize(int x) {
        int x_out = x * CELL_SIZE / TICK_COUNT;
        return x_out;
    }

    static public int toWorldSize(int x) {
        int x_out = x * TICK_COUNT / CELL_SIZE;
        return x_out;
    }

    static public Coord toScreenCoords(int x, int y) {
        int x_out = map(x, 0, CELL_COUNT_X * TICK_COUNT, 0, WIDTH);
        int y_out = map(y, 0, CELL_COUNT_Y * TICK_COUNT, 0, HEIGHT);

        return new Coord(x_out, y_out);
    }

    static public Coord toScreenCoords(Coord coord) {
        int x_out = map(coord.x, 0, CELL_COUNT_X * TICK_COUNT, 0, WIDTH);
        int y_out = map(coord.y, 0, CELL_COUNT_Y * TICK_COUNT, 0, HEIGHT);

        return new Coord(x_out, y_out);
    }

    static public Coord toWorldCoords(int x, int y) {
        int x_out = map(x, 0, WIDTH,  0, CELL_COUNT_X * TICK_COUNT);
        int y_out = map(y, 0, HEIGHT, 0, CELL_COUNT_Y * TICK_COUNT);

        return new Coord(x_out, y_out);
    }

    static public Coord toWorldCoords(Coord coord) {
        int x_out = map(coord.x, 0, WIDTH,  0, CELL_COUNT_X * TICK_COUNT);
        int y_out = map(coord.y, 0, HEIGHT, 0, CELL_COUNT_Y * TICK_COUNT);

        return new Coord(x_out, y_out);
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapder());

        snake.onGrowth(new Callback() {
            @Override
            public void call() {
                System.out.println("Snake has grown.");
            }
        });

        snake.onDeath(new Callback() {
            @Override
            public void call() {
                gameState = GameState.GameOver;
                System.out.println("Snake is dead.");
            }
        });

        timer.start();
        lastTimeStamp = System.currentTimeMillis();
    }

    private void drawBackground(Graphics g) {        
        for (int y = 0; y < CELL_COUNT_Y; y++) {
            for (int x = 0; x < CELL_COUNT_X; x++) {
                g.setColor( (x % 2 == 1 ^ y % 2 == 1) ? BACKGROUND_COLOR_LIGHT : BACKGROUND_COLOR_DARK);
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        long timeStamp = System.currentTimeMillis();
        long deltaTime = timeStamp - lastTimeStamp;
        lastTimeStamp = timeStamp;

        long ticks = (deltaTime + unaccountedMillis) / TIME_TICK_MILLIS;
        unaccountedMillis = (deltaTime + unaccountedMillis) % TIME_TICK_MILLIS;
        
        if (gameState == GameState.Running) {
            snake.update((int)ticks);
            apple.update();
        }

        this.drawBackground(g);
        apple.draw(g);
        snake.draw(g);
        snake.debugDraw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }

    public class MyKeyAdapder extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameState == GameState.Start || gameState == GameState.Paused) {
                gameState = GameState.Running;
                return;
            }  
            switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                snake.setDirection(Snake.Direction.Up);
                break;
            case KeyEvent.VK_DOWN:
                snake.setDirection(Snake.Direction.Down);
                break;
            case KeyEvent.VK_LEFT:
                snake.setDirection(Snake.Direction.Left);
                break;
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Snake.Direction.Right);
                break;
            case KeyEvent.VK_P:
                gameState = GameState.Paused;
                break;
            }
        }
    }
}
