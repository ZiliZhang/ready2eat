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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zilizhang
 */
public class ConfirmController implements Initializable {

    @FXML
    private Label label;
    String user_email;
    @FXML
    private Button exit;
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
            String selectSQL = "INSERT INTO payments VALUES (default, '" + 
                    user_email + "', default, '',NULL," + 
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
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey());
                selectSQL = "INSERT INTO orderItems VALUES (" + transNum + ", '" + 
                    resto + "', '" + pair.getKey() + "', " + 
                        pair.getValue() + ")";
                System.out.println(selectSQL);
                System.out.println(pair.getKey());
                Statement statement2 = con.createStatement();
                statement2.executeQuery(selectSQL);
            }
            if ((boolean) rb.getObject("istakeout")) {
                String currentItemSelected = (String) rb.getObject("booking");
                Integer table_num = Integer.parseInt(currentItemSelected.substring(14, currentItemSelected.indexOf(',')));
                String selectSQL2 = "UPDATE timeslots SET available=false WHERE rname='" + resto + "' AND tablenum=" + table_num;
                System.out.println(selectSQL);
                ResultSet rs2 = statement.executeQuery(selectSQL2);
            }
            
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
    public void backToFront(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
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
        root = FXMLLoader.load(getClass().getResource("Restaurant_list.fxml"), rb);
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
