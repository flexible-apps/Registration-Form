package com.flexible.authentications;

import javafx.scene.layout.AnchorPane;

public abstract class ClientAuthentication {
    protected AnchorPane father;

    public ClientAuthentication(AnchorPane father) {
        this.father = father;
    }

    public AnchorPane getFather() {
        return father;
    }

    public void setFather(AnchorPane father) {
        this.father = father;
    }
}
