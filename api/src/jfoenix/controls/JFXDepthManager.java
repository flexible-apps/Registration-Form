package jfoenix.controls;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * it will create a shadow effect for a given temps and a specified depth level.
 * depth levels are {0,1,2,3,4,5}
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class JFXDepthManager {


    public JFXDepthManager(Node control, ObjectProperty<Color> color, IntegerProperty level) {
        createMaterialNode(control, color.get(), level.get());

    }

    private static DropShadow[] shadows(Color color) {
        return new DropShadow[]{
                new DropShadow(BlurType.GAUSSIAN, color, 0, 0, 0, 0),
                new DropShadow(BlurType.GAUSSIAN, color, 10, 0.12, -1, 2),
                new DropShadow(BlurType.GAUSSIAN, color, 15, 0.16, 0, 4),
                new DropShadow(BlurType.GAUSSIAN, color, 20, 0.19, 0, 6),
                new DropShadow(BlurType.GAUSSIAN, color, 25, 0.25, 0, 8),
                new DropShadow(BlurType.GAUSSIAN, color, 30, 0.30, 0, 10)
        };
    }

    public static DropShadow[] depth = new DropShadow[] {
            new DropShadow(BlurType.GAUSSIAN, Color.rgb(205, 205, 205, 0), 0, 0, 0, 0),
            new DropShadow(BlurType.GAUSSIAN, Color.rgb(205, 205, 205, 0.26), 10, 0.12, -1, 2),
            new DropShadow(BlurType.GAUSSIAN, Color.rgb(205, 205, 205, 0.26), 15, 0.16, 0, 4),
            new DropShadow(BlurType.GAUSSIAN, Color.rgb(205, 205, 205, 0.26), 20, 0.19, 0, 6),
            new DropShadow(BlurType.GAUSSIAN, Color.rgb(205, 205, 205, 0.26), 25, 0.25, 0, 8),
            new DropShadow(BlurType.GAUSSIAN, Color.rgb(205, 205, 205, 0.26), 30, 0.30, 0, 10),
            new DropShadow(BlurType.GAUSSIAN, Color.rgb(205, 205, 205, 0.16), 5, 0.5, 0, 0) // I'm insert this index=6
    };

    /**
     * this method is used to insert shadow effect to the temps,
     * however the shadow is not real ( gets affected with temps transform)
     * <p>
     * use {@link #createMaterialNode(Node, int)} instead to generate a real shadow
     */
    public static void setDepth(Node control, int level) {
        level = Math.max(level, 0);
        level = Math.min(level, 5);
        control.setEffect(new DropShadow(BlurType.GAUSSIAN,
                depth[level].getColor(),
                depth[level].getRadius(),
                depth[level].getSpread(),
                depth[level].getOffsetX(),
                depth[level].getOffsetY()));
    }

    public static int getLevels() {
        return depth.length;
    }

    public static DropShadow getShadowAt(int level) {
        return depth[level];
    }

    /**
     * this method will generate a new container temps that prevent
     * control transformation to be applied to the shadow effect
     * (which makes it looks as a real shadow)
     */
    public static Node createMaterialNode(Node control, int level) {
        Node container = new Pane(control){
            @Override
            protected double computeMaxWidth(double height) {
                return computePrefWidth(height);
            }

            @Override
            protected double computeMaxHeight(double width) {
                return computePrefHeight(width);
            }

            @Override
            protected double computePrefWidth(double height) {
                return control.prefWidth(height);
            }

            @Override
            protected double computePrefHeight(double width) {
                return control.prefHeight(width);
            }
        };
        container.getStyleClass().add("depth-container");
        container.setPickOnBounds(false);
        level = Math.max(level, 0);
        level = Math.min(level, 5);
        container.setEffect(new DropShadow(BlurType.GAUSSIAN,
                depth[level].getColor(),
                depth[level].getRadius(),
                depth[level].getSpread(),
                depth[level].getOffsetX(),
                depth[level].getOffsetY()));
        return container;
    }

    public static Node createMaterialNode(Node control, Color color, int level) {
        Node container = new Pane(control){
            @Override
            protected double computeMaxWidth(double height) {
                return computePrefWidth(height);
            }

            @Override
            protected double computeMaxHeight(double width) {
                return computePrefHeight(width);
            }

            @Override
            protected double computePrefWidth(double height) {
                return control.prefWidth(height);
            }

            @Override
            protected double computePrefHeight(double width) {
                return control.prefHeight(width);
            }
        };
        container.setPickOnBounds(false);
        level = Math.max(level, 0);
        level = Math.min(level, 5);
        container.getStyleClass().add("depth-container");
        container.setEffect(new DropShadow(BlurType.GAUSSIAN,
                shadows(color)[level].getColor(),
                shadows(color)[level].getRadius(),
                shadows(color)[level].getSpread(),
                shadows(color)[level].getOffsetX(),
                shadows(color)[level].getOffsetY()));
        return container;
    }
}
