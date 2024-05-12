package com.flexible.authentications;

import com.flexible.assets.ScreenSnapshot;
import com.flexible.authentications.login.LoginController;
import com.flexible.authentications.register.RegisterController;
import com.flexible.geometry.XYPoint;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.flexible.controls.shapes.MFARectangle;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PageController implements Initializable {
    @FXML
    private MFXButton btnSlideSingIn;

    @FXML
    private MFXButton btnSlideSingUp;

    @FXML
    private VBox partLogin;

    @FXML
    private MFARectangle rectangle;

    @FXML
    private VBox partRegister;

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane slide;

    @FXML
    private VBox leftBox;

    @FXML
    private VBox rightBox;

    private Rectangle mask;

    private boolean isDefault = true;

    private final XYPoint point =
            XYPoint.getInstance();


    public void initialize(URL location, ResourceBundle resources) {
        /**
         * FIRST STEP WE LOADING ALL SING UP AND SING IN FORMS
         **/
        Platform.runLater(() -> {
            loadingForms();
            maskOfParent();
            ScreenSnapshot.snap(root);
        });


        /**
         * MAKE CLIP FOR SLIDE
         **/
        mask = new Rectangle(400, 0, 400, 500);
        slide.setClip(mask);

        /**
         * UPDATE CLIP AFTER ANY CHANGE IN TRANSLATE OF RECTANGLE
         **/
        rectangle.translateXProperty().addListener((observable, oldValue, newValue) -> {
            mask.setX(newValue.doubleValue());
            slide.setClip(mask);
        });

        /**
         * SING UP OF RESISTER ACTIONS
         * */
        btnSlideSingUp.setOnAction(event -> {
            funAnimation();
            isDefault = !isDefault;
        });

        btnSlideSingIn.setOnAction(event -> {
            funAnimation();
            isDefault = !isDefault;
        });

        /**
         * ACTION OF MOVE STAGE
         **/
        root.setOnMousePressed(event -> {
            point.setX(event.getSceneX());
            point.setY(event.getSceneY());
            point.setTranslateX(root.getTranslateX());
            point.setTranslateY(root.getTranslateY());
        });

        root.setOnMouseDragged(event -> {
            ClientApplication.stage.setX(event.getScreenX() - point.getX() + point.getTranslateX());
            ClientApplication.stage.setY(event.getScreenY() - point.getY() + point.getTranslateY());
        });
    }

    private void maskOfParent() {
        Rectangle m = new Rectangle(0, 0, root.getPrefWidth(), root.getPrefHeight());
        m.setArcWidth(50);
        m.setArcHeight(50);
        root.setClip(m);
    }

    private void funAnimation() {
        List<KeyValue> primaryOpacityKeyValues = new ArrayList<>();
        List<KeyValue> finallyOpacityKeyValues = new ArrayList<>();
        createOpacityAnim(leftBox, primaryOpacityKeyValues, finallyOpacityKeyValues, isDefault ? 0 :1, isDefault ? 1 :0);
        createOpacityAnim(rightBox, primaryOpacityKeyValues, finallyOpacityKeyValues, isDefault ? 1 :0, isDefault ? 0 :1);

        KeyFrame primaryKeyFrames = new KeyFrame(Duration.ZERO, new KeyValue(mask.translateXProperty(), isDefault ? 400.0 : 0),
                new KeyValue(rectangle.translateXProperty(), isDefault ? 400.0 : 0),
                new KeyValue(rectangle.topLeftRadiusProperty(), isDefault ? 150 : 30),
                new KeyValue(rectangle.topRightRadiusProperty(), isDefault ? 30 : 150),
                new KeyValue(rectangle.bottomLeftRadiusProperty(), isDefault ? 150 : 30),
                new KeyValue(rectangle.bottomRightRadiusProperty(), isDefault ? 30 : 150)

        );

        KeyFrame finallyKeyFrames = new KeyFrame(Duration.millis(500),
                new KeyValue(rectangle.translateXProperty(), isDefault ? 0 : 400),
                new KeyValue(rectangle.topLeftRadiusProperty(), isDefault ? 30 : 150),
                new KeyValue(rectangle.topRightRadiusProperty(), isDefault ? 150 : 30),
                new KeyValue(rectangle.bottomLeftRadiusProperty(), isDefault ? 30 : 150),
                new KeyValue(rectangle.bottomRightRadiusProperty(), isDefault ? 150 : 30)
        );

        KeyFrame primaryOpacityKeyFrames = new KeyFrame(Duration.millis(0),
                primaryOpacityKeyValues.toArray(KeyValue[]::new));

        KeyFrame finallyOpacityKeyFrames = new KeyFrame(Duration.millis(700),
                finallyOpacityKeyValues.toArray(KeyValue[]::new));
        
        Timeline anim = new Timeline(
                primaryKeyFrames,
                finallyKeyFrames,
                primaryOpacityKeyFrames,
                finallyOpacityKeyFrames
        );

        anim.play();
    }

    private void createOpacityAnim(VBox boxNodes, List<KeyValue> primaryKeyValues, List<KeyValue> finallyKeyValues, double iOpacity,double fOpacity) {
        boxNodes.getChildren().forEach(node -> {
            KeyValue primaryKeyValue = new KeyValue(node.opacityProperty(), iOpacity);
            primaryKeyValues.add(primaryKeyValue);

            KeyValue finallyKeyValue = new KeyValue(node.opacityProperty(), fOpacity);
            finallyKeyValues.add(finallyKeyValue);
        });
    }

    private void loadingForms() {
        try {
            // load login form
            FXMLLoader fxmlLoginForm = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/flexible/authentications/login/login.fxml")));
            fxmlLoginForm.setControllerFactory(param -> new LoginController(root){
                @Override
                public void initialize(URL location, ResourceBundle resources) {
                    super.initialize(location, resources);
                }
            });
            AnchorPane loginForm = fxmlLoginForm.load();

            // load register form
            FXMLLoader fxmlRegisterForm = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/flexible/authentications/register/register.fxml")));
            fxmlRegisterForm.setControllerFactory(param -> new RegisterController(root){
                @Override
                public void initialize(URL location, ResourceBundle resources) {
                    super.initialize(location, resources);
                }
            });
            AnchorPane registerForm = fxmlRegisterForm.load();

            partLogin.getChildren().setAll(loginForm);
            partRegister.getChildren().setAll(registerForm);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

    }
}
