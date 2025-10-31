package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        MenuScene menu = new MenuScene(stage);
        stage.setTitle("Arkanoid FX");
        stage.setScene(menu.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

