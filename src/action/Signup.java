package action;

import gui.LoginUI;
import gui.SignupUI;
import gui.TableUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *Class defines an action that signs up a user, adding them to the database and creating their own space with their own tables
 */
public class Signup implements ActionListener {

    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private Connection connection;

    private String DB_URL;
    private String USER;
    private String PASS;
    private boolean isTesting;

    private SignupUI signupUI;
    private LoginUI loginUI;

    public Signup() {}

    public Signup(SignupUI signupUI) {
        this.signupUI = signupUI;
    }

    public Signup(LoginUI loginUI) {
        this.loginUI = loginUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Cancel")) {
            signupUI.dispose();
            return;
        }

        if(e.getActionCommand().equals("SignupUI")) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new SignupUI();
                }
            });
            return;
        }

        if(e.getActionCommand().equals("Test")) {
            USER = "testuser123";
            PASS = "testuser123";
            isTesting = true;
        }else {
            USER = signupUI.getUserField().getText();
            PASS = new String(signupUI.getPassField().getPassword());
        }

        DB_URL  = "jdbc:mysql://localhost:3306/sys?useSSL=false";

        if(!USER.matches("[a-zA-Z0-9\\\\._\\\\-]{3,}")) {
            JOptionPane.showMessageDialog(null, "Invalid username.[A-Z,a-z,0-9,.,_,-] \nMin. length (3)");
            return;
        }

        if(PASS.length() < 6) {
            JOptionPane.showMessageDialog(null, "Password too short.\nMin. length (6)");
            return;
        }

        try {

            BufferedReader br = null;

            try {

                br = new BufferedReader(new FileReader("admin.txt"));

            } catch (FileNotFoundException e1) {

                String adminUser = JOptionPane.showInputDialog(null, "Please enter root user for MySQL.");
                if(adminUser == null) {
                    return;
                }

                String adminPass = JOptionPane.showInputDialog(null, "Please enter root password for MySQL.");
                if(adminPass == null) {
                    return;
                }

                storeCredentials(adminUser, adminPass);
                br = new BufferedReader(new FileReader("admin.txt"));
            }


            ArrayList<String> adminCredentials = new ArrayList<>();

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String credential = sCurrentLine.substring(9);
                adminCredentials.add(credential);
            }

            String adminUser = adminCredentials.get(0);
            String adminPass = adminCredentials.get(1);

            //Open a connection
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, adminUser, adminPass);

            if (!createUser() && !isTesting) {
                JOptionPane.showMessageDialog(null, "User already exists.");
                return;
            }

            //If Quick Test pressed then automatically fill fields and click login button
            if (isTesting) {
                loginUI.getUserField().setText(USER);
                loginUI.getPassField().setText(PASS);
                loginUI.getLoginButton().doClick();
            } else {
                //Sign up Successful
                JOptionPane.showMessageDialog(null, "Signup Successful!");
                signupUI.dispose();
            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Missing MySQL Driver" );
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid Root Credentials");

            String adminUser = JOptionPane.showInputDialog(null, "Please enter root user for MySQL.");
            if(adminUser == null) {
                return;
            }

            String adminPass = JOptionPane.showInputDialog(null, "Please enter root password for MySQL.");
            if(adminPass == null) {
                return;
            }

            storeCredentials(adminUser,adminPass);
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //Creates user in database
    public boolean createUser() {

        try {
            Statement stmt = connection.createStatement();

            //Check if user exists
            ResultSet rs = stmt.executeQuery("SELECT User FROM mysql.user");
            while (rs.next()) {
                if (rs.getString(1).equals(USER)) {
                    return false;
                }
            }

            //Create user space
            stmt.executeUpdate("CREATE DATABASE " + USER);

            //Create user
            stmt.execute("CREATE USER '" + USER + "'@'%' IDENTIFIED BY '" + PASS + "'");
            stmt.execute("GRANT ALL PRIVILEGES ON " + USER + ".* TO '" + USER + "'@'%' IDENTIFIED BY '" + PASS + "'");
            stmt.execute("GRANT ALL ON " + USER + ".* TO '" + USER + "'@'%'");
            stmt.execute("FLUSH PRIVILEGES");

            //Finished creating user
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQLException");
            e.printStackTrace();
        }
        return true;
    }

    //Stores database admin credentials in admin.txt file
    private void storeCredentials(String adminUser, String adminPass) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("admin.txt"));
            writer.write("rootUser:" + adminUser);
            writer.newLine();
            writer.write("rootPass:" + adminPass);
            writer.flush();
            writer.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }

}
