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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zilizhang
 */
public class ConfirmController implements Initializable {

    @FXML
    private Label label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection con;
        Statement statement;
        try {
            con = new Ready2eat().getConnection();
            statement = con.createStatement();
            ResultSet rs = (ResultSet) rb.getObject("");
            Float total = (Float) rb.getObject("total");
            String resto = (String) rb.getObject("resto");
            String selectSQL = "INSERT INTO payments VALUES (default, '" + 
                    rs.getString(1) + "', default, '',NULL," + 
                    ((boolean) rb.getObject("istakeout")) + 
                    "," + total + ")";
            System.out.println(selectSQL);
            statement = con.prepareStatement(resto, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate(selectSQL);
            int transNum = statement.getGeneratedKeys().getInt(1);
            HashMap<String, Integer> orderMap = (HashMap) rb.getObject("orderMap");
            Iterator it = orderMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                selectSQL = "INSERT INTO orderItems VALUES (" + transNum + ", '" + 
                    resto + "', '" + pair.getKey() + "', " + 
                        pair.getValue() + ")";
                System.out.println(selectSQL);
                statement.executeQuery(selectSQL);
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Ready2eat.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException e){
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }
    }  
    
    public void backToFront(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("Restaurant_list.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
    }
}
