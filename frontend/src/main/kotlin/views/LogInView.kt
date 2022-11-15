package views

import TODOApplication
import client.TODOClient
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import models.User
import java.io.IOException

class LogInView(private val todoApplication: TODOApplication) : VBox() {
    private val todoClient = TODOClient

    init {
        val welcomeLabel = Label("Welcome to TODOs!")

        val usernameContainer = HBox()
        val usernameLabel = Label("Username:")
        val usernameTextField = TextField()
        usernameContainer.children.addAll(usernameLabel, usernameTextField)

        val passwordContainer = HBox()
        val passwordLabel = Label("Password:")
        val passwordTextField = TextField()
        passwordContainer.children.addAll(passwordLabel, passwordTextField)

        val buttonsContainer = HBox()
        val registerButton = Button("Register")
        registerButton.setOnAction {
            handleButtonOperation(User(usernameTextField.text, passwordTextField.text), todoClient::registerUser)
            Alert(Alert.AlertType.CONFIRMATION, "User successfully created").show()
        }
        val logInButton = Button("Log in")
        logInButton.setOnAction {
            handleButtonOperation(
                User(usernameTextField.text, passwordTextField.text)
            ) { user ->
                todoClient.logInUser(user)
                todoApplication.setMainView()
            }
        }
        buttonsContainer.children.addAll(registerButton, logInButton)

        children.addAll(welcomeLabel, usernameContainer, passwordContainer, buttonsContainer)
    }

    private fun handleButtonOperation(user: User, operation: (User) -> Unit) {
        if (user.username.isBlank() || user.username.isEmpty() || user.password.isBlank() || user.password.isEmpty()) {
            Alert(Alert.AlertType.ERROR, "Username and password cannot be empty").show()
            return
        }
        try {
            operation(user)
        } catch (ignore: IOException) {
            Alert(Alert.AlertType.ERROR, "Username and password invalid.").show()
            return
        }
    }
}
