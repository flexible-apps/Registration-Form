package com.flexible.controls.fields;

import com.flexible.annotations.Controller;
import com.flexible.implementions.MFAConsumer;
import com.flexible.implementions.MFAConsumerObservableValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

@Controller
public abstract class MFAParent<T> extends VBox {
    protected final PseudoClass CUSTOM_ERROR = PseudoClass.getPseudoClass("error");
    protected final PseudoClass CUSTOM_FOCUSED = PseudoClass.getPseudoClass("focus");
    protected final static double HEIGHT = 50;
    protected final static double WIDTH = 150;
    protected boolean isValid;
    protected Text floatingLabel;
    private final BooleanProperty error =
            new SimpleBooleanProperty(this, "Valid Text", false);
    private final BooleanProperty customFocused =
            new SimpleBooleanProperty(this, "Custom Focused", false);
    private final BooleanProperty displayFloatingText =
            new SimpleBooleanProperty(this, "Hide Floating Text", true);
    private final StringProperty floatingText =
            new SimpleStringProperty(this, "Floating Text", "");
    private final StringProperty promptText =
            new SimpleStringProperty(this, "Prompt Text", "");

    public MFAParent() {
    }

    public abstract void initialize();

    public abstract HBox getBoxTextField();

    public abstract T getNode();

    public abstract void updateNodeOrientation(NodeOrientation orientation);

    protected HBox getTextBox() {
        HBox box = new HBox();
        box.setMaxWidth(Region.USE_PREF_SIZE);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("mfa-box-text");
        box.getChildren().setAll(getLabel());
        return box;
    }

    protected Text getLabel() {
        floatingLabel = new Text(getFloatingText());
        floatingLabel.getStyleClass().add("mfa-floating-text");
        floatingTextProperty().addListener((observable, oldValue, newValue) -> floatingLabel.setText(newValue));
        return floatingLabel;
    }

    public <T> void setPatternObservableValueListener(ObservableValue<T> textProperty, MFAConsumer<Boolean, T> matcher) {
        textProperty.addListener((observable, oldValue, newValue) -> setError(!matcher.accept(newValue)));
    }

    public <T> void setPatternObservableValueListener(MFAConsumerObservableValue<ObservableValue<T>> textProperty, MFAConsumer<Boolean, T> matcher, Consumer<T> resultMatcher) {
        textProperty.accept().addListener((observable, oldValue, newValue) -> {
            isValid = matcher.accept(newValue);
            setError(!isValid);
            if (isValid) {
                resultMatcher.accept(newValue);
            }
        });
    }

    protected void updateErrorAction() {
        if (isCustomFocused() && isError()) {
            pseudoClassStateChanged(CUSTOM_ERROR, true);
            pseudoClassStateChanged(CUSTOM_FOCUSED, false);
        } else if (!isCustomFocused() && isError()) {
            pseudoClassStateChanged(CUSTOM_ERROR, true);
            pseudoClassStateChanged(CUSTOM_FOCUSED, false);
        } else if (!isCustomFocused() && !isError()) {
            pseudoClassStateChanged(CUSTOM_ERROR, false);
            pseudoClassStateChanged(CUSTOM_FOCUSED, false);
        } else if (isCustomFocused() && !isError()) {
            pseudoClassStateChanged(CUSTOM_ERROR, false);
            pseudoClassStateChanged(CUSTOM_FOCUSED, true);
        }
    }

    public boolean isError() {
        return error.get();
    }

    public BooleanProperty errorProperty() {
        return error;
    }

    public void setError(boolean error) {
        this.error.set(error);
    }

    public boolean isCustomFocused() {
        return customFocused.get();
    }

    public BooleanProperty customFocusedProperty() {
        return customFocused;
    }

    public void setCustomFocused(boolean customFocused) {
        this.customFocused.set(customFocused);
    }

    public boolean getDisplayFloatingText() {
        return displayFloatingText.get();
    }

    public BooleanProperty displayFloatingTextProperty() {
        return displayFloatingText;
    }

    public void setDisplayFloatingText(boolean displayFloatingText) {
        this.displayFloatingText.set(displayFloatingText);
    }

    public String getFloatingText() {
        return floatingText.get();
    }

    public StringProperty floatingTextProperty() {
        return floatingText;
    }

    public void setFloatingText(String floatingText) {
        this.floatingText.set(floatingText);
    }

    public <K> void setFloatingText(MFAConsumerObservableValue<K> floatingText) {
        this.floatingText.set((String) floatingText.accept());
    }

    public String getPromptText() {
        return promptText.get();
    }

    public StringProperty promptTextProperty() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText.set(promptText);
    }

    public <K> void setPromptText(MFAConsumerObservableValue<K> promptText) {
        this.promptText.set((String) promptText.accept());
    }
}
