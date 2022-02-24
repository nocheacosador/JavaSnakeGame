public class Bound {
    public boolean contains(int x, int y) {
        return false;
    }
    public boolean contains(Coord coord) {
        return contains(coord.x, coord.y);
    }
}