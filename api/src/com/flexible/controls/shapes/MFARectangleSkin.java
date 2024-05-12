package com.flexible.controls.shapes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.SkinBase;
import javafx.scene.shape.*;

public class MFARectangleSkin extends SkinBase<MFARectangle> {

    private final Group group;
    private final MoveTo moveTo;
    private final Path path;
    private final HLineTo top;
    private final HLineTo bottom;
    private final VLineTo left;
    private final VLineTo right;
    private final ArcTo topLeftArc;
    private final ArcTo topRightArc;
    private final ArcTo bottomLeftArc;
    private final ArcTo bottomRightArc;

    public MFARectangleSkin(MFARectangle control) {
        super(control);
        path = new Path();
        path.setFill(control.getFill());
        path.setStroke(control.getStroke());
        path.setStrokeWidth(control.getStrokeWidth());
        moveTo = new MoveTo(control.getBorderRadius().getTop(), 0);

        top = new HLineTo(
                control.getRectangleWidth() - control.getBorderRadius().getRight()
        );

        topRightArc = new ArcTo(
                control.getBorderRadius().getRight(),
                control.getBorderRadius().getRight(),
                0,
                control.getRectangleWidth(),
                control.getBorderRadius().getRight(),
                false,
                true
        );

        right = new VLineTo(
                control.getRectangleHeight() - control.getBorderRadius().getBottom()
        );

        bottomRightArc = new ArcTo(
                control.getBorderRadius().getBottom(),
                control.getBorderRadius().getBottom(),
                0,
                control.getRectangleWidth() - control.getBorderRadius().getBottom(),
                control.getRectangleHeight(),
                false,
                true
        );

        bottom = new HLineTo(
                control.getBorderRadius().getLeft()
        );

        bottomLeftArc = new ArcTo(
                control.getBorderRadius().getLeft(),
                control.getBorderRadius().getLeft(),
                0,
                0,
                control.getRectangleHeight() - control.getBorderRadius().getLeft(),
                false,
                true
        );

        left = new VLineTo(
                control.getBorderRadius().getTop()
        );

        topLeftArc = new ArcTo(
                control.getBorderRadius().getTop(),
                control.getBorderRadius().getTop(),
                0,
                control.getBorderRadius().getTop(),
                0,
                false,
                true
        );

        path.getElements().addAll(moveTo, top, topRightArc, right, bottomRightArc, bottom, bottomLeftArc, left, topLeftArc);
        group = new Group();
        group.getChildren().setAll(path);

        getChildren().setAll(group);

        registerChangeListener(control.rectangleWidthProperty(), observableValue -> updateWidthShape());
        registerChangeListener(control.rectangleHeightProperty(), observableValue -> updateHeightShape());
        registerChangeListener(control.fillProperty(), observableValue -> updateFillShape());
        registerChangeListener(control.strokeProperty(), observableValue -> updateStrokeShape());
        registerChangeListener(control.strokeWidthProperty(), observableValue -> updateStrokeWidthShape());

        registerChangeListener(control.borderRadiusProperty(), observableValue -> updateTopCurve());
        registerChangeListener(control.borderRadiusProperty(), observableValue -> updateRightCurve());
        registerChangeListener(control.borderRadiusProperty(), observableValue -> updateBottomCurve());
        registerChangeListener(control.borderRadiusProperty(), observableValue -> updateLeftCurve());


        registerChangeListener(control.topLeftRadiusProperty(), observableValue -> {
            updateBorderInsets();
            updateLeftCurve();
        });
        registerChangeListener(control.topRightRadiusProperty(), observableValue -> {
            updateBorderInsets();
            updateLeftCurve();
        });
        registerChangeListener(control.bottomLeftRadiusProperty(), observableValue -> {
            updateBorderInsets();
            updateLeftCurve();
        });

        registerChangeListener(control.bottomRightRadiusProperty(), observableValue -> {
            updateBorderInsets();
            updateLeftCurve();
        });
    }

    private void updateBorderInsets() {
        MFARectangle control = getSkinnable();
        control.setBorderRadius(new Insets(control.getTopLeftRadius(), control.getTopRightRadius(), control.getBottomRightRadius(), control.getBottomLeftRadius()));
    }

    private void updateLeftCurve() {
        right.setY(getSkinnable().getRectangleHeight() - getSkinnable().getBorderRadius().getBottom());
        bottomRightArc.setRadiusX(getSkinnable().getBorderRadius().getBottom());
        bottomRightArc.setRadiusY(getSkinnable().getBorderRadius().getBottom());
        bottomRightArc.setX(getSkinnable().getRectangleWidth() - getSkinnable().getBorderRadius().getBottom());
    }

    private void updateBottomCurve() {
        bottom.setX(getSkinnable().getBorderRadius().getLeft());
        bottomLeftArc.setRadiusX(getSkinnable().getBorderRadius().getLeft());
        bottomLeftArc.setRadiusY(getSkinnable().getBorderRadius().getLeft());
        bottomLeftArc.setY(getSkinnable().getRectangleHeight() - getSkinnable().getBorderRadius().getLeft());
    }

    private void updateRightCurve() {
        top.setX(getSkinnable().getRectangleWidth() - getSkinnable().getBorderRadius().getRight());
        topRightArc.setRadiusX(getSkinnable().getBorderRadius().getRight());
        topRightArc.setRadiusY(getSkinnable().getBorderRadius().getRight());
        topRightArc.setY(getSkinnable().getBorderRadius().getRight());
    }

    private void updateTopCurve() {
        moveTo.setX(getSkinnable().getBorderRadius().getTop());
        left.setY(getSkinnable().getBorderRadius().getTop());
        topLeftArc.setRadiusX(getSkinnable().getBorderRadius().getTop());
        topLeftArc.setRadiusY(getSkinnable().getBorderRadius().getTop());
        topLeftArc.setX(getSkinnable().getBorderRadius().getTop());
    }

    private void updateHeightShape() {
        right.setY(getSkinnable().getRectangleHeight() - getSkinnable().getBorderRadius().getBottom());
        bottomRightArc.setY(getSkinnable().getRectangleHeight());
        bottomLeftArc.setY(getSkinnable().getRectangleHeight() - getSkinnable().getBorderRadius().getBottom());
    }


    private void updateWidthShape() {
        top.setX(getSkinnable().getRectangleWidth() - getSkinnable().getBorderRadius().getRight());
        topRightArc.setX(getSkinnable().getRectangleWidth());
        bottomRightArc.setX(getSkinnable().getRectangleWidth() - getSkinnable().getBorderRadius().getRight());
    }

    private void updateFillShape() {
        path.setFill(getSkinnable().getFill());
    }

    private void updateStrokeWidthShape() {
        path.setStrokeWidth(getSkinnable().getStrokeWidth());
    }

    private void updateStrokeShape() {
        path.setStroke(getSkinnable().getStroke());
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().getRectangleWidth();
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().getRectangleHeight();
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().getRectangleWidth();
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().getRectangleHeight();
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().getRectangleWidth();
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().getRectangleHeight();
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(group, 0, 0, getSkinnable().getRectangleWidth(), getSkinnable().getRectangleHeight(), 0, HPos.LEFT, VPos.TOP);
    }
}
