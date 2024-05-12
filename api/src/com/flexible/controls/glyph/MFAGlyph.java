package com.flexible.controls.glyph;

import com.flexible.implementions.Call;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.Styleable;
import javafx.css.StyleableStringProperty;
import javafx.css.converter.StringConverter;
import javafx.scene.shape.SVGPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MFAGlyph extends SVGPath {
    private static final String STYLE_CLASS = "mfa-glyph";
    private ScaleBean scaleBean;
    private final StyleableStringProperty glyph =
            new SimpleStyleableStringProperty(StyleableProperties.GLYPH,this, "Glyph", "");

    public MFAGlyph() {
        initialize();
    }

    public MFAGlyph(String glyph) {
        setGlyph(glyph);
        initialize();
    }

    public MFAGlyph(String glyph, Call<ScaleBean> scaleBean) {
        setGlyph(glyph);
        setScaleBean(scaleBean.get());
        initialize();
    }

    private void initialize() {
        this.getStyleClass().setAll(STYLE_CLASS);
        this.setContent(getGlyph());
        glyphProperty().addListener((observable, oldValue, newValue) -> this.setContent(newValue));
        update();
    }

    private void update() {
        this.setScaleX(getScaleBean().getX());
        this.setScaleY(getScaleBean().getY());
    }

    public ScaleBean getScaleBean() {
        return Objects.requireNonNullElseGet(scaleBean, () -> ScaleBean.of(0.8));
    }

    public void setScaleBean(ScaleBean scaleBean) {
        this.scaleBean = scaleBean;
    }


    public String getGlyph() {
        return glyph.get();
    }

    public StyleableStringProperty glyphProperty() {
        return glyph;
    }

    public void setGlyph(String glyph) {
        this.glyph.set(glyph);
    }

    private static class StyleableProperties {
        private static final CssMetaData<MFAGlyph, String> GLYPH =
                new CssMetaData<>("-mfa-glyph",
                        StringConverter.getInstance(), "") {
                    @Override
                    public boolean isSettable(MFAGlyph control) {
                        return !control.glyph.isBound();
                    }

                    @Override
                    public StyleableStringProperty getStyleableProperty(MFAGlyph control) {
                        return control.glyphProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(SVGPath.getClassCssMetaData());
            Collections.addAll(styleables, GLYPH);
            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

}
