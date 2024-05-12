package com.flexible.controls.shapes;

import com.flexible.annotations.Controller;
import com.flexible.implementions.MFAStyleSheet;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.*;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class MFARectangle extends Control implements MFAStyleSheet {
    private static final StyleablePropertyFactory<MFARectangle> FACTORY =
            new StyleablePropertyFactory<>(Control.getClassCssMetaData());

    private final static String NAME_STYLE_CLASS = "mfa-rectangle";
    private final static double DEFAULT_SIZE = 100.;
    private final static double DEFAULT_STROKE_WIDTH = 1.5;
    private final DoubleProperty topLeftRadius = new SimpleDoubleProperty(0);
    private final DoubleProperty topRightRadius = new SimpleDoubleProperty(0);
    private final DoubleProperty bottomLeftRadius = new SimpleDoubleProperty(0);
    private final DoubleProperty bottomRightRadius = new SimpleDoubleProperty(0);

    public MFARectangle(double prefWidth, double prefHeight, Insets borderRadius) {
        super();
        setRectangleWidth(prefWidth);
        setRectangleHeight(prefHeight);
        setBorderRadius(borderRadius);
        initialize();
    }

    public MFARectangle() {
        super();
        initialize();
    }

    public MFARectangle(Insets borderRadius) {
        super();
        setBorderRadius(borderRadius);
        initialize();
    }

    @Override
    public void initialize() {
        this.getStyleClass().add(NAME_STYLE_CLASS);
    }

    public StyleableObjectProperty<Insets> borderRadius = new SimpleStyleableObjectProperty<>(
            Properties.BORDER_RADIUS,
            this,
            "borderRadius",
            Insets.EMPTY
    );

    public StyleableObjectProperty<Paint> fill = new SimpleStyleableObjectProperty<>(
            Properties.FILL,
            this,
            "rectangleFill",
            Color.BLACK
    );

    public StyleableObjectProperty<Paint> stroke = new SimpleStyleableObjectProperty<>(
            Properties.STROKE,
            this,
            "rectangleStrokeFill",
            Color.BLACK
    );

    public StyleableDoubleProperty strokeWidth = new SimpleStyleableDoubleProperty(
            Properties.STROKE_WIDTH,
            this,
            "strokeWidth",
            DEFAULT_STROKE_WIDTH
    );

    public StyleableDoubleProperty prefWidth = new SimpleStyleableDoubleProperty(
            Properties.RECT_WIDTH,
            this,
            "rectangleWidth",
            DEFAULT_SIZE
    );

    public StyleableDoubleProperty prefHeight = new SimpleStyleableDoubleProperty(
            Properties.RECT_HEIGHT,
            this,
            "rectangleHeight",
            DEFAULT_SIZE
    );

    public Insets getBorderRadius() {
        return borderRadius == null ? Insets.EMPTY : borderRadius.get();
    }

    public void setBorderRadius(Insets borderRadius) {
        this.borderRadius.set(borderRadius);
    }

    public StyleableObjectProperty<Insets> borderRadiusProperty() {
        return borderRadius;
    }

    public Paint getFill() {
        return fill.get();
    }

    public StyleableObjectProperty<Paint> fillProperty() {
        return fill;
    }

    public void setFill(Paint fill) {
        this.fill.set(fill);
    }

    public Paint getStroke() {
        return stroke.get();
    }

    public StyleableObjectProperty<Paint> strokeProperty() {
        return stroke;
    }

    public void setStroke(Paint stroke) {
        this.stroke.set(stroke);
    }

    public double getStrokeWidth() {
        return strokeWidth.get();
    }

    public StyleableDoubleProperty strokeWidthProperty() {
        return strokeWidth;
    }

    public void setStrokeWidth(double stroke_width) {
        this.strokeWidth.set(stroke_width);
    }

    public double getRectangleWidth() {
        return prefWidth.get();
    }

    public StyleableDoubleProperty rectangleWidthProperty() {
        return prefWidth;
    }

    public void setRectangleWidth(double rectangleWidth) {
        this.prefWidth.set(rectangleWidth);
    }

    public double getRectangleHeight() {
        return prefHeight.get();
    }

    public StyleableDoubleProperty rectangleHeightProperty() {
        return prefHeight;
    }

    public void setRectangleHeight(double rectangleHeight) {
        this.prefHeight.set(rectangleHeight);
    }

    public double getTopLeftRadius() {
        return topLeftRadius == null ? 0 : topLeftRadius.get();
    }

    public DoubleProperty topLeftRadiusProperty() {
        return topLeftRadius;
    }

    public void setTopLeftRadius(double topLeftRadius) {
        this.topLeftRadius.set(topLeftRadius);
    }

    public double getTopRightRadius() {
        return topRightRadius == null ? 0 : topRightRadius.get();
    }

    public DoubleProperty topRightRadiusProperty() {
        return topRightRadius;
    }

    public void setTopRightRadius(double topRightRadius) {
        this.topRightRadius.set(topRightRadius);
    }

    public double getBottomLeftRadius() {
        return bottomLeftRadius == null ? 0 : bottomLeftRadius.get();
    }

    public DoubleProperty bottomLeftRadiusProperty() {
        return bottomLeftRadius;
    }

    public void setBottomLeftRadius(double bottomLeftRadius) {
        this.bottomLeftRadius.set(bottomLeftRadius);
    }

    public double getBottomRightRadius() {
        return bottomRightRadius == null ? 0 : bottomRightRadius.get();
    }

    public DoubleProperty bottomRightRadiusProperty() {
        return bottomRightRadius;
    }

    public void setBottomRightRadius(double bottomRightRadius) {
        this.bottomRightRadius.set(bottomRightRadius);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MFARectangleSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getRequireStyleSheet();
    }

    @Override
    public String getRequireStyleSheet() {
        return Objects.requireNonNull(getClass().getResource("/com/flexible/resources/stylesheets/MFARectangle.css"))
                .toExternalForm();
    }

    static class Properties {
        private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

        public final static CssMetaData<MFARectangle, Insets> BORDER_RADIUS = FACTORY.createInsetsCssMetaData(
                "-mfa-rectangle-border-radius",
                MFARectangle::borderRadiusProperty,
                Insets.EMPTY
        );

        public final static CssMetaData<MFARectangle, Paint> FILL = FACTORY.createPaintCssMetaData(
                "-mfa-rectangle-fill",
                MFARectangle::fillProperty,
                Color.BLACK
        );

        public final static CssMetaData<MFARectangle, Paint> STROKE = FACTORY.createPaintCssMetaData(
                "-mfa-rectangle-stroke-fill",
                MFARectangle::strokeProperty,
                Color.BLACK
        );

        public final static CssMetaData<MFARectangle, Number> STROKE_WIDTH = FACTORY.createSizeCssMetaData(
                "-mfa-rectangle-stroke-width",
                MFARectangle::strokeWidthProperty,
                MFARectangle.DEFAULT_STROKE_WIDTH
        );

        public final static CssMetaData<MFARectangle, Number> RECT_WIDTH = FACTORY.createSizeCssMetaData(
                "-mfa-rectangle-pref-width",
                MFARectangle::rectangleWidthProperty,
                MFARectangle.DEFAULT_SIZE
        );

        public final static CssMetaData<MFARectangle, Number> RECT_HEIGHT = FACTORY.createSizeCssMetaData(
                "-mfa-rectangle-pref-height",
                MFARectangle::rectangleWidthProperty,
                MFARectangle.DEFAULT_SIZE
        );

        static {
            List<CssMetaData<? extends Styleable, ?>> btnCssMetaData = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(btnCssMetaData, BORDER_RADIUS, FILL, STROKE, STROKE_WIDTH, RECT_WIDTH, RECT_HEIGHT);
            cssMetaDataList = Collections.unmodifiableList(btnCssMetaData);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return Properties.cssMetaDataList;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override
    public String toString() {
        return super.toString() + "'" + MFARectangle.class.getName() + "'";
    }
}

