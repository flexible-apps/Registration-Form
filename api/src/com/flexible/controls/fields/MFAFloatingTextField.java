package com.flexible.controls.fields;

import com.flexible.annotations.Controller;
import com.flexible.implementions.MFAConsumerObservableValue;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Objects;

@SuppressWarnings("unused")
@Controller(name = "MFAFloatingTextField")
public class MFAFloatingTextField extends MFAParent<TextField> {
    private final static String CLASS_NAME = "mfa-floating-text-field";

    protected final PseudoClass APPLY_EVENT = PseudoClass.getPseudoClass("applyEvent");

    private TextField textField;

    private final BooleanProperty focusEntry = new SimpleBooleanProperty(false);

    private final StringProperty text =
            new SimpleStringProperty(this, "Text", "");

    private final BooleanProperty applyEvent =
            new SimpleBooleanProperty(this, "Apply Event", false);

    public MFAFloatingTextField() {
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
    public void updateNodeOrientation(NodeOrientation orientation) {
        floatingLabel.setNodeOrientation(orientation);
        textField.setNodeOrientation(orientation);
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
    public TextField getNode() {
        textField = new TextField("");
        textField.textProperty().bindBidirectional(textProperty());
        textField.promptTextProperty().bindBidirectional(promptTextProperty());
        textField.setMaxWidth(Double.MAX_VALUE);
        focusEntryProperty().bind(textField.focusedProperty());
        HBox.setHgrow(textField, Priority.ALWAYS);
        HBox.setMargin(textField, new Insets(0,5,0,0));
        Platform.runLater(() -> {
            if (textField.isFocused()) {
                setCustomFocused(true);
            }
        });
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            setCustomFocused(newValue);
        });
        return textField;
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

    public <K> void setText(MFAConsumerObservableValue<K> text) {
        this.text.set((String) text.accept());
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

    public boolean isFocusEntry() {
        return focusEntry.get();
    }

    public BooleanProperty focusEntryProperty() {
        return focusEntry;
    }

    public void setFocusEntry(boolean focusEntry) {
        this.focusEntry.set(focusEntry);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(getClass().getResource("/com/flexible/resources/stylesheets/MFAFloatingTextField.css")).toExternalForm();
    }
}
