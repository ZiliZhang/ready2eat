/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ready2eat;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
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
    @FXML
    private PasswordField pwd;
    @FXML
    private Button exit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void loginButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        Button clicked = (Button) event.getSource();
        ResourceBundle rb = new ResourceBundle(){
                @Override
                protected Object handleGetObject(String key) {
                    return email.getText();
                }
                @Override
                public Enumeration<String> getKeys() {
                    return null;
                }               
            };
        if (clicked.getId().equals("login")) {
            Connection con;
            Statement statement;
            try {
                con = new Ready2eat().getConnection();
                statement = con.createStatement();
                String selectSQL = "SELECT * FROM users WHERE email='" + email.getText() + 
                       "' AND pwd='" + pwd.getText() + "'";
                //System.out.println(selectSQL);
                ResultSet rs = statement.executeQuery(selectSQL);
                if (rs.next()) {
                    root = FXMLLoader.load(getClass().getResource("Restaurant_list.fxml"), rb);
                }
                else {
                    email.setText("Wrong credentials! Try again.");
                    return;
                }
                con.close();
            }
            catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFoundException: " + ex.getClass());
            } 
            catch (SQLException ex) {
                int sqlCode = ex.getErrorCode();
                String sqlState = ex.getSQLState();
                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            }
        }
        else root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
    }
    
    @FXML
    private void exit(MouseEvent event) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    
}
