package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
    private Circle circle;
    private double dx = 3;
    private double dy = -3;

    public Ball(double x, double y, double radius) {
        circle = new Circle(x, y, radius, Color.RED);
    }

    public Circle getCircle() {
        return circle;
    }

    public void move() {
        circle.setCenterX(circle.getCenterX() + dx);
        circle.setCenterY(circle.getCenterY() + dy);
    }

    public void bounceX() {
        dx = -dx;
    }

    public void bounceY() {
        dy = -dy;
    }

    public double getX() { return circle.getCenterX(); }
    public double getY() { return circle.getCenterY(); }
    public double getRadius() { return circle.getRadius(); }

    public double getDx() { return dx; }
    public double getDy() { return dy; }

    public void reverseY() { dy = -dy; }
}
