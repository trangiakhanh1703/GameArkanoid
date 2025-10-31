package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Background {
    private final Rectangle bg;
    private double hue = 0;

    public Background(Group root, double width, double height) {
        bg = new Rectangle(width, height, Color.BLACK);
        root.getChildren().add(0, bg); // thêm làm lớp nền
    }

    public void update() {
        hue += 0.5;
        if (hue > 360) hue = 0;
        bg.setFill(Color.hsb(hue, 0.5, 0.3));
    }
}
