package com.flexible.controls.progress;

import com.flexible.annotations.Controller;
import com.flexible.implementions.MFAStyleSheet;
import javafx.scene.control.ProgressIndicator;

import java.util.Objects;

@Controller(name = "MFAProgressIndicator")
public class MFAProgressIndicator extends ProgressIndicator implements MFAStyleSheet {
    private final static String STYLE_NAME = "mfa-progress-indicator";

    @SuppressWarnings("unused")
    public static MFAProgressIndicator SPINNER_INDICATOR =
            new MFAProgressIndicator();

    public MFAProgressIndicator() {
        initialize();
    }

    @Override
    public void initialize() {
        this.getStyleClass().add(STYLE_NAME);
        this.setPrefSize(15,15);
    }

    @Override
    public String getRequireStyleSheet() {
        return Objects.requireNonNull(getClass().getResource("/com/flexible/resources/stylesheets/MFAProgressIndicator.css")).toExternalForm();
    }

    @Override
    public String getUserAgentStylesheet() {
        return getRequireStyleSheet();
    }
}
