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
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class BookController implements Initializable {

    @FXML
    private Button exit;
    @FXML
    private ListView<String> time_list;
    
    ObservableList<String> order = FXCollections.observableArrayList(new ArrayList<String>());
    HashMap<String, Integer> orderMap = new HashMap<>();
    String resto;
    boolean istakeout;
    Float total;
    String user_email;
    String currentItemSelected;
    Integer table_num;
    String time_slot;
    ArrayList<String> timeslots = new ArrayList<>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        order = (ObservableList<String>) rb.getObject("order");
        orderMap = (HashMap) rb.getObject("orderMap");
        resto = (String) rb.getObject("resto");
        istakeout = (boolean) rb.getObject("istakeout");
        total = (Float) rb.getObject("total");
        user_email = (String) rb.getObject("");
        //resto = "Cuisine Szechuan";
        Connection con;
        Statement statement;
        try {
            con = new Ready2eat().getConnection();
            statement = con.createStatement();
            String selectSQL = "SELECT t1.tablenum, t1.slot, t2.maxnumofseats "
                    + "FROM timeslots t1 JOIN tables t2 ON t1.tablenum = t2.tablenum AND t1.rname= t2.rname "
                    + "WHERE t1.rname ='" + resto + "' AND availability=true ORDER BY t1.tablenum, t1.slot";
            //System.out.println(selectSQL);
            ResultSet rs = statement.executeQuery(selectSQL);
            if (!rs.next()) {
                timeslots.add("No time slots available!");
            }
            else {
                do {
                    table_num = rs.getInt("tablenum");
                    time_slot = rs.getString("slot");
                    String s1 = "Table Number: " +rs.getInt("tablenum")+ ", Max Number of Seats: " + rs.getInt("maxnumofseats")+",Time Slot: "+rs.getString("slot");
                    timeslots.add(s1);
                } while (rs.next());
            }
            ObservableList<String> times = FXCollections.observableArrayList(timeslots);
            time_list.setItems(times);
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
    }    

    @FXML
    private void confirmBookAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
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
                           return true;
                       case "table_num":
                           return table_num;
                       case "time_slot":
                           return time_slot;
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

    @FXML
    private void chooseTime(MouseEvent event) {
           currentItemSelected = (String) time_list.getSelectionModel().getSelectedItem();
           //table_num = Integer.parseInt(currentItemSelected.substring(14, currentItemSelected.indexOf(',')));
    }
}