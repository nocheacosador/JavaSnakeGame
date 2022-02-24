public class RectBound extends Bound {
    private int top;
    private int bottom;
    private int left;
    private int right;

    public RectBound() {
        this.top = 0;
        this.bottom = 0;
        this.left = 0;
        this.right = 0;
    }

    public RectBound(int x, int y, int width, int height) {
        this.top = y;
        this.bottom = y + height;
        this.left = x;
        this.right = x + width;
    }

    public RectBound(Coord topLeft, int width, int height) {
        this.top = topLeft.y;
        this.bottom = topLeft.y + height;
        this.left = topLeft.x;
        this.right = topLeft.x + width;
    }

    public RectBound(Coord topLeft, Coord bottomRight) {
        this.top = topLeft.y;
        this.bottom = bottomRight.y;
        this.left = topLeft.x;
        this.right = bottomRight.x;
    }

    @Override
    public boolean contains(int x, int y) {
        if (left <= x && right >= x && top <= y && bottom >= y) {
            return true;
        }
        
        return false;
    }
}