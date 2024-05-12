package com.flexible.assets.animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationBuilder {
    public static void opacity(Node target) {
        Timeline oTime = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, new KeyValue(target.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(170),new KeyValue(target.opacityProperty(), 1)));
        oTime.play();
    }
}
