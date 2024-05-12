package com.flexible.authentications;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

public class DialogStackPageCreator {
    public static StackPane get(AnchorPane father) {
        StackPane stackPane = new StackPane() {
            @Override
            public String getUserAgentStylesheet() {
                return father.getUserAgentStylesheet();
            }
        };
        stackPane.setMinSize(father.getPrefWidth(), father.getPrefHeight());
        stackPane.setMaxSize(father.getPrefWidth(), father.getPrefHeight());
        father.getChildren().add(stackPane);
        return stackPane;
    }
}
