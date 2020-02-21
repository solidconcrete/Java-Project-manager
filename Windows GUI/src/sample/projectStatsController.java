package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public class projectStatsController implements Serializable {

    @FXML
    PieChart projectsPiechart;
    @FXML
    TextField newProjectTextField;
    String loggedIn = null;

    public void setLoggedIn(String loggedIn) throws SQLException {
        this.loggedIn = loggedIn;
        showStats();
    }

    public void showStats() throws SQLException {
        databaseActions da = new databaseActions();
        int[] data = da.countActiveFinishedProjects(loggedIn);
        ObservableList<PieChart.Data> piechartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Active projects", data[1]),
                        new PieChart.Data("Inactive projects", data[0])
                );
        projectsPiechart.setData(piechartData);
    }

    public void createProject(ActionEvent actionEvent) throws SQLException {
        String projectName = newProjectTextField.getText();
        databaseActions da = new databaseActions();
        da.createProject(projectName, loggedIn);
        showStats();
    }

    public void goToProjects(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("projectsDetails.fxml"));
        Parent root = load.load();

        projectsDetailsController con = load.getController();
        con.setLoggedIn(loggedIn);

        Scene scene = new Scene(root);
        Stage newStage = new Stage();

        newStage.setTitle("Projects details");
        newStage.setScene(scene);
        newStage.show();
    }
}
