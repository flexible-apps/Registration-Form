package com.flexible.authentications.error;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import jfoenix.controls.JFXDialog;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorController implements Initializable {
    private final JFXDialog dialog;

    @FXML
    protected MFXButton btnCancel;

    @FXML
    protected Text message;

    @FXML
    private AnchorPane root;

    @FXML
    protected Text title;

    @FXML
    protected ImageView iv;

    public ErrorController(JFXDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
