package com.trios.csd214test2b_dianat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                switchToCrud(event);
            } else {
                showAlert("Login Failed", "Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToCrud(ActionEvent event) throws Exception {
        Parent crudParent = FXMLLoader.load(getClass().getResource("crud.fxml"));
        Scene crudScene = new Scene(crudParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(crudScene);
        window.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

