import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;

public class Apple {
    public int x; // world coordinate
    public int y; // world coordinate

    private Random random = new Random();
    private Snake snake;
    
    private final Color color = Color.RED;
    private final int APPLE_RADIUS = GamePanel.CELL_SIZE / 3;
    private final int APPLE_RADIUS_WORLD = GamePanel.toWorldSize(APPLE_RADIUS);

    public Apple(Snake snake) {
        this.snake = snake;
        this.newPosition();
    }

    private void newPosition() {
        do {
            int cellX = random.nextInt(GamePanel.CELL_COUNT_X);
            int cellY = random.nextInt(GamePanel.CELL_COUNT_Y);
            
            this.x = GamePanel.TICK_COUNT / 2 + GamePanel.TICK_COUNT * cellX;
            this.y = GamePanel.TICK_COUNT / 2 + GamePanel.TICK_COUNT * cellY;
        } while (snake.contains(this.x, this.y));
    }

    public void update() {
        CircleBound bound = new CircleBound(this.x, this.y, APPLE_RADIUS_WORLD);
        if (bound.contains(snake.getNoseCoord())) {
            this.newPosition();
            //snake.grow();
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
