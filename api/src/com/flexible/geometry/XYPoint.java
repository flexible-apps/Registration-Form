package com.flexible.geometry;

public class XYPoint {
    public static final XYPoint XY_POINT =
            new XYPoint(0, 0, 0, 0);
    private double x;

    private double y;

    private double translateX;

    private double translateY;


    public static XYPoint getInstance() {
        return XY_POINT;
    }

    public XYPoint(double x, double y, double translateX, double translateY) {
        this.x = x;
        this.y = y;
        this.translateX = translateX;
        this.translateY = translateY;
    }

    public XYPoint(double x, double y) {
        this(x, y, 0, 0);
    }

    public XYPoint() {
        this(0, 0, 0, 0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTranslateX() {
        return translateX;
    }

    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public void setTranslateY(double translateY) {
        this.translateY = translateY;
    }

    @Override
    public String toString() {
        return "XYPoint{" + "x=" + x +
                ", y=" + y +
                ", translateX=" + translateX +
                ", translateY=" + translateY +
                '}';
    }
}
