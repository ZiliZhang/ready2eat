/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ready2eat;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class ConfirmController implements Initializable {

    @FXML
    private Label label;
    String user_email;
    String time_slot;
    Integer table_num;
    @FXML
    private Button exit;
    @FXML
    private Label table_confirm;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection con;
        PreparedStatement statement;
        try {
            con = new Ready2eat().getConnection();
            //statement = con.createStatement();
            user_email = (String) rb.getObject("");
            Float total = (Float) rb.getObject("total");
            String resto = (String) rb.getObject("resto");
            String comments = (String) rb.getObject("comments");
            String selectSQL = "INSERT INTO payments VALUES (default, '" + 
                    user_email + "', default, '" + comments + "',NULL," + 
                    ((boolean) rb.getObject("istakeout")) + 
                    "," + total + ")";
            System.out.println(selectSQL);
            statement = con.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            //statement.executeQuery(selectSQL);
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int transNum = rs.getInt(1);
            HashMap<String, Integer> orderMap = (HashMap) rb.getObject("orderMap");
            Iterator it = orderMap.entrySet().iterator();
            for (Map.Entry<String, Integer> pair : orderMap.entrySet()) {
                selectSQL = "INSERT INTO orderItems VALUES (" + transNum + ", '" + 
                    resto + "', '" + pair.getKey() + "', " + 
                        pair.getValue() + ")";
                System.out.println(selectSQL);
                Statement statement2 = con.createStatement();
                try {
                    statement2.executeQuery(selectSQL);
                }
                catch (SQLException e){
                    int sqlCode = e.getErrorCode();
                    String sqlState = e.getSQLState();
                    System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
            }
            if ((boolean) rb.getObject("istakeout")) {
                table_num = (Integer) rb.getObject("table_num");
                System.out.println(table_num);
                time_slot = (String) rb.getObject("time_slot");
                System.out.println(time_slot);
                String selectSQL2 = "UPDATE timeslots SET availability=false WHERE rname='" + resto + "' AND tablenum=" + table_num + " AND slot='" + time_slot + "'";
                System.out.println(selectSQL2);
                PreparedStatement statement2 = con.prepareStatement(selectSQL2);
                statement2.executeUpdate();
            }
            con.close();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException: " + ex.getClass());
        }
        catch (SQLException e){
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }
        
        table_confirm.setText("Your table number is: " + table_num + "\n" +
                            "For Time Slot: " + time_slot);
    }  
    
    @FXML
    public void backToFront(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        Button clicked = (Button) event.getSource();
        ResourceBundle rb = new ResourceBundle(){
                @Override
                protected Object handleGetObject(String key) {
                    return user_email;
                }
                @Override
                public Enumeration<String> getKeys() {
                    return null;
                }               
            };
        if (clicked.getId().equals("back_to_front")){
            root = FXMLLoader.load(getClass().getResource("Restaurant_list.fxml"), rb);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();
        }
        else root = FXMLLoader.load(getClass().getResource("Review.fxml"));
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
