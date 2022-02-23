import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;

public class Snake {
    public enum Direction {
        Up, Down, Left, Right
    }

    private class Vector {
        public int x;
        public int y;
        public Direction direction;

        public Vector(int x, int y, Direction d) {
            this.x = x;
            this.y = y;
            this.direction = d;
        }

        public Vector(Vector vec) {
            this.x = vec.x;
            this.y = vec.y;
            this.direction = vec.direction;
        }

        public Vector toScreenCoords() {
            Coord coord = GamePanel.toScreenCoords(x, y);
            return new Vector(coord.x, coord.y, direction);
        }
    }
    
    private int speed;                              // world ticks per time tick
    private int length;
    private Direction nextDirection;
    private ArrayList<Vector> bodyNodes;            // in world coordinates
    
    private final int SNAKE_WIDTH = GamePanel.CELL_SIZE / 2;
    private final int SNAKE_WIDTH_WORLD = GamePanel.toWorldSize(SNAKE_WIDTH);
    private final Color BODY_COLOR = Color.WHITE;
    
    
    public Snake() {
        this.length = 3;
        this.speed = 1;
        this.nextDirection = Direction.Left;

        createSnake();
    }

    public Snake(int length) {
        this.length = length;
        this.speed = 1;
        this.nextDirection = Direction.Left;

        createSnake();
    }

    private void createSnake() {
        bodyNodes = new ArrayList<Vector>();
        int x = GamePanel.CELL_COUNT_X / 2 * GamePanel.TICK_COUNT + GamePanel.TICK_COUNT / 2;
        int y = GamePanel.CELL_COUNT_Y / 2 * GamePanel.TICK_COUNT + GamePanel.TICK_COUNT / 2;
        
        bodyNodes.add(new Vector(x, y, Direction.Left));
        bodyNodes.add(new Vector(x + GamePanel.TICK_COUNT * (length - 1), y, Direction.Left));
    }

    public void update(int ticks) {
        Vector head = bodyNodes.get(0);
        Vector tail = bodyNodes.get(bodyNodes.size() - 1);

        for (int i = 0; i < ticks; i++) {
            if (head.direction != nextDirection) {
                if (head.x % GamePanel.TICK_COUNT == GamePanel.TICK_COUNT / 2 && head.y % GamePanel.TICK_COUNT == GamePanel.TICK_COUNT / 2) {
                    head.direction = nextDirection;

                    bodyNodes.add(0, new Vector(head));
                    head = bodyNodes.get(0);
                }
            }

            switch (head.direction) {
            case Up:
                head.y -= speed;
                break;
            case Down:
                head.y += speed;
                break;
            case Left:
                head.x -= speed;
                break;
            case Right:
                head.x += speed;
                break;
            }

            if (bodyNodes.size() < 2) {
                throw new RuntimeException("Snake size is less than 2.");
            }
            
            Vector pretail = bodyNodes.get(bodyNodes.size() - 2);

            if (pretail.x == tail.x && pretail.y == tail.y) {
                bodyNodes.remove(bodyNodes.size() - 1);
                tail = bodyNodes.get(bodyNodes.size() - 1);
            }

            switch (tail.direction) {
            case Up:
                tail.y -= speed;
                break;
            case Down:
                tail.y += speed;
                break;
            case Left:
                tail.x -= speed;
                break;
            case Right:
                tail.x += speed;
                break;
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < bodyNodes.size(); i++) {
            Vector node = bodyNodes.get(i).toScreenCoords();
            g.setColor(BODY_COLOR);
            g.fillOval(node.x - SNAKE_WIDTH / 2, node.y - SNAKE_WIDTH / 2, SNAKE_WIDTH, SNAKE_WIDTH);
            
            if (i != 0) {
                Vector previousNode = bodyNodes.get(i - 1).toScreenCoords();
                g.setColor(BODY_COLOR);

                int height = SNAKE_WIDTH;
                int width  = SNAKE_WIDTH;
                int minX = Math.min(node.x, previousNode.x);
                int maxX = Math.max(node.x, previousNode.x);
                int minY = Math.min(node.y, previousNode.y);
                int maxY = Math.max(node.y, previousNode.y);
                
                if (node.x == previousNode.x) {
                    height = maxY - minY;
                    minX -= SNAKE_WIDTH / 2;
                }
                else {
                    width = maxX - minX;
                    minY -= SNAKE_WIDTH / 2;
                }

                g.fillRect(minX, minY, width, height);
            }
        }
    }

    // Debug Draw - shows nodes
    public void debugDraw(Graphics g) {
        for (int i = 0; i < bodyNodes.size(); i++) {
            Vector node = bodyNodes.get(i).toScreenCoords();

            g.setColor(Color.ORANGE);
            g.fillOval(node.x - SNAKE_WIDTH / 4, node.y - SNAKE_WIDTH / 4, SNAKE_WIDTH / 2, SNAKE_WIDTH / 2);
            g.setColor(Color.BLACK);
            g.drawOval(node.x - SNAKE_WIDTH / 4, node.y - SNAKE_WIDTH / 4, SNAKE_WIDTH / 2, SNAKE_WIDTH / 2);
            
            if (i != 0) {
                Vector previousNode = bodyNodes.get(i - 1).toScreenCoords();
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(previousNode.x, previousNode.y, node.x, node.y);
            }
        }
    }

    private int squareDisatanceBetweenPoints(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    private boolean rectContains(int x, int y, int rectX, int rectY, int rectWidth, int rectHeight) {
        if (rectX <= x && rectX + rectWidth >= x && rectY <= y && rectY + rectHeight >= y) {
            return true;
        }
        return false;
    }

    public boolean contains(int x, int y) {
        for (int i = 0; i < bodyNodes.size(); i++) {
            Vector node = bodyNodes.get(i);

            if ((SNAKE_WIDTH_WORLD / 2) * (SNAKE_WIDTH_WORLD / 2) >= squareDisatanceBetweenPoints(node.x, node.y, x, y)) {
                return true;
            }

            if (i != 0) {
                Vector previousNode = bodyNodes.get(i - 1);

                int height = SNAKE_WIDTH_WORLD;
                int width  = SNAKE_WIDTH_WORLD;
                int minX = Math.min(node.x, previousNode.x);
                int maxX = Math.max(node.x, previousNode.x);
                int minY = Math.min(node.y, previousNode.y);
                int maxY = Math.max(node.y, previousNode.y);
                
                if (node.x == previousNode.x) {
                    height = maxY - minY;
                    minX -= SNAKE_WIDTH_WORLD / 2;
                }
                else {
                    width = maxX - minX;
                    minY -= SNAKE_WIDTH_WORLD / 2;
                }

                if (rectContains(x, y, minX, minY, width, height)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setDirection(Direction direction) {
        Direction currentDirection = bodyNodes.get(0).direction;

        if (   (direction == Direction.Up    && currentDirection != Direction.Down)
            || (direction == Direction.Down  && currentDirection != Direction.Up) 
            || (direction == Direction.Left  && currentDirection != Direction.Right)
            || (direction == Direction.Right && currentDirection != Direction.Left)) {
            nextDirection = direction;
        }
    }

    public int getLength() {
        return length;
    }
}
