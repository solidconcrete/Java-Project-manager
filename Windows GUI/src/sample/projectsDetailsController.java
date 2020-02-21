package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class projectsDetailsController implements Initializable {
String loggedIn;

@FXML
    TableView activeProjectTable;
@FXML
    Text selectedProjectText;
@FXML
    TextField addUserLogin;
@FXML
    Button addUserButton, completeProjectPutton;

    public void setLoggedIn(String loggedIn) throws SQLException {
        this.loggedIn = loggedIn;
        populate_tables();
    }
    public void getSelectedRow(javafx.scene.input.MouseEvent mouseEvent)
    {
        project p = (project) activeProjectTable.getSelectionModel().getSelectedItem();
        System.out.println(p.getTitle());
        selectedProjectText.setText(p.getTitle());
    }

    public void addUser(ActionEvent actionEvent)
    {
        databaseActions da = new databaseActions();
        int projectId = da.getProjectId(selectedProjectText.getText());
        da.addPersonToProject(projectId, addUserLogin.getText());
    }

    public void completeProject (ActionEvent actionEvent)
    {
        databaseActions da = new databaseActions();
        da.completeProject(selectedProjectText.getText());
    }

    public void deleteProject (ActionEvent actionEvent)
    {
        databaseActions da = new databaseActions();
        da.removeProject(selectedProjectText.getText());
        populate_tables();
    }

    public void populate_tables ()
    {
        databaseActions da = new databaseActions();
        ArrayList<String> projects = da.getProjectNames(loggedIn);
        System.out.println(projects);
        activeProjectTable.getItems().clear();
        ArrayList<project> projectList = new ArrayList<>();
        for(String p : projects) {
            project pr = new project();
            pr.setTitle(p);
            projectList.add(pr);
        }
            activeProjectTable.getItems().clear();
            activeProjectTable.getItems().addAll(projectList);
//            completedProjectsTable.getItems().addAll(projectList);
    }
    @Override
    public void initialize (URL url, ResourceBundle rb)
    {
        TableColumn<String, project> column1 = new TableColumn<>("Title");
        column1.setCellValueFactory(new PropertyValueFactory<>("title"));

        activeProjectTable.getColumns().clear();
        activeProjectTable.getColumns().add(column1);
//        completedProjectsTable.getColumns().add(column1);
    }

}
