package gui;

import model.Book;
import paths.OutFile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.ArrayList;

/**
 *Class defines the main user interface of the program
 * Main components include:
 * -Menu Bar for switching views and handling file related tasks
 * -Header Panel displays user who is logged in, title of table, and total price of inventory
 * -Control Panel contains interface for adding, removing, and adjusting quantity of books. Also previews image of book
 * -ScrollPane(Table) contains a table displaying information about the book
 * -Log Panel displays recent activities
 */
public class TableUI extends JFrame {

    //Menu bar and menu components
    private JMenuBar menuBar;
    private JMenuItem newMenuItem;
    private JMenuItem logoutMenuItem;
    private JMenuItem inventoryMenuItem;
    private JMenuItem historyMenuItem;
    private JMenuItem addMenutItem;
    private JMenuItem reportMenuItem;
    private JMenuItem databaseMenuItem;

    //Defines the user that is current logged in
    //Defines title of table
    //Defines total price
    private JPanel headerPanel;
    private JLabel userLabel;
    private JLabel tableTitleLabel;
    private JLabel totalPriceLabel;

    //Input field for new ISBN number
    //Insert,delete,remove buttons to modify tables
    //Image label for image of book
    //Book info area display book title,author,year,etc.
    private JPanel controlPanel;
    private JTextField inputField;
    private JButton insertButton;
    private JButton deleteButton;
    private JButton removeButton;
    private JLabel imageLabel;
    private JTextArea bookInfoArea;

    //Table compponents to be added to the scroll pane
    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel inventoryTableModel;
    private DefaultTableModel historyTableModel;

    //Log area displays recent activites
    private JPanel logPanel;
    private JTextArea logArea;

    /*
    * Constructor prepares every panel before displaying GUI
    * */
    public TableUI() {
        prepareFrame();
        prepareMenuBar();
        prepareControlPanel();
        prepareScrollPane();
        prepareLogPanel();
        if(!OutFile.fileSpecified) {
            showGUI();
        }
    }

    /*
    * Method prepares the frame of the window
    * */
    private void prepareFrame() {

        setTitle("Media Evaluation");
        setSize(1440,900);
//        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        headerPanel = new JPanel();
        controlPanel = new JPanel();
        scrollPane = new JScrollPane();
        logPanel = new JPanel();

        setJMenuBar(menuBar);
        add(headerPanel,BorderLayout.NORTH);
        add(controlPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(logPanel,BorderLayout.SOUTH);
    }

    /*
    * Method prepares and adds components to the menu bar
    * */
    private void prepareMenuBar() {

        //create menus
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu exportMenu = new JMenu("Export");

        //create menu items

        newMenuItem = new JMenuItem("New Database...");
        newMenuItem.setActionCommand("New");

        addMenutItem = new JMenuItem("Add...");
        addMenutItem.setActionCommand("Add");

        logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.setActionCommand("Logout");

        inventoryMenuItem = new JMenuItem("Switch to Inventory");
        inventoryMenuItem.setActionCommand("Change to Inventory");

        historyMenuItem = new JMenuItem("Switch to History");
        historyMenuItem.setActionCommand("Change to History");

        reportMenuItem = new JMenuItem("Report...");
        reportMenuItem.setActionCommand("Report");

        databaseMenuItem = new JMenuItem("Database...");
        databaseMenuItem.setActionCommand("Database");


        //add menu items to menus
        fileMenu.add(addMenutItem);
        fileMenu.add(newMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(logoutMenuItem);
        viewMenu.add(inventoryMenuItem);
        viewMenu.add(historyMenuItem);
        exportMenu.add(reportMenuItem);
        exportMenu.add(databaseMenuItem);

        //add menu to menubar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(exportMenu);
    }

    /*
    * Method prepares and adds components to the header panel
    * */
    private void prepareHeaderPanel(String user){

        headerPanel.setLayout(new GridBagLayout());

        userLabel = new JLabel("Logged in as: "+ user);
        userLabel.setFont(new Font("Calibri", Font.BOLD, 15));

        tableTitleLabel = new JLabel("Inventory Table");
        tableTitleLabel.setFont(new Font("Calibri", Font.BOLD, 15));

        totalPriceLabel = new JLabel("Total Price: ");
        totalPriceLabel.setFont(new Font("Calibri", Font.BOLD, 15));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,5,0,0);
        headerPanel.add(userLabel,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0,0,0,0);
        headerPanel.add(tableTitleLabel,gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0,0,0,5);
        headerPanel.add(totalPriceLabel,gbc);
    }

    /*
    * Method prepares and adds components to the control panel
    * */
    private void prepareControlPanel() {

        controlPanel.setLayout(new GridBagLayout());

        JLabel previewLabel = new JLabel("Preview");
        previewLabel.setFont(new Font("Calibri", Font.BOLD, 15));

        BufferedImage blackImage = new BufferedImage(230,340,BufferedImage.TYPE_BYTE_GRAY);
        imageLabel = new JLabel(new ImageIcon(blackImage));
        imageLabel.setPreferredSize(new Dimension(230, 340));

        bookInfoArea = new JTextArea(
                "Title: \n" +
                "\nAuthor: \n" +
                "\nPublisher: \n" +
                "\nYear: \n" +
                "\nPrice: \n");
        bookInfoArea.setFont(new Font("Calibri", Font.PLAIN, 13));
        bookInfoArea.setEditable(false);
        bookInfoArea.setLineWrap(true);
        bookInfoArea.setWrapStyleWord(true);
        bookInfoArea.setOpaque(false);

        JPanel dummyPanel = new JPanel();
        JPanel addPanel = new JPanel(new GridBagLayout());

        JLabel newBookLabel = new JLabel("Add a New Book");
        newBookLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        JLabel inputLabel = new JLabel("ISBN:");
        inputField = new JTextField();

        insertButton = new JButton("Add");
        insertButton.setActionCommand("Add");

        removeButton = new JButton("Reduce Count");
        removeButton.setActionCommand("Remove");

        deleteButton = new JButton("Delete Entry");
        deleteButton.setActionCommand("Delete");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20,5,0,5);
        addPanel.add(newBookLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0,5,0,0);
        addPanel.add(inputLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0,0,0,5);
        addPanel.add(inputField,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0,0,0,0);
        addPanel.add(insertButton,gbc);

        gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,0);
        controlPanel.add(addPanel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,5,0,0);
        controlPanel.add(previewLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0,0,0,0);
        controlPanel.add(imageLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(5,5,0,5);
        controlPanel.add(bookInfoArea,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0,0,0,0);
        controlPanel.add(removeButton,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0,0,20,0);
        controlPanel.add(deleteButton,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0,0,0,0);
        controlPanel.add(dummyPanel,gbc);
    }

