public class CircleBound extends Bound {
    private int squareRadius;
    private int x;
    private int y;
    
    public CircleBound() {
        this.x = 0;
        this.y = 0;
        this.squareRadius = 0;
    }

    public CircleBound(int radius) {
        this.x = 0;
        this.y = 0;
        this.squareRadius = radius * radius;
    }

    public CircleBound(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.squareRadius = radius * radius;
    }

    public CircleBound(Coord center, int radius) {
        this.x = center.x;
        this.y = center.y;
        this.squareRadius = radius * radius;
    }

    @Override
    public boolean contains(int x, int y) {
        if ( (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) <= this.squareRadius ) {
            return true;
        }
        
        return false;
    }
}
