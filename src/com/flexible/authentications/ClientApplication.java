package com.flexible.authentications;

import com.flexible.main.MainApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ClientApplication extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/flexible/authentications/page.fxml")));
        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setTitle("Flexible Apps");
        primaryStage.getIcons().setAll(getIcon());
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;
    }

    @NotNull
    private static Image getIcon() {
        return new Image(Objects.requireNonNull(ClientApplication.class.getResourceAsStream("/com/flexible/resource/pic/flexible-apps.png")));
    }

    public static void main(String[] args) {
        launch(args);
    }

}