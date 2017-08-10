package gui;

import javax.swing.*;
import java.awt.*;

/**
 *Class defines the sign up user interface
 */
public class SignupUI extends JFrame {

    //Main panels
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;

    //Fields for username and password
    private JTextField userField;
    private JPasswordField passField;
    private JLabel statusLabel;

    /*
    * Constructor prepares every panel before displaying GUI
    * */
    public SignupUI() {
        prepareFrame();
        prepareHeaderPanel();
        prepareFormPanel();
        prepareButtonPanel();
        showGUI();
    }

    /*
    * Method prepares the frame of the window
    * */
    private void prepareFrame() {

        setTitle("Signup");
        setSize(320,380);
        setResizable(false);
        setLayout(new GridLayout(3,1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        headerPanel = new JPanel();
        formPanel = new JPanel();
        buttonPanel = new JPanel();

        add(headerPanel);
        add(formPanel);
        add(buttonPanel);

    }

    /*
    * Method prepares and adds components to the header panel
    * */
    private void prepareHeaderPanel() {

        headerPanel.setLayout(new GridBagLayout());

        JLabel headerLabel = new JLabel("Sign Up", JLabel.CENTER);
        headerLabel.setFont(new Font("Calibri", Font.BOLD, 18));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        headerPanel.add(headerLabel,gbc);

    }

    /*
    * Method prepares and adds components to the form panel
    * */
    private void prepareFormPanel() {

        formPanel.setLayout(new GridBagLayout());

        JLabel userLabel = new JLabel("New Username: ", JLabel.LEFT);
        JLabel passLabel = new JLabel("New Password: ", JLabel.LEFT);

        userField = new JTextField(12);

        passField = new JPasswordField(12);

        statusLabel = new JLabel("Fill in all fields.",JLabel.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipady = 5;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,5,0);
        formPanel.add(userLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,5,0);
        formPanel.add(userField,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,0,5,0);
        formPanel.add(passLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,0,5,0);
        formPanel.add(passField,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5,0,0,0);
        formPanel.add(statusLabel,gbc);
    }

    /*
    * Method compares and adds components to the field panel
    * */
    private void prepareButtonPanel() {

        buttonPanel.setLayout(new GridBagLayout());

        JButton signupButton = new JButton("Finish");
        signupButton.addActionListener(new action.Signup(this));

        JButton cancelButton = new JButton(("Cancel"));
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(new action.Signup(this));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,0);
        buttonPanel.add(signupButton,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,0,0);
        buttonPanel.add(cancelButton,gbc);

    }

    /*
    * Centers frame and displays GUI
    * */
    public void showGUI() {
        //center frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true);
    }

    /*
    * Getter Methods
    * */

    public JTextField getUserField() {
        return userField;
    }

    public JPasswordField getPassField() {
        return passField;
    }

}