    /*
    * Method prepares and adds table components to the scroll pane
    * */
    public void prepareScrollPane() {
        Object inventoryColumnNames[] = {"ISBN", "Title", "Author", "Publisher", "Year", "Price", "Quantity"};
        inventoryTableModel = new DefaultTableModel(inventoryColumnNames,0);
        Object historyColumnNames[] = {"ID", "ISBN", "Title", "Date Entered"};
        historyTableModel = new DefaultTableModel(historyColumnNames,0);

        table = new JTable();
        table.setModel(inventoryTableModel);
        scrollPane.setViewportView(table);
    }

    /*
    * Method prepares and adds components to the log panel
    * */
    private void prepareLogPanel(){

        logPanel.setLayout(new GridBagLayout());

        logArea = new JTextArea("Nothing changed");
        logArea.setEditable(false);
        logArea.setBackground(new Color(249, 242, 226));
        logArea.setBorder(BorderFactory.createLineBorder(new Color(91, 91, 91), 1, false));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0,0,0,0);
        logPanel.add(logArea,gbc);
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
    * Creates a new table to be displayed on the scroll pane
    * */
    public void createTable(ArrayList<Book> bookList, Connection connection) {
        new action.Insert(this, bookList, connection,false);
    }

    /*
    * Adds entries to the table
    * */
    public void addToTable(ArrayList<Book> bookList, Connection connection) {
        new action.Insert(this, bookList, connection, true);
    }

    /*
    * Switches from inventory table to history table and vice versa
    * */
    public void changeTableModel(DefaultTableModel model) {
        table.setModel(model);
        scrollPane.setViewportView(table);
    }

    /*
    * Pass information to the GUI controllers*/
    public void pass(String user, Connection connection) {
        prepareHeaderPanel(user);
        action.OpenFile openFileAction = new action.OpenFile(this,connection);
        action.Logout logoutAction = new action.Logout(this,connection);
        action.ChangeView changeViewAction = new action.ChangeView(this, connection);
        action.Export exportAction = new action.Export(this, connection);
        action.Insert insertAction = new action.Insert(this,connection);
        action.Delete deleteAction = new action.Delete(this,connection);
        action.DisplayImage displayImageAction = new action.DisplayImage(this,connection);

        newMenuItem.addActionListener(openFileAction);
        addMenutItem.addActionListener(openFileAction);
        logoutMenuItem.addActionListener(logoutAction);
        inventoryMenuItem.addActionListener(changeViewAction);
        historyMenuItem.addActionListener(changeViewAction);
        reportMenuItem.addActionListener(exportAction);
        databaseMenuItem.addActionListener(exportAction);


        insertButton.addActionListener(insertAction);
        removeButton.addActionListener(deleteAction);
        deleteButton.addActionListener(deleteAction);

        table.addMouseListener(displayImageAction);
        inventoryTableModel.addTableModelListener(displayImageAction);
    }

    /*
    * Getter Methods
    * */

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getInventoryTableModel() {
        return inventoryTableModel;
    }

    public DefaultTableModel getHistoryTableModel() {
        return historyTableModel;
    }

    public JLabel getTableTitleLabel() {
        return tableTitleLabel;
    }

    public JLabel getTotalPriceLabel() {
        return totalPriceLabel;
    }

    public JButton getInsertButton() {
        return insertButton;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JMenuItem getNewMenuItem() {
        return newMenuItem;
    }

    public JTextArea getBookInfoArea() {
        return bookInfoArea;
    }

    public JTextField getInputField() {
        return inputField;
    }

    public JTextArea getLogArea() {
        return logArea;
    }
}