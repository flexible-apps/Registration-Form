package com.flexible.launcher;

import com.flexible.controls.shapes.MFARectangle;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane view = new Pane();

        MFARectangle rectangle = new MFARectangle(100, 50, new Insets(10, 30, 30, 10));
        rectangle.setFill(Color.RED);
        rectangle.setTranslateX(100);


        view.getChildren().setAll(rectangle);
        Scene scene = new Scene(view);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
