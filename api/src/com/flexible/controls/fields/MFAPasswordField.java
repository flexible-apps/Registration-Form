package com.flexible.controls.fields;

import com.flexible.controls.glyph.MFAGlyph;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.NodeUtils;
import io.github.palexdev.materialfx.utils.StyleablePropertiesUtils;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class MFAPasswordField extends MFXPasswordField {

    private final static String CLASS_NAME = "mfa-password-field";

    public static final String BULLET = "\u2022";

    public MFAPasswordField() {
        this("");
    }

    public MFAPasswordField(String text) {
        this(text, "");
    }

    public MFAPasswordField(String text, String promptText) {
        this(text, promptText, "");
    }

    public MFAPasswordField(String text, String promptText, String floatingText) {
        super(text, promptText, floatingText);
        initialize();
    }

    private void initialize() {
        getStyleClass().add(CLASS_NAME);
        setBehavior();
        defaultTrailingIcon();
        defaultContextMenu();

        hideCharacterProperty().set(BULLET);
        hideCharacterProperty().set(BULLET);
    }

    @Override
    protected void defaultTrailingIcon() {
        MFAGlyph glyph = new MFAGlyph(MFAConstants.GLYPH_EYE);
        showPasswordProperty().addListener((observable) -> glyph.setGlyph(isShowPassword()? MFAConstants.GLYPH_EYE_OFF : MFAConstants.GLYPH_EYE));

        MFXIconWrapper showPasswordIcon = new MFXIconWrapper(glyph, 24).defaultRippleGeneratorBehavior();
        NodeUtils.makeRegionCircular(showPasswordIcon);
        showPasswordIcon.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            setShowPassword(!isShowPassword());

            // Workaround for caret being positioned (only visually) wrongly
            int currPos = delegateGetCaretPosition();
            positionCaret(0);
            positionCaret(currPos);

            event.consume();
        });

        setTrailingIcon(showPasswordIcon);
    }

    private static class StyleableProperties {
        private static final StyleablePropertyFactory<MFAPasswordField> FACTORY = new StyleablePropertyFactory<>(MFXPasswordField.getClassCssMetaData());
        private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

        private static final CssMetaData<MFAPasswordField, String> HIDE_CHARACTER =
                FACTORY.createStringCssMetaData(
                        "-mfx-hide-character",
                        MFXPasswordField::hideCharacterProperty,
                        BULLET
                );

        static {
            cssMetaDataList = StyleablePropertiesUtils.cssMetaDataList(
                    MFXPasswordField.getClassCssMetaData(),
                    HIDE_CHARACTER
            );
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.cssMetaDataList;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
