package com.flexible.controls.fields;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Objects;

public class MFAFloatingPasswordField extends MFAParent<MFXPasswordField> {
    private final static String CLASS_NAME = "mfa-floating-password-field";
    private MFAPasswordField passwordField;
    protected final PseudoClass APPLY_EVENT = PseudoClass.getPseudoClass("applyEvent");

    private final BooleanProperty focusEntry = new SimpleBooleanProperty(false);

    private final StringProperty text =
            new SimpleStringProperty(this, "Text", "");

    private final BooleanProperty applyEvent =
            new SimpleBooleanProperty(this, "Apply Event", false);

    public MFAFloatingPasswordField() {
        initialize();
    }

    @Override
    public void initialize() {
        this.getStyleClass().setAll(CLASS_NAME);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefWidth(WIDTH);
        this.setPrefHeight(HEIGHT);



        HBox textBox = getTextBox();
        HBox boxTextField = getBoxTextField();

        textBox.visibleProperty().bindBidirectional(displayFloatingTextProperty());

        updateErrorAction();
        errorProperty().addListener((observable, oldValue, newValue) -> {
            setCustomFocused(oldValue);
            updateErrorAction();
        });

        updateEvent();
        applyEventProperty().addListener(observable -> updateEvent());

        customFocusedProperty().addListener(observable -> updateErrorAction());

        updateNodeOrientation(getNodeOrientation());
        nodeOrientationProperty().addListener((observable, oldValue, newValue) -> updateNodeOrientation(newValue));

        getChildren().setAll(textBox, boxTextField);

    }
    private void updateEvent() {
        pseudoClassStateChanged(APPLY_EVENT, isApplyEvent());
    }

    @Override
    public HBox getBoxTextField() {
        HBox box = new HBox();
        box.setMinHeight(30);
        box.setMaxHeight(30);
        box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("mfa-box-children");

        box.getChildren().add(getNode());
        return box;

    }

    @Override
    public MFXPasswordField getNode() {
        passwordField = new MFAPasswordField();
        passwordField.setMinHeight(30);
        passwordField.setMaxHeight(30);
        passwordField.textProperty().bindBidirectional(textProperty());
        passwordField.promptTextProperty().bindBidirectional(promptTextProperty());
        passwordField.setMaxWidth(Double.MAX_VALUE);
        focusEntryProperty().bind(passwordField.focusWithinProperty());
//        focusEntryProperty().bind(passwordField.focusedProperty());
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        HBox.setMargin(passwordField, new Insets(0,5,0,0));
        Platform.runLater(() -> {
            if (passwordField.isFocusWithin()) {
                setCustomFocused(true);
            }
        });
        passwordField.focusWithinProperty().addListener((observable, oldValue, newValue) -> {
            setCustomFocused(newValue);
        });
        return passwordField;
    }

    @Override
    public void updateNodeOrientation(NodeOrientation orientation) {
        floatingLabel.setNodeOrientation(orientation);
        passwordField.setNodeOrientation(orientation);
    }

    public boolean isFocusEntry() {
        return focusEntry.get();
    }

    public BooleanProperty focusEntryProperty() {
        return focusEntry;
    }

    public void setFocusEntry(boolean focusEntry) {
        this.focusEntry.set(focusEntry);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public boolean isApplyEvent() {
        return applyEvent.get();
    }

    public BooleanProperty applyEventProperty() {
        return applyEvent;
    }

    public void setApplyEvent(boolean applyEvent) {
        this.applyEvent.set(applyEvent);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(getClass().getResource("/com/flexible/resources/stylesheets/MFAFloatingPasswordField.css")).toExternalForm();
    }
}
