package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick {
    private Rectangle rect;
    private boolean broken = false;

    public Brick(double x, double y, double width, double height) {
        rect = new Rectangle(x, y, width, height);
        rect.setFill(Color.web("#ff9933"));
        rect.setStroke(Color.BLACK);
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean b) {
        broken = b;
        rect.setVisible(!b);
    }
}
