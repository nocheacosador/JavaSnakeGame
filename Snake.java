import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;

public class Snake extends Bound {
    public enum Direction {
        Up, Down, Left, Right
    }

    private class Vector {
        public Coord position;
        public Direction direction;

        public Vector(int x, int y, Direction d) {
            this.position = new Coord(x, y);
            this.direction = d;
        }

        public Vector(Coord coord, Direction d) {
            this.position = new Coord(coord);
            this.direction = d;
        }

        public Vector(Vector vec) {
            this.position = new Coord(vec.position);
            this.direction = vec.direction;
        }

        public Vector toScreenCoords() {
            Coord coord = GamePanel.toScreenCoords(position);
            return new Vector(coord, direction);
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
                if (   head.position.x % GamePanel.TICK_COUNT == GamePanel.TICK_COUNT / 2 
                    && head.position.y % GamePanel.TICK_COUNT == GamePanel.TICK_COUNT / 2)
                {    
                    head.direction = nextDirection;

                    bodyNodes.add(0, new Vector(head));
                    head = bodyNodes.get(0);
                }
            }

            switch (head.direction) {
            case Up:
                head.position.y -= speed;
                break;
            case Down:
                head.position.y += speed;
                break;
            case Left:
                head.position.x -= speed;
                break;
            case Right:
                head.position.x += speed;
                break;
            }

            if (bodyNodes.size() < 2) {
                throw new RuntimeException("Snake size is less than 2.");
            }
            
            Vector pretail = bodyNodes.get(bodyNodes.size() - 2);

            if (pretail.position.x == tail.position.x && pretail.position.y == tail.position.y) {
                bodyNodes.remove(bodyNodes.size() - 1);
                tail = bodyNodes.get(bodyNodes.size() - 1);
            }

            switch (tail.direction) {
            case Up:
                tail.position.y -= speed;
                break;
            case Down:
                tail.position.y += speed;
                break;
            case Left:
                tail.position.x -= speed;
                break;
            case Right:
                tail.position.x += speed;
                break;
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < bodyNodes.size(); i++) {
            Vector node = bodyNodes.get(i).toScreenCoords();
            g.setColor(BODY_COLOR);
            g.fillOval(node.position.x - SNAKE_WIDTH / 2, node.position.y - SNAKE_WIDTH / 2, SNAKE_WIDTH, SNAKE_WIDTH);
            
            if (i != 0) {
                Vector previousNode = bodyNodes.get(i - 1).toScreenCoords();
                g.setColor(BODY_COLOR);

                int height = SNAKE_WIDTH;
                int width  = SNAKE_WIDTH;
                int minX = Math.min(node.position.x, previousNode.position.x);
                int maxX = Math.max(node.position.x, previousNode.position.x);
                int minY = Math.min(node.position.y, previousNode.position.y);
                int maxY = Math.max(node.position.y, previousNode.position.y);
                
                if (node.position.x == previousNode.position.x) {
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
            g.fillOval(node.position.x - SNAKE_WIDTH / 4, node.position.y - SNAKE_WIDTH / 4, SNAKE_WIDTH / 2, SNAKE_WIDTH / 2);
            g.setColor(Color.BLACK);
            g.drawOval(node.position.x - SNAKE_WIDTH / 4, node.position.y - SNAKE_WIDTH / 4, SNAKE_WIDTH / 2, SNAKE_WIDTH / 2);
            
            if (i != 0) {
                Vector previousNode = bodyNodes.get(i - 1).toScreenCoords();
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(previousNode.position.x, previousNode.position.y, node.position.x, node.position.y);
            }
        }
    }

    @Override
    public boolean contains(int x, int y) {
        for (int i = 0; i < bodyNodes.size(); i++) {
            Vector node = bodyNodes.get(i);

            CircleBound circleBound = new CircleBound(node.position, SNAKE_WIDTH_WORLD / 2);
            if (circleBound.contains(x, y)) {
                return true;
            }

            if (i != 0) {
                Vector previousNode = bodyNodes.get(i - 1);

                int height = SNAKE_WIDTH_WORLD;
                int width  = SNAKE_WIDTH_WORLD;
                int minX = Math.min(node.position.x, previousNode.position.x);
                int maxX = Math.max(node.position.x, previousNode.position.x);
                int minY = Math.min(node.position.y, previousNode.position.y);
                int maxY = Math.max(node.position.y, previousNode.position.y);
                
                if (node.position.x == previousNode.position.x) {
                    height = maxY - minY;
                    minX -= SNAKE_WIDTH_WORLD / 2;
                }
                else {
                    width = maxX - minX;
                    minY -= SNAKE_WIDTH_WORLD / 2;
                }

                RectBound rectBound = new RectBound(minX, minY, width, height);
                if (rectBound.contains(x, y)) {
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
