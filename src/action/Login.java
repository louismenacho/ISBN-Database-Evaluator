package action;

import gui.LoginUI;
import gui.TableUI;
import paths.InFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *Class defines an action for logging in
 */
public class Login implements ActionListener {

    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private Connection connection;

    private String DB_URL;
    private String USER;
    private String PASS;

    private LoginUI loginUI;

    public Login(LoginUI loginUI) {
        this.loginUI = loginUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Get username and password from filled in fields and construct database url
        USER = loginUI.getUserField().getText();
        PASS = new String(loginUI.getPassField().getPassword());
        DB_URL = "jdbc:mysql://localhost:3306/" + USER +"?useSSL=false";

        try {
            //Open a connection
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = connection.createStatement();

            //Create table for books
            stmt.execute("CREATE TABLE IF NOT EXISTS books(" +
                    "isbn VARCHAR (10) NOT NULL , " +
                    "title VARCHAR(100) NOT NULL, " +
                    "author VARCHAR(100) NOT NULL, " +
                    "publisher VARCHAR(100) NOT NULL, " +
                    "year_published VARCHAR(4) , " +
                    "price DECIMAL(5,2), " +
                    "quantity INT, " +
                    "image_link VARCHAR (150), " +
                    "PRIMARY KEY (isbn))"
            );

            //Create table for history records/entries of books
            stmt.execute("CREATE TABLE IF NOT EXISTS records(" +
                    "id INT (3) ZEROFILL NOT NULL AUTO_INCREMENT, " +
                    "isbn VARCHAR(20) NOT NULL, " +
                    "title VARCHAR(100) NOT NULL, " +
                    "date_entered VARCHAR(20) NOT NULL, " +
                    "PRIMARY KEY (id))"
            );

            //Allow for auto incrementation of table
            stmt.execute("ALTER TABLE records AUTO_INCREMENT=1");

            //Connect to amazon
            URLConnection oConnection = (new URL("https://www.amazon.com/")).openConnection();

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ClassNotFoundException" );
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Access denied." );
            return;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //Login successful
        loginUI.dispose();

        //Open main interface and pass user info
        TableUI tableUI = new TableUI();
        tableUI.pass(USER,connection);

        //If args[0] specified then run New Database command
        if(InFile.fileSpecified) {
            tableUI.getNewMenuItem().doClick();
        }
    }
}
