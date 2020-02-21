package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
//        databaseActions da = new databaseActions();
//        System.out.println(da.getProjectWorkers(1));
//        da.setLoggedIn("company1");
//        da.countActiveFinishedProjects();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
