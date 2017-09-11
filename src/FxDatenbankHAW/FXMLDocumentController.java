package FxDatenbankHAW;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 *
 * @author Uwe Krause
 */
public class FXMLDocumentController {

    @FXML
    private MenuItem menu_ueber;

    @FXML
    private Button login_button;

    @FXML
    private TextField login_benutzername;

    @FXML
    private PasswordField login_passwort;

    @FXML
    private MenuItem menu_quit;

    @FXML
    private Label rightStatus;

    @FXML
    private ListView ListView;

    private Statement verbinde(String user, String passwort) throws SQLException {

        // Verbindung zur Datenbank herstellen
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@ora14.informatik.haw-hamburg.de:1521:inf14", user, passwort);

        return con.createStatement();

    }

    private void fuelle_listview() {

        try {
            /**
             * Textfelder auslesen
             */
            String user = login_benutzername.getText().trim();
            String passwort = login_passwort.getText().trim();

            if (!"".equals(user) && !"".equals(passwort)) {

                Statement stmt = verbinde(user, passwort);

                ResultSet rset = stmt.executeQuery("Select table_name from user_tables");

                setze_status(1);

                /**
                 * ResultSetMetaData rsetmd = rset.getMetaData();
                 * System.out.println(rsetmd.getColumnName(1));
                 */
                ObservableList<String> items = FXCollections.observableArrayList();

                while (rset.next()) {
                    // System.out.println(rset.getString(1));
                    items.add(rset.getString(1));
                }

                ListView.setItems(items);
            }
        } catch (SQLException ex) {
            // Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            rightStatus.setTextFill(Color.web("red"));
            rightStatus.setText("Verbindungsfehler");

            ListView.setItems(null);

            System.err.println("SQl-Fehler! Verbindung okay? Benutzername & Passwort?");
        }
    }

    @FXML
    public void table_clickedsomewhere() {

        Object clicked_on = ListView.getSelectionModel().getSelectedItem();

        //System.out.println("clicked on " + clicked_on);
        fuelle_listview();

        //if (clicked_on != null) {
        fuelle_tableview(clicked_on);
        //}

    }

    @FXML
    private MenuItem verbinden;

    @FXML
    private Label label_tableview;

    @FXML
    private MenuItem menu_disconnect;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    private void setze_status(Integer code) {

        switch (code) {

            case 0:
                rightStatus.setTextFill(Color.web("red"));
                rightStatus.setText("Verbindungsfehler");

                label_tableview.setText("Inhalt");
                break;
            case 1:
                rightStatus.setTextFill(Color.web("#C0C0C0"));
                rightStatus.setText("Verbindung hergestellt");
                break;

            default:
                rightStatus.setTextFill(Color.web("#C0C0C0"));
                rightStatus.setText("status");

        }

    }

    //TABLE VIEW AND DATA
    @FXML
    private TableView TableView;
    private ObservableList<ObservableList> data;

    private void fuelle_tableview(Object table_object) {
        // System.out.println(table_object);
        String table_name = (String) table_object;

        TableView.getColumns().clear();
        label_tableview.setText(table_name);

        data = FXCollections.observableArrayList();

        try {
            /**
             * Textfelder auslesen
             */
            String user = login_benutzername.getText().trim();
            String passwort = login_passwort.getText().trim();

            if (!"".equals(user) && !"".equals(passwort) && table_object != null) {

                Statement stmt = verbinde(user, passwort);

                ResultSet rs = stmt.executeQuery("Select * from " + table_name);

                /**
                 * ****************************
                 * http://blog.ngopal.com.np/2011/10/19/dyanmic-tableview-data-from-database/comment-page-1/
                 * ****************************
                 */
                /**
                 * TABLE COLUMN ADDED DYNAMICALLY
                 */
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {

                    //We are using non property style for making dynamic table
                    final int j = i;

                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                        @Override
                        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {

                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }

                    });

                    TableView.getColumns().addAll(col);

                    //System.out.println("Column [" + i + "] ");
                }

                /**
                 * Data added to ObservableList
                 */
                while (rs.next()) {
                    //Iterate Row

                    ObservableList<String> row = FXCollections.observableArrayList();

                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                        //Iterate Column
                        row.add(rs.getString(i));

                    }

                    //System.out.println("Row [1] added " + row);
                    data.add(row);

                }

                //FINALLY ADDED TO TableView
                TableView.setItems(data);
                /**
                 * ****************************
                 */

            }

        } catch (SQLException ex) {
            // Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            setze_status(0);

            ListView.setItems(null);

            System.err.println("SQl-Fehler! Verbindung okay? Benutzername & Passwort?");
        }

    }

    @FXML
    void button_check_logindata(ActionEvent event) {
        fuelle_listview();
    }

    @FXML
    void menu_connect(ActionEvent event) throws SQLException {
        System.out.println("Verbinde");
    }

    @FXML
    void menu_disconnect(ActionEvent event) {
        System.out.println("trenne Verbindung");
    }

    @FXML
    void menu_quit(ActionEvent event) {
        System.out.println("Schließe Programm");

        System.exit(0);
    }

    @FXML
    void menu_ueber(ActionEvent event) {
        System.out.println("Zeige Über-Fenster");
    }

}
