package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
// tạo menu

public class MenuScene {
    private final Scene scene;

    public MenuScene(Stage stage) {
        Label title = new Label("ARKANOID FX");
        title.setTextFill(Color.LIGHTBLUE);
        title.setFont(Font.font("Verdana", 50));

        Button startButton = new Button("Start Game");
        Button helpButton = new Button("Instructions");
        Button exitButton = new Button("Exit");

        startButton.setPrefWidth(200);
        helpButton.setPrefWidth(200);
        exitButton.setPrefWidth(200);
        startButton.setFont(Font.font(18));
        helpButton.setFont(Font.font(18));
        exitButton.setFont(Font.font(18));

        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");
        layout.getChildren().addAll(title, startButton, helpButton, exitButton);

        scene = new Scene(layout, 600, 600);

        // Nút Start Game
        startButton.setOnAction(e -> {
            Game game = new Game();
            game.start(stage);
        });

        // Nút Instructions
        helpButton.setOnAction(e -> showInstructions(stage));

        // Nút Exit
        exitButton.setOnAction(e -> stage.close());
    }

    private void showInstructions(Stage stage) {
        Label info = new Label(
                "🕹 Cách chơi Arkanoid:\n\n" +
                        "• Dùng phím ← và → để di chuyển thanh đỡ.\n" +
                        "• Phá hết tất cả các viên gạch để thắng.\n" +
                        "• Nếu bóng rơi xuống dưới, bạn sẽ mất lượt!\n\n"
        );
        info.setTextFill(Color.WHITE);
        info.setFont(Font.font(20));
        info.setWrapText(true);
        info.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back to Menu");
        backButton.setFont(Font.font(16));
        backButton.setOnAction(e -> stage.setScene(scene));

        VBox helpLayout = new VBox(30, info, backButton);
        helpLayout.setAlignment(Pos.CENTER);
        helpLayout.setStyle("-fx-background-color: black;");

        Scene helpScene = new Scene(helpLayout, 600, 600);
        stage.setScene(helpScene);
    }

    public Scene getScene() {
        return scene;
    }
}

