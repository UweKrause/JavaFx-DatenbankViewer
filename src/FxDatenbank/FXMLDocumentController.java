package FxDatenbank;

import java.sql.SQLException;
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
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import javafx.beans.property.ReadOnlyObjectWrapper;

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

    private Statement verbinde(String db_user, String db_password) throws SQLException {

        // Connect to database
        String hostName = "uwe.database.windows.net";
        String dbName = "javafx";

        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, db_user, db_password);

        Connection connection = DriverManager.getConnection(url);

        return connection.createStatement();

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

                ResultSet rset = stmt.executeQuery("SELECT * FROM sysobjects WHERE xtype='U'");

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
        } catch (SQLException e) {
            // Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            rightStatus.setTextFill(Color.web("red"));
            rightStatus.setText("Verbindungsfehler");

            ListView.setItems(null);

            System.err.println("SQl-Fehler! Verbindung okay? Benutzername & Passwort?");
            e.printStackTrace();
        }
    }

    @FXML
    public void table_clickedsomewhere() {

        Object clicked_on = ListView.getSelectionModel().getSelectedItem();

        System.out.println("clicked on " + clicked_on);

        // Tabellenliste wird aktualisiert
        fuelle_listview();

        // Die Tabelle wird angezeigt
        if (clicked_on != null) {
            fuelle_tableview(clicked_on);
        }

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

    private void fuelle_tableview(Object table_object) {
        // System.out.println(table_object);
        String table_name = (String) table_object;

        //tableview.getColumns().clear();
        label_tableview.setText(table_name);

        try {
            /**
             * Textfelder auslesen
             */
            String user = login_benutzername.getText().trim();
            String passwort = login_passwort.getText().trim();

            // Tabelle leeren, falls vorher was drin war
            TableView.getColumns().clear();
            TableView.getItems().clear();

            if (!"".equals(user) && !"".equals(passwort) && table_object != null) {

                Statement stmt = verbinde(user, passwort);

                ResultSet rs = stmt.executeQuery("Select * from [SalesLT].[" + table_name + "]");

                int rs_ColumnCount = rs.getMetaData().getColumnCount();

                for (int i = 0; i < rs_ColumnCount; i++) {
                    // muss fuer lambda final sein
                    final int finalI = i;

                    String columnName = rs.getMetaData().getColumnName(i + 1);

                    /**
                     * CellValueFactory basiert auf Codebeispiel
                     * https://stackoverflow.com/questions/37559584/how-to-add-dynamic-columns-and-rows-to-tableview-in-java-fxml
                     */
                    TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnName);
                    column.setCellValueFactory((CellDataFeatures<ObservableList<String>, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalI)));

                    TableView.getColumns().add(column);

                    //System.out.println("Column [" + i + "]: " + columnName);
                }

                // ResultSet durchlaufen
                while (rs.next()) {

                    // aktuelle Zeile wird vorbereitet
                    ObservableList<String> row = FXCollections.observableArrayList();

                    // Spalte des ResultSet durchlaufen
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        // der Zeile die Zellen hinzufuegen
                        row.add(rs.getString(i));
                    }

                    // die Zeile der Tabelle hinzufuegen
                    TableView.getItems().add(row);

                    //System.out.println("Row [" + rs.getRow() + "] added " + row);
                }

            }

        } catch (SQLException e) {
            // Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            setze_status(0);

            ListView.setItems(null);

            System.err.println("SQl-Fehler!");
            e.printStackTrace();
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
