package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public class loginPageController implements Serializable {

    @FXML
    TextField  login;
    @FXML
    Button loginButton, signUp;
    @FXML
    PasswordField pass;


    public void login(ActionEvent actionEvent) throws IOException, SQLException {
        databaseActions da = new databaseActions();
        if (da.logIn(login.getText(), pass.getText()).contains("succ"))
        {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
                    FXMLLoader load = new FXMLLoader(getClass().getResource("projectStats.fxml"));
            Parent root = load.load();

            projectStatsController con = load.getController();
            con.setLoggedIn(login.getText());

            Scene scene = new Scene(root);
            Stage newStage = new Stage();

            newStage.setTitle("Projects statistics");
            newStage.setScene(scene);
            newStage.show();
        }

    }
        public void signUp (ActionEvent actionEvent) throws IOException {
            FXMLLoader load = new FXMLLoader(getClass().getResource("signIn.fxml"));
            Parent root = load.load();

            Scene scene = new Scene(root);
            Stage newStage = new Stage();

            newStage.setTitle("Sign Up");
            newStage.setScene(scene);
            newStage.show();
    }
}
