/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ready2eat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Ready2eat extends Application {
    public static boolean succ = true;
    public static boolean isBook = false;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public Connection getConnection() throws ClassNotFoundException, SQLException{       
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        return DriverManager.getConnection (url,"cs421g08", "ready2eat");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException{
        launch(args);
    }
}