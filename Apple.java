import java.awt.Graphics;
import java.awt.Color;
//import java.util.Random;

public class Apple {
    public int x;
    public int y;

    //private Random random = new Random();
    private Color color = Color.YELLOW;

    private final int APPLE_RADIUS = GamePanel.CELL_SIZE / 3;

    public Apple() {
        this.x = GamePanel.CELL_COUNT_X / 2 * GamePanel.TICK_COUNT + GamePanel.TICK_COUNT / 2;
        this.y = GamePanel.CELL_COUNT_Y / 2 * GamePanel.TICK_COUNT + GamePanel.TICK_COUNT / 2 + GamePanel.TICK_COUNT * 5;
    }

    public void active(boolean b) {
        if (b) {
            color = Color.GREEN;
        }
        else {
            color = Color.YELLOW;
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
