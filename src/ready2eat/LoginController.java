/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ready2eat;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zilizhang
 */
public class LoginController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TextField email;
    @FXML
    private Button button;
    @FXML
    private Button button1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // What to do here?
    }    

    @FXML
    private void loginButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        Button clicked = (Button) event.getSource();
        ResourceBundle rb = new ResourceBundle(){
                @Override
                protected Object handleGetObject(String key) {
                    String user_email = email.getText();
                    System.out.println("HIfrthjtgdjhtrhygjh");
                    System.out.println(user_email);
                    return user_email;
                }
                @Override
                public Enumeration<String> getKeys() {
                    return null;
                }               
            };
        if (clicked.getId().equals("login")) {
            root = FXMLLoader.load(getClass().getResource("Restaurant_list.fxml"), rb);
        }
        else {
            root = FXMLLoader.load(getClass().getResource("Registration.fxml"), rb);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
    }
    
}
