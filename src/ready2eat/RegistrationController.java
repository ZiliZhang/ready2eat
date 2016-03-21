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
 * @author dkim63
 */
public class RegistrationController implements Initializable {

    @FXML
    private Button register;
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private TextField phone;
    @FXML
    private TextField address;
    @FXML
    private TextField card;
    @FXML
    private TextField expiry;
    @FXML
    private PasswordField pwd;
    @FXML
    private TextField cvv;
    @FXML
    private Label label;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private Label label5;
    @FXML
    private Label label6;
    @FXML
    private Label label61;
    @FXML
    private Button exit;
    @FXML
    private Label registation_confirm; 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(!Ready2eat.succ){
            //System.out.println(Ready2eat.succ);
            registation_confirm.setText("Registration Failure due to invailad inputs");
        }
    }

    @FXML
    private void registerUser(ActionEvent event) {
        Connection con;
        Statement statement;
        try {
            con = new Ready2eat().getConnection();
            statement = con.createStatement();
            String selectSQL = "INSERT INTO users VALUES ('" + email.getText() + "','" +
                    username.getText() + "','" + pwd.getText() + "'," + 
                    phone.getText() + ",'" + address.getText() + "'," +
                    card.getText() + "," + expiry.getText() + "," +
                    cvv.getText() + ")";
            System.out.println(selectSQL);
            ResultSet rs = statement.executeQuery(selectSQL);
            register.setText("Welcome...");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException: " + ex.getClass());
        }
        catch (SQLException ex) {
            int sqlCode = ex.getErrorCode();
            String sqlState = ex.getSQLState();
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            if(!sqlState.equals("02000")){
                Ready2eat.succ = false;
            }
            else Ready2eat.succ = true;
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        try {
            
            if(Ready2eat.succ){
                    root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            }else{
                    root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
            }
        } catch (IOException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
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