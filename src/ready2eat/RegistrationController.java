/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ready2eat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author dkim63
 */
public class RegistrationController implements Initializable {

    @FXML
    private Label email;
    @FXML
    private Label username;
    @FXML
    private Label pwd;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label card;
    @FXML
    private Label expiry;
    @FXML
    private Label cvv;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}