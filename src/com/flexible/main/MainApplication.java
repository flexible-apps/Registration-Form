package com.flexible.main;

import com.flexible.annotations.Author;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Objects;

@Author(name = "Abdel Halim")
public class MainApplication extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/flexible/main/main.fxml")));
        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
        MainApplication.stage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static Stage getStage() {
        return stage;
    }
}
