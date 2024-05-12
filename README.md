<h1 align="center">
    Flexible Apps
</h1>

___

<p align="center">
<sup>
<b>Flexible Apps is an educational website to explain the javaFX programming language and open source library.</b>
</sup>
</p>

# What is this project ?

* It is simple model for using the above custom controller
* You can take a look at this link: https://github.com/flexible-apps/Progress-Indicator

# Overview

Detailed images about this application.

* Registration image
    ![Registration.png](src%2Fcom%2Fflexible%2Fresource%2Fpic%2Fexamples%2FRegistration.png)

* Login image
    ![Login.png](src%2Fcom%2Fflexible%2Fresource%2Fpic%2Fexamples%2FLogin.png)

# Used libraries

* [JFoenix](https://github.com/sshahine/JFoenix)
* [MaterialFX](https://github.com/palexdev/MaterialFX)

# Used custom controller

* MFAFloatingTextField: Basic example for how we can use MFAFloatingTextField ?

``` Java
public class PasswordFieldController implements Initializable {

    @FXML
    @RegularExpiration(pattern = "^[A-Z][a-zA-Z0-9 ]{2,}$")
    private MFAFloatingTextField fieldUsername;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldUsername.setPatternObservableValueListener(fieldUsername::textProperty,
                text -> text.matches(regularExpiration.pattern()) && !text.isEmpty(),
                text -> {
                    // put your code hire...
        });
    }
}

```

* MFAFloatingPasswordField: Basic example for how we can use MFAFloatingPasswordField ?

``` Java

public class TextFieldController implements Initializable {

    @FXML
    @RegularExpiration(pattern = "^[A-Z][a-zA-Z0-9@,;!:?*=)('\"^~&\\[\\]\\\\/ ]{7,}")
    private MFAFloatingPasswordField fieldPassword;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldPassword.setPatternObservableValueListener(fieldPassword::textProperty,
                text -> text.matches(regularExpiration.pattern()) && !text.isEmpty(),
                text -> {
                    // put your code hire...
        });
    }
}
```

# Special Thanks

Special thanks to <a href="https://www.jetbrains.com">JetBrains</a> for their support to this project