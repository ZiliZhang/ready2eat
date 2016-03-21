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
import java.util.HashMap;
import java.util.ResourceBundle;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class PaymentController implements Initializable {

    @FXML
    private Label label;
    
    String user_email = "";
    String user_info = "";
    String comments;
    ObservableList<String> order = FXCollections.observableArrayList(new ArrayList<String>());
    ResultSet rs;
    String resto = "";
    HashMap<String, Integer> orderMap = new HashMap<>();
    @FXML
    private ListView<String> payment = new ListView<>();
    float total = 0;
    @FXML
    private Button exit;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user_email = (String) rb.getObject("");
        orderMap = (HashMap) rb.getObject("orderMap");
        comments = (String) rb. getObject("comments");
        Connection con;
        Statement statement;
        try {
            con = new Ready2eat().getConnection();
            statement = con.createStatement();
            String selectSQL = "SELECT * FROM users WHERE email='" + user_email + "'";
            //System.out.println(selectSQL);
            rs = statement.executeQuery(selectSQL);
            rs.next();
            user_info = "Email: " + user_email + "\n" + 
                               "Username: " + rs.getString(2) + "\n" +
                               "Phone Number: " + rs.getString(4) + "\n" +
                               "Billing Address: " + rs.getString(5) + "\n" +
                               "Card Number (last 4 digits): " + rs.getString(6).substring(rs.getString(6).length()-4, rs.getString(6).length()) + "\n" +
                                "Comments: " + comments;
            con.close();
            rs.close();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException: " + ex.getClass());
        }
        catch (SQLException e){
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }
        resto = (String) rb.getObject("resto");
        order.add(resto);
        order.add(user_info);
        order.addAll((ObservableList<String>) rb.getObject("orders"));
        for (int i = 2; i < order.size(); i++) {
            int priceIndex = order.get(i).indexOf("$");
            total += Float.parseFloat(order.get(i).substring(priceIndex+1, order.get(i).length()));
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
            ResourceBundle rb = new ResourceBundle(){
               @Override
               protected Object handleGetObject(String key) {
                   switch (key) {
                       case "order":
                           return order;
                       case "total":
                           return total;
                       case "comments":
                           return comments;
                       case "resto":
                           return resto;
                       case "istakeout":
                           return true;
                       case "orderMap":
                           return orderMap;
                       default:
                           return user_email;
                   }
               }

               @Override
               public Enumeration<String> getKeys() {
                   return null;
               }               
           };
            root = FXMLLoader.load(getClass().getResource("Booking.fxml"), rb);
        }
        else {
            ResourceBundle rb = new ResourceBundle(){
               @Override
               protected Object handleGetObject(String key) {
                   switch (key) {
                       case "order":
                           return order;
                       case "total":
                           return total;
                       case "resto":
                           return resto;
                       case "istakeout":
                           return false;
                       case "orderMap":
                           return orderMap;
                       default:
                           return user_email;
                   }
               }

               @Override
               public Enumeration<String> getKeys() {
                   return null;
               }               
           };
            root = FXMLLoader.load(getClass().getResource("Confirmation.fxml"), rb);
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
