package com.flexible.controls.glyph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Objects;

public class ScaleBean {
    private final DoubleProperty x =
            new SimpleDoubleProperty(1.0);
    private final DoubleProperty y =
            new SimpleDoubleProperty(1.0);

    public ScaleBean(double x, double y) {
        setX(x);
        setY(y);
    }

    public ScaleBean(double xy) {
        setX(xy);
        setY(xy);
    }

    public static ScaleBean of(double xValue, double yValue) {
        return new ScaleBean(xValue, yValue);
    }

    public static ScaleBean of(double xyValue) {
        return new ScaleBean(xyValue);
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScaleBean that = (ScaleBean) o;
        return getX() == (that.getX()) && getY() == (that.getY());
    }

    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "X/Y: [" + getX() + ", " + getY() + "]";
    }
}
