package action;

import gui.LoginUI;
import gui.TableUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *Class defines an action that logs user out
 */
public class Logout implements ActionListener {

    private TableUI tableUI;
    private  Connection connection;

    public Logout(TableUI tableUI, Connection connection) {
        this.tableUI = tableUI;
        this.connection = connection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Logging out, closing connection
        try {
            connection.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"Logged out successfully");
        tableUI.dispose();
        new LoginUI();
    }
}
