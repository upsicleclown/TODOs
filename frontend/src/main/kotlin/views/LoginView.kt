package views

import TODOApplication
import commands.LogInUserCommand
import commands.RegisterUserCommand
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import models.User

class LoginView(private val todoApplication: TODOApplication) : VBox(24.0) {
    private val title = Label("nexus")
    private val usernameContainer = HBox(12.0)
    private val usernameLabel = Label("USERNAME")
    private val usernameTextField = TextField()
    private val passwordContainer = HBox(9.0)
    private val passwordLabel = Label("PASSWORD")
    private val passwordTextField = TextField()
    private val buttonsContainer = HBox(64.0)
    private val registerButton = Button("register")
    private val loginButton = Button("login")

    init {
        /* region styling */
        alignment = Pos.CENTER
        title.styleClass.addAll("title-max")

        usernameContainer.alignment = Pos.CENTER
        usernameContainer.styleClass.addAll("username")
        usernameLabel.styleClass.addAll("subtitle")
        usernameTextField.styleClass.addAll("username__text-field", "body")

        passwordContainer.alignment = Pos.CENTER
        passwordContainer.styleClass.addAll("password")
        passwordLabel.styleClass.addAll("subtitle")
        passwordTextField.styleClass.addAll("password__text-field", "body")

        buttonsContainer.alignment = Pos.CENTER
        buttonsContainer.styleClass.addAll("buttons")
        registerButton.styleClass.addAll("buttons__register", "body")
        loginButton.styleClass.addAll("buttons__login", "body")
        /* end region styling */

        /* region event filters */
        registerButton.setOnAction {
            handleButtonOperation(
                User(usernameTextField.text, passwordTextField.text)
            ) { user ->
                todoApplication.commandHandler.execute(RegisterUserCommand(user))
                Alert(Alert.AlertType.CONFIRMATION, "User successfully created").show()
            }
        }

        loginButton.setOnAction {
            handleButtonOperation(
                User(usernameTextField.text, passwordTextField.text)
            ) { user ->
                todoApplication.commandHandler.execute(LogInUserCommand(user))
                todoApplication.setMainView()
            }
        }
        /* end region event filters */

        usernameContainer.children.addAll(usernameLabel, usernameTextField)
        passwordContainer.children.addAll(passwordLabel, passwordTextField)
        buttonsContainer.children.addAll(registerButton, loginButton)
        children.addAll(title, usernameContainer, passwordContainer, buttonsContainer)
    }

    private fun handleButtonOperation(user: User, operation: (User) -> Unit) {
        if (user.username.isBlank() || user.username.isEmpty() || user.password.isBlank() || user.password.isEmpty()) {
            Alert(Alert.AlertType.ERROR, "Username and password cannot be empty").show()
            return
        }
        try {
            operation(user)
        } catch (ignore: IllegalArgumentException) {
            Alert(Alert.AlertType.ERROR, "Username and password invalid.").show()
            return
        }
    }
}
