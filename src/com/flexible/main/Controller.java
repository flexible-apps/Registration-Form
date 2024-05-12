package com.flexible.main;

import com.flexible.annotations.RegularExpiration;
import com.flexible.assets.MFAReflections;
import com.flexible.assets.ScreenSnapshot;
import com.flexible.controls.fields.MFAFloatingTextField;
import com.flexible.geometry.XYPoint;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@com.flexible.annotations.Controller
public class Controller implements Initializable {
    private static final double delta = 2;
    private static final double radius = 50;

    private XYPoint point = XYPoint.getInstance();

    @FXML
    private AnchorPane root;

    @FXML
    @RegularExpiration(pattern = "^[A-Z][a-zA-Z ]+$")
    private MFAFloatingTextField fieldUsername;

    @FXML
    @RegularExpiration(pattern = "^[a-zA-Z0-9 @,:^/\\\\]+$")
    private MFAFloatingTextField fieldPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            getMask();
            ScreenSnapshot.snap(root);
        });

        applyValidations(fieldUsername, "fieldUsername");
        applyValidations(fieldPassword, "fieldPassword");

        transformations();
    }

    private void applyValidations(MFAFloatingTextField mfaFloatingTextField, String declarationFieldName) {
        MFAReflections.getDeclarationAnnotations(Controller.class.getDeclaredFields())
                .ifEqualDeclarationFieldName(declarationFieldName)
                .ifEqualController(RegularExpiration.class)
                .toAssertBuilder()
                .filter(Objects::nonNull)
                .map(o -> (RegularExpiration) o)
                .ifPresentOrElse(regularExpiration -> mfaFloatingTextField.setPatternObservableValueListener(mfaFloatingTextField::textProperty,
                        s -> s.matches(regularExpiration.pattern()), s -> {}), () -> System.err.println("cant found regular expression."));
    }

    private void getMask() {
        Rectangle mask = new Rectangle(delta, delta, root.getWidth() - (delta * 2), root.getHeight() - (delta * 2));
        mask.setArcWidth(radius);
        mask.setArcHeight(radius);
        root.setClip(mask);
    }

    private void transformations() {
        root.setOnMousePressed(event -> {
            point.setX(event.getSceneX());
            point.setY(event.getSceneY());
            point.setTranslateX(root.getTranslateX());
            point.setTranslateY(root.getTranslateY());
        });

        root.setOnMouseDragged(event -> {
            Stage stage = MainApplication.getStage();
            stage.setX(event.getScreenX() - point.getX() + point.getTranslateX());
            stage.setY(event.getScreenY() - point.getY() + point.getTranslateY());

             // add a simple opacity animation for root of this app.
            root.setOpacity(0.9);
        });

        root.setOnMouseReleased(event -> {
            point = XYPoint.XY_POINT;

            // delete opacity animation
            root.setOpacity(1.0);
        });
    }
}
