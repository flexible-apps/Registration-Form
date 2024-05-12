package com.flexible.authentications.success;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import jfoenix.controls.JFXDialog;

import java.net.URL;
import java.util.ResourceBundle;

public class SuccessController implements Initializable {
    private final JFXDialog dialog;

    @FXML
    protected MFXButton btnCloser;

    @FXML
    protected AnchorPane root;

    @FXML
    protected Text title;


    @FXML
    protected Text message;

    @FXML
    protected ImageView iv;

    public SuccessController(JFXDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
