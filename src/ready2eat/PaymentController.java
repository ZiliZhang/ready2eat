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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zilizhang
 */
public class PaymentController implements Initializable {

    @FXML
    private Label label;
    
    String user_email = "";
    String user_info = "";
    ObservableList<String> order = FXCollections.observableArrayList(new ArrayList<String>());
    
    @FXML
    private ListView<String> payment = new ListView<String>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user_email = (String) rb.getObject("");
        
        Connection con;
        Statement statement;
        try {
            con = new Ready2eat().getConnection();
            statement = con.createStatement();
            String selectSQL = "SELECT * FROM users WHERE email='" + user_email + "'";
            ResultSet rs = statement.executeQuery(selectSQL);
            rs.first();
            user_info = "Email: " + rs.getString(1) + "\n" + 
                               "Username: " + rs.getString(2) + "\n" +
                               "Phone Number: " + rs.getString(4) + "\n" +
                               "Billing Address: " + rs.getString(5) + "\n" +
                               "Card Number (last 4 digits): " + rs.getString(6).substring(rs.getString(6).length()-4, rs.getString(6).length()) + "\n";
            con.close();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Ready2eat.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException e){
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }
        order.add(user_info);
        order.addAll((ObservableList<String>) rb.getObject("orders"));
        int total = 0;
        for (int i = 0; i < order.size(); i++) {
            System.out.println(order.get(i));
            int priceIndex = order.get(i).indexOf("$");
            System.out.println(priceIndex);
            total += Integer.parseInt(order.get(i).substring(priceIndex, order.get(i).length()));
        }
        order.add("\nTotal: "+total);
        payment.setItems(order);
    }    

    @FXML
    private void bookAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        Button clicked = (Button) event.getSource();
        if (clicked.getId().equals("booking_button")) {
            root = FXMLLoader.load(getClass().getResource("Booking.fxml"));
        }
        else root = FXMLLoader.load(getClass().getResource("Confirmation.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
    }
    
}
