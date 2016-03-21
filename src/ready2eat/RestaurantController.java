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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class RestaurantController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private ListView resto_list;
    String user_email = "";
    @FXML
    private Button exit;
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
            String selectSQL = "SELECT * FROM restaurants";
            ResultSet rs = statement.executeQuery(selectSQL);
            ArrayList<String> restaurants = new ArrayList<>();
            while (rs.next()) {
                String s1 = rs.getString(1) + "--------" + rs.getString(2);
                restaurants.add(s1);
            }
            ObservableList<String> restos = FXCollections.observableArrayList(restaurants);
            resto_list.setItems(restos);
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
    public void handleMouseClick(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        if (event.getClickCount() >= 2) {
           String currentItemSelected = (String) resto_list.getSelectionModel().getSelectedItem();
           //System.out.println(currentItemSelected);
           ResourceBundle rb = new ResourceBundle(){
               @Override
               protected Object handleGetObject(String key) {
                   if (key.equals("currentItem")) return currentItemSelected;
                   else return user_email;
               }

               @Override
               public Enumeration<String> getKeys() {
                   return null;
               }               
           };
           FXMLLoader loader = new FXMLLoader(getClass().getResource("Dish_list.fxml"), rb);
           root = loader.load();
        }
        else return;
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