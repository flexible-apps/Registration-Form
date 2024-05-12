package com.flexible.authentications.login;

import com.flexible.authentications.ClientAuthentication;
import com.flexible.authentications.DialogStackPageCreator;
import com.flexible.authentications.PageLoader;
import com.flexible.authentications.db.Accounts;
import com.flexible.authentications.db.ClientCreator;
import com.flexible.authentications.db.Clients;
import com.flexible.authentications.error.ErrorController;
import com.flexible.authentications.success.SuccessController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.flexible.controls.fields.MFAFloatingPasswordField;
import com.flexible.controls.fields.MFAFloatingTextField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import jfoenix.controls.JFXDialog;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class LoginController extends ClientAuthentication implements Initializable {
    private JFXDialog dialog;

    @FXML
    private MFXButton btnLogin;

    @FXML
    private MFAFloatingPasswordField fieldPassword;

    @FXML
    private MFAFloatingTextField fieldUsername;

    @FXML
    private MFXCheckbox remember;

    @FXML
    private Text textForgetPassword;

    @FXML
    private Text textSingIn;

    public LoginController(AnchorPane father) {
        super(father);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> {
            Stream.of(fieldUsername, fieldPassword).forEach(this::isValid);
            if (!(fieldUsername.isError()) && !(fieldPassword.isError())) {
                Region pLoader = PageLoader.get(getFather());

                Task<ResultBlock> taskLogin = new Task<>() {
                    @Override
                    protected ResultBlock call() {
                        return getTaskLogin(fieldUsername.getText(), fieldPassword.getText());
                    }
                };
                taskLogin.setOnSucceeded(e -> {
                    getFather().getChildren().removeIf(node -> node.equals(pLoader));
                    ResultBlock result = (ResultBlock) e.getSource().getValue();
                    if (result.isAvailable()) {
                        System.out.println("complete registration.");
                        addSuccessWindow(result.getClientCreator());
                    } else {
                        System.err.println("error registration.");
                        addErrorWindow();
                    }
                });

                Thread thread = new Thread(taskLogin);
                thread.setDaemon(true);
                thread.start();

                event.consume();
            }
        });
    }

    private void addErrorWindow() {
        StackPane stackPane = DialogStackPageCreator.get(getFather());

        FXMLLoader fxmlErrorForm = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/flexible/authentications/error/error.fxml")));
        fxmlErrorForm.setControllerFactory(param -> new ErrorController(dialog) {
            @Override
            public void initialize(URL location, ResourceBundle resources) {
                super.initialize(location, resources);
                // update image view
                iv.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/flexible/resource/pic/10.png"))));

                // update title and error message
                title.setText("Error Registration");
                message.setText("Verify the username and email you entered try again later.");

                btnCancel.setOnAction(event -> {
                    if (dialog != null) {
                        dialog.setOnDialogClosed(es -> getFather().getChildren().removeIf(node -> node.equals(stackPane)));
                        dialog.close();

                    }
                });
            }
        });
        try {
            AnchorPane successForm = fxmlErrorForm.load();
            dialog = new JFXDialog(stackPane, successForm, JFXDialog.DialogTransition.CENTER);
            dialog.setOverlayClose(false);
            dialog.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addSuccessWindow(ClientCreator clientCreator) {
        StackPane stackPane = DialogStackPageCreator.get(getFather());

        FXMLLoader fxmlSuccessForm = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/flexible/authentications/success/success.fxml")));
        fxmlSuccessForm.setControllerFactory(param -> new SuccessController(dialog) {
            @Override
            public void initialize(URL location, ResourceBundle resources) {
                super.initialize(location, resources);

                // update image view
                iv.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/flexible/resource/pic/322.png"))));

                // update text and message
                title.setText("Welcome " + clientCreator.getEmail());
                message.setText("Your account has been verified. We will transfer you to the application window, Enjoy the experience.");

                Platform.runLater(() -> {
                    dialog.setOverlayClose(true);
                    dialog.setOnDialogClosed(es -> getFather().getChildren().removeIf(node -> node.equals(stackPane)));
                });

                // close this option
                btnCloser.setOnAction(event -> {
                    System.out.println(dialog == null);
                    if (dialog != null) {
                        dialog.setOnDialogClosed(es -> getFather().getChildren().removeIf(node -> node.equals(stackPane)));
                        dialog.close();

                    }
                });
            }
        });
        try {
            AnchorPane successForm = fxmlSuccessForm.load();
            dialog = new JFXDialog(stackPane, successForm, JFXDialog.DialogTransition.CENTER);
            dialog.setOverlayClose(false);
            dialog.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ResultBlock getTaskLogin(String username, String password) {
        AtomicReference<ResultBlock> blockAtomicReference =
                new AtomicReference<>(ResultBlock.ERROR);
        try {
            // wait just for test
            Thread.sleep(1000);
            Clients.of(Accounts::getClients)
                    .stream()
                    .filter(client -> client.getUsername().equals(username) && client.getPassword().equals(password))
                    .ifPresentOrElse(clientCreator -> {
                        blockAtomicReference.set(new ResultBlock(true, clientCreator));
                    }, () -> {
                        blockAtomicReference.set(ResultBlock.ERROR);
                    });

        } catch (InterruptedException e) {
            return blockAtomicReference.get();
        }
        return blockAtomicReference.get();
    }

    private <T> void isValid(T floatingField) {
        String text;
        if (floatingField instanceof MFAFloatingTextField floatingTextField) {
            text = floatingTextField.getText();
            floatingTextField.setError(text.isEmpty());
        } else if (floatingField instanceof MFAFloatingPasswordField floatingPasswordField) {
            text = floatingPasswordField.getText();
            floatingPasswordField.setError(text.isEmpty());
        }
    }

    static class ResultBlock {
        public static final ResultBlock ERROR = new ResultBlock(false, null);
        private boolean isAvailable;
        private ClientCreator clientCreator;

        public ResultBlock(boolean isAvailable, ClientCreator clientCreator) {
            this.isAvailable = isAvailable;
            this.clientCreator = clientCreator;
        }

        public ResultBlock() {
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }

        public ClientCreator getClientCreator() {
            return clientCreator;
        }

        public void setClientCreator(ClientCreator clientCreator) {
            this.clientCreator = clientCreator;
        }
    }

}
