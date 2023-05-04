package src;

public class Coordinates {
    private int x;
    private int y;

    public static boolean checkX(int x){return x <= 970;}
    public static boolean checkY(int y){return y > -988;}
    public static boolean checkCoordinates(int x, int y) {
        return checkX(x) && checkY(y);
    }

    public Coordinates(){
        x = 0;
        y = 0;
    }

    // Конструктор с параметрами
    public Coordinates(int x, int y) {
        if (!checkCoordinates(x, y)) {
            throw new IllegalArgumentException("Invalid coordinates: (" + x + "," + y + ")");
        }
        this.x = x;
        this.y = y;
    }

    // Геттеры и сеттеры
    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (!checkCoordinates(x, this.y)) {
            throw new IllegalArgumentException("Invalid coordinates: (" + x + "," + this.y + ")");
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (!checkCoordinates(this.x, y)) {
            throw new IllegalArgumentException("Invalid coordinates: (" + this.x + "," + y + ")");
        }
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" + "x=" + x + ", y=" + y + '}';
    }
}