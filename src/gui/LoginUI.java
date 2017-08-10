package gui;

import javax.swing.*;
import java.awt.*;

/**
 *Class defines the log in user interface
 */
public class LoginUI extends JFrame {

    //Main panels
    private JPanel headerPanel;
    private JPanel fieldPanel;
    private JPanel buttonPanel;

    //Fields for username and password
    private JTextField userField;
    private JPasswordField passField;

    //Button to login
    private JButton loginButton;

    /*
    * Constructor prepares every panel before displaying GUI
    * */
    public LoginUI() {
        prepareFrame();
        prepareHeaderPanel();
        prepareFieldPanel();
        prepareButtonPanel();
        showGUI();
    }

    /*
    * Method prepares the frame of the window
    * */
    public void prepareFrame() {
        setTitle("Login");
        setSize(320,380);
        setResizable(false);
        setLayout(new GridLayout(3,1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        headerPanel = new JPanel();
        fieldPanel = new JPanel();
        buttonPanel = new JPanel();

        add(headerPanel);
        add(fieldPanel);
        add(buttonPanel);
    }

    /*
    * Method prepares and adds components to the header panel
    * */
    private void prepareHeaderPanel() {

        headerPanel.setLayout(new GridBagLayout());

        JLabel headerLabel = new JLabel("Media Evaluation", JLabel.CENTER);
        headerLabel.setFont(new Font("Calibri", Font.BOLD, 18));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        headerPanel.add(headerLabel,gbc);
    }

    /*
    * Method prepares and adds components to the field panel
    * */
    private void prepareFieldPanel() {

        fieldPanel.setLayout(new GridBagLayout());

        JLabel userLabel = new JLabel("Username: ", JLabel.LEFT);
        JLabel passLabel = new JLabel("Password: ", JLabel.LEFT);

        userField = new JTextField(12);

        passField = new JPasswordField(12);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipady = 5;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,10,0);
        fieldPanel.add(userLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,10,0);
        fieldPanel.add(userField,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,0,0);
        fieldPanel.add(passLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,0,0);
        fieldPanel.add(passField,gbc);
    }

    /*
    * Method compares and adds components to the field panel
    * */
    private void prepareButtonPanel() {

        buttonPanel.setLayout(new GridBagLayout());

        loginButton = new JButton("Login");
        loginButton.addActionListener(new action.Login(this));

        JButton signupButton = new JButton("Sign Up");
        signupButton.setActionCommand("SignupUI");
        signupButton.addActionListener(new action.Signup());

        JButton testingButton = new JButton("Quick Test");
        testingButton.setActionCommand("Test");
        testingButton.addActionListener(new action.Signup(this));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,0);
        buttonPanel.add(loginButton,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,0,0);
        buttonPanel.add(signupButton,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0,0,0,0);
        buttonPanel.add(testingButton,gbc);
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
    * Getter methods
    * */

    public JTextField getUserField() {
        return userField;
    }

    public JPasswordField getPassField() {
        return passField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}
