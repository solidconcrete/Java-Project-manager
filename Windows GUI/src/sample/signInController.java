package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//import java.awt.event.ActionEvent;


//import java.awt.event.ActionEvent;

public class signInController {
    @FXML
    TextField cl, ct, pl, pn, ps;
    @FXML
    PasswordField cp, pp;
    @FXML
    Button cb, pb;

    public void createCompany(ActionEvent actionEvent)
    {
        databaseActions da = new databaseActions();
        da.createUser(cl.getText(), cp.getText(), ct.getText());
    }
    public void createPerson(ActionEvent actionEvent)
    {
        databaseActions da = new databaseActions();
        da.createUser(pl.getText(), pp.getText(), pn.getText(), ps.getText());
    }

}
