package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall {
    public Wall(Group root, double width, double height) {
        Rectangle left = new Rectangle(0, 0, 10, height);
        Rectangle right = new Rectangle(width - 10, 0, 10, height);
        Rectangle top = new Rectangle(0, 0, width, 10);

        left.setFill(Color.GRAY);
        right.setFill(Color.GRAY);
        top.setFill(Color.GRAY);

        root.getChildren().addAll(left, right, top);
    }
}

