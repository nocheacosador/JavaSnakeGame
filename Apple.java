import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;

public class Apple {
    public int x;
    public int y;

    private Random random = new Random();
    private Color color = Color.RED;
    private Snake snake;
    private final int APPLE_RADIUS = GamePanel.CELL_SIZE / 3;

    public Apple(Snake snake) {
        this.snake = snake;
        newPosition();
    }

    private void newPosition() {
        do {
            int cellX = random.nextInt(GamePanel.CELL_COUNT_X);
            int cellY = random.nextInt(GamePanel.CELL_COUNT_Y);
            
            this.x = GamePanel.TICK_COUNT / 2 + GamePanel.TICK_COUNT * cellX;
            this.y = GamePanel.TICK_COUNT / 2 + GamePanel.TICK_COUNT * cellY;

            System.out.println("Apple CELL X=" + cellX + " Y=" + cellY);
            System.out.println("Apple WORLD X=" + this.x + " Y=" + this.y);
            Coord screen = GamePanel.toScreenCoords(this.x, this.y);
            System.out.println("Apple WORLD X=" + screen.x + " Y=" + screen.y);
        } while (snake.contains(this.x, this.y));
    }

    public void update() {
        if (snake.contains(this.x, this.y)) {
            newPosition();
        }
    }

    public void draw(Graphics g)
    {
        Coord screen = GamePanel.toScreenCoords(this.x, this.y);
        g.setColor(color);
        g.fillOval(screen.x - APPLE_RADIUS, screen.y - APPLE_RADIUS, APPLE_RADIUS * 2, APPLE_RADIUS * 2);
        g.setColor(Color.BLACK);
        g.drawOval(screen.x - APPLE_RADIUS, screen.y - APPLE_RADIUS, APPLE_RADIUS * 2, APPLE_RADIUS * 2);
    }
}
