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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zilizhang
 */
public class DishController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private ListView<String> dish_list;

    String user_email = "";
    
    ObservableList<String> orders = FXCollections.observableArrayList(new ArrayList<String>());
    @FXML
    private ListView<String> order_list = new ListView<String>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user_email = (String) rb.getObject("");
        String resto = (String) rb.getObject("currentItem");
        int i = resto.indexOf("------");
        String resto_name = resto.substring(0, i);
        Connection con;
        Statement statement;
        try {
            con = new Ready2eat().getConnection();
            statement = con.createStatement();
            String selectSQL = "SELECT * FROM dishes WHERE rName='" + resto_name + "'";
            System.out.println(selectSQL);
            ResultSet rs = statement.executeQuery(selectSQL);
            ArrayList<String> dishes = new ArrayList<>();
            while (rs.next()) {
                String s1 = rs.getString(1) + "\t" + rs.getString(4);
                dishes.add(s1);
            }
            ObservableList<String> dishlist = FXCollections.observableArrayList(dishes);
            dish_list.setItems(dishlist);
            con.close();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(DishController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) {
            int sqlCode = ex.getErrorCode();
            String sqlState = ex.getSQLState();
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }
   }
    
    @FXML
    public void addToOrder(MouseEvent event) throws IOException {
        if (event.getClickCount() >= 2) {
           String currentItemSelected = (String) dish_list.getSelectionModel()
                                                    .getSelectedItem();
            orders.add(currentItemSelected);
            order_list.setItems(orders);
        }
    }

    @FXML
    private void payAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        ResourceBundle rb = new ResourceBundle(){
            @Override
            protected Object handleGetObject(String key) {
                System.out.println("HIfrthjtgdjhtrhygjh");
                if (key.equals("orders")) return orders;
                else return user_email;
            }
            @Override
            public Enumeration<String> getKeys() {
                return null;
            }               
        };
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment.fxml"), rb);
        root = loader.load();     
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
    }
    
}
