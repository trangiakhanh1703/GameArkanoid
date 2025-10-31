package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Background background;

    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks = new ArrayList<>();
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600, Color.BLACK);
        background = new Background(root, 800, 800);
        new Wall(root, 600, 600);


        paddle = new Paddle(250, 550, 100, 15);
        ball = new Ball(300, 400, 10);

        root.getChildren().addAll(paddle.getRect(), ball.getCircle());

        // tạo gạch
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                Brick brick = new Brick(50 + j * 60, 50 + i * 25, 50, 20);
                bricks.add(brick);
                root.getChildren().add(brick.getRect());
            }
        }

        // điều khiển
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = true;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = false;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
        });

        // game loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(scene);
            }
        }.start();

        stage.setScene(scene);
        stage.setTitle("Arkanoid FX");
        stage.show();
    }

    private void update(Scene scene) {
        background.update();

        if (leftPressed) paddle.moveLeft();
        if (rightPressed) paddle.moveRight(scene.getWidth());

        ball.move();

        // va chạm tường
        if (ball.getX() <= 0 || ball.getX() >= scene.getWidth())
            ball.bounceX();
        if (ball.getY() <= 0)
            ball.bounceY();

        // rơi xuống
        if (ball.getY() >= scene.getHeight()) {
            ball = new Ball(300, 400, 10);
        }

        // va chạm paddle
        if (ball.getCircle().getBoundsInParent().intersects(paddle.getRect().getBoundsInParent())) {
            ball.bounceY();
        }

        // va chạm gạch
        for (Brick brick : bricks) {
            if (!brick.isBroken() && ball.getCircle().getBoundsInParent().intersects(brick.getRect().getBoundsInParent())) {
                brick.setBroken(true);
                ball.reverseY();
                break;
            }
        }
    }
}
