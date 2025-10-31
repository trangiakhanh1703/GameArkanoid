package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
    private Rectangle rect;

    public Paddle(double x, double y, double width, double height) {
        rect = new Rectangle(x, y, width, height);
        rect.setFill(Color.BLUE);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void moveLeft() {
        if (rect.getX() > 0)
            rect.setX(rect.getX() - 15);
    }

    public void moveRight(double sceneWidth) {
        if (rect.getX() + rect.getWidth() < sceneWidth)
            rect.setX(rect.getX() + 15);
    }
}
