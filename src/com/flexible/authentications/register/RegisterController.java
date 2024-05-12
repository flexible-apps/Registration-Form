package com.flexible.authentications.register;

import com.flexible.annotations.RegularExpiration;
import com.flexible.assets.MFAAssertBuilder;
import com.flexible.assets.MFAFilterBuilder;
import com.flexible.assets.MFAReflections;
import com.flexible.assets.animations.AnimationBuilder;
import com.flexible.authentications.ClientAuthentication;
import com.flexible.authentications.DialogStackPageCreator;
import com.flexible.authentications.PageLoader;
import com.flexible.authentications.db.Accounts;
import com.flexible.authentications.db.ClientCreator;
import com.flexible.authentications.db.Clients;
import com.flexible.authentications.error.ErrorController;
import com.flexible.authentications.success.SuccessController;
import com.flexible.controls.fields.MFAFloatingPasswordField;
import com.flexible.controls.fields.MFAFloatingTextField;
import com.flexible.controls.progress.MFAProgressIndicator;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import jfoenix.controls.JFXDialog;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController extends ClientAuthentication implements Initializable {
    @FXML
    private Text textSingIn;

    @FXML
    private MFXButton btnGoogleRegistration;

    @FXML
    @RegularExpiration(pattern = "^[A-Z][a-zA-Z0-9 ]{2,}$")
    private MFAFloatingTextField fieldUsername;

    @FXML
    @RegularExpiration(pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private MFAFloatingTextField fieldEmail;

    @FXML
    @RegularExpiration(pattern = "^[A-Z][a-zA-Z0-9@,;!:?*=)('\"^~&\\[\\]\\\\/ ]{7,}")
    private MFAFloatingPasswordField fieldPassword;

    @FXML
    private MFXButton btnRegister;

    public RegisterController(AnchorPane father) {
        super(father);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //fields validation
        addValidationListener(fieldUsername, "fieldUsername");
        addValidationListener(fieldEmail, "fieldEmail");
        addValidationListener(fieldPassword, "fieldPassword");

        //add action for register a new account
        btnRegister.setOnAction(event -> {
            // first check if field is empty and is matched
            isValid(fieldUsername, "fieldUsername");
            isValid(fieldEmail, "fieldEmail");
            isValid(fieldPassword, "fieldPassword");

            if (!(fieldUsername.isError()) && !(fieldEmail.isError()) && !(fieldPassword.isError())) {
                // apply register actions
                Region pLoader = PageLoader.get(getFather());

                Task<Boolean> taskResult = new Task<>() {
                    @Override
                    protected Boolean call() {
                        try {
                            // wait just for test
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return getProgressTask(fieldUsername.getText(), fieldEmail.getText(), fieldPassword.getText());
                    }
                };
                taskResult.setOnSucceeded(e -> {
                    getFather().getChildren().removeIf(node -> node.equals(pLoader));
                    boolean result = (boolean) e.getSource().getValue();
                    if (result) {
                        System.out.println("complete registration.");
                        addSuccessWindow();
                    } else {
                        System.err.println("error registration.");
                        addErrorWindow();
                    }
                });

                Thread thread = new Thread(taskResult);
                thread.setDaemon(true);
                thread.start();

                event.consume();
                // add progress task
            } else {
                System.out.println("we have an error");
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
                btnCancel.setOnAction(event -> {
                    // update title and error message
                    title.setText("Error Registration");
                    message.setText("Verify the username and email you entered try again later.");

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

    private static JFXDialog dialog;

    private void addSuccessWindow() {
        StackPane stackPane = DialogStackPageCreator.get(getFather());

        FXMLLoader fxmlSuccessForm = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/flexible/authentications/success/success.fxml")));
        fxmlSuccessForm.setControllerFactory(param -> new SuccessController(dialog) {
            @Override
            public void initialize(URL location, ResourceBundle resources) {
                super.initialize(location, resources);

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

    private boolean isRegister;

    private Boolean getProgressTask(String text, String fieldEmailText, String fieldPasswordText) {
        ClientCreator newClientCreator = ClientCreator.getInstance(text, fieldEmailText, fieldPasswordText);
        Clients.of(Accounts::getClients)
                .stream()
                .filter(client -> client.getUsername().equals(newClientCreator.getUsername()) || client.getEmail().equals(newClientCreator.getEmail()))
                .register((availableClients, isAvailable) -> {
                    if (!isAvailable) {
                        availableClients.add(newClientCreator);
                        isRegister = true;
                        return;
                    }
                    isRegister = false;
                });
        return isRegister;
    }

    private <T> void isValid(T floatingField, String declarationFieldName) {
        String text;
        if (floatingField instanceof MFAFloatingTextField floatingTextField) {
            text = floatingTextField.getText();
            floatingTextField.setError(text.isEmpty() || !text.matches(getAnnotation(declarationFieldName, RegularExpiration.class).pattern()));
        } else if (floatingField instanceof MFAFloatingPasswordField floatingPasswordField) {
            text = floatingPasswordField.getText();
            floatingPasswordField.setError(text.isEmpty() || !text.matches(getAnnotation(declarationFieldName, RegularExpiration.class).pattern()));
        }
    }

    private static <R> R getAnnotation(String declarationFieldName, Class<R> classType) {
        return getAnnotationMapper(declarationFieldName, classType).get();
    }

    @SuppressWarnings("unchecked")
    private static <R> MFAFilterBuilder<R> getAnnotationMapper(String declarationFieldName, Class<R> classType) {
        return MFAReflections.getDeclarationAnnotations(RegisterController.class.getDeclaredFields())
                .ifEqualDeclarationFieldName(declarationFieldName)
                .ifEqualController(classType)
                .toAssertBuilder()
                .filter(MFAAssertBuilder::notNull)
                .map(o -> (R) o);
    }

    private static <T> void addValidationListener(T floatingField, String declarationFieldName) {
        getAnnotationMapper(declarationFieldName, RegularExpiration.class).ifPresentOrElse(regularExpiration -> {
            if (floatingField instanceof MFAFloatingTextField floatingTextField) {
                floatingTextField.setPatternObservableValueListener(floatingTextField::textProperty,
                        text -> text.matches(regularExpiration.pattern()) && !text.isEmpty(),
                        text -> {
//                            System.out.println("Truth value is: " + text);
                        });
            } else if (floatingField instanceof MFAFloatingPasswordField floatingPasswordField)
                floatingPasswordField.setPatternObservableValueListener(floatingPasswordField::textProperty,
                        text -> text.matches(regularExpiration.pattern()) && !text.isEmpty(),
                        text -> {
//                            System.out.println("Truth value is: " + text);
                        });
        }, () -> System.err.println("RegularExpiration maybe equal null."));
    }
}
