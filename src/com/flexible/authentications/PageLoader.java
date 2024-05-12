package com.flexible.authentications;

import com.flexible.assets.animations.AnimationBuilder;
import com.flexible.controls.progress.MFAProgressIndicator;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class PageLoader {

    public static Region get(AnchorPane father) {
        ObservableList<Node> fatherChildren = father.getChildren();

        MFAProgressIndicator indicator = new MFAProgressIndicator();
        indicator.setPrefSize(15, 15);
        indicator.setMaxSize(15, 15);

        StackPane pageLoader = new StackPane(indicator) {
            @Override
            public String getUserAgentStylesheet() {
                return father.getUserAgentStylesheet();
            }
        };
        pageLoader.setAlignment(Pos.CENTER);
        pageLoader.getStyleClass().add("progress-page-loader");
        pageLoader.setMinSize(father.getPrefWidth(), father.getPrefHeight());
        pageLoader.setPrefSize(father.getPrefWidth(), father.getPrefHeight());
        pageLoader.setMaxSize(father.getPrefWidth(), father.getPrefHeight());

        fatherChildren.add(fatherChildren.size(), pageLoader);

        // add animation for stack pane
        AnimationBuilder.opacity(pageLoader);
        return pageLoader;
    }
}
