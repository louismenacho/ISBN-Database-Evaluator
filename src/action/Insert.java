package action;

import gui.TableUI;
import model.Book;
import paths.OutFile;
import web.BookMiner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *Class defines inserting actions for the tables and database
 */
public class Insert implements ActionListener {

    private TableUI tableUI;
    private Connection connection;

    public Insert(TableUI tableUI, Connection connection) {
        this.tableUI = tableUI;
        this.connection = connection;

        constructTableModel();
    }

    public Insert(TableUI tableUI, ArrayList<Book> bookList, Connection connection, boolean isAdding) {
        this.tableUI = tableUI;
        this.connection = connection;

        //If New Database... pressed
         if(!isAdding) {
            clearDatabase();
         }

        //Insert all books to database
        for (Book book : bookList) {
            insertToDatabase(book);
        }
        constructTableModel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //If Add button pressed
        if(e.getActionCommand().equals("Add")) {
            //Update log area
            tableUI.getLogArea().setText("Fetching data...");
            boolean newInsert = false;

            //Get ISBN from text field
            String ISBN = tableUI.getInputField().getText();
            if(!isValid(ISBN)) {
                JOptionPane.showMessageDialog(null, "Invalid ISBN number:" + ISBN);
                return;
            }

            BookMiner miner = new BookMiner();
            Book book = miner.getBookInfo(ISBN);
            //If book title not null
            if(book.getTitle() != null) {
                //Insert to database and reconstruct table
                insertToDatabase(book);
                constructTableModel();
                newInsert = true;
            } else {
                //Else book unrecognized
                JOptionPane.showMessageDialog(null, "Could not recognize books with ISBN number:" + ISBN);
            }

            //Reset text field
            tableUI.getInputField().setText("");

            if(newInsert) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String currentTime = dtf.format(now);
                tableUI.getLogArea().setText("Table Updated " + currentTime);
            }
            else {
                //If no new insert
                tableUI.getLogArea().setText("No changes made.");
            }
        }
    }

    private void insertToDatabase(Book book){
        //Connect as user to create necessary tables
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true));

            Statement stmt;
            ResultSet rs;
            String sql;

            String isbn = book.getISBN();
            String title = book.getTitle();
            String author = book.getAuthor();
            String publisher = book.getPublisher();
            String year_published = book.getYear();
            String price = book.getPrice();
            int quantity = 1;
            boolean duplicate = false;
            String imageLink = book.getImageLink();

            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM books WHERE isbn = '" + isbn + "'");
            while (rs.next()) {
                quantity = rs.getInt("quantity");
                quantity++;
                duplicate = true;
            }

            //If duplicate found then update quantity else insert a new entry into database
            if (duplicate) {
                sql = "UPDATE books SET quantity = '" + quantity + "'WHERE isbn = " + isbn;
                writer.write(sql);
                writer.newLine();
                writer.flush();
            } else {
                sql = "INSERT INTO books(isbn,title,author,publisher,year_published,price,quantity,image_link) " +
                        "VALUES('" + isbn + "','" + title + "','" + author + "','" + publisher + "','" + year_published + "','" + price + "','" + quantity + "','" + imageLink + "');";
                writer.write(sql);
                writer.newLine();
                writer.flush();
            }
            stmt.executeUpdate(sql);

            //Format time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String currentTime = dtf.format(now);

            sql = "INSERT INTO records(isbn,title,date_entered) " +
                    "VALUES('" + isbn + "','" + title + "','" + currentTime + "');";
            writer.write(sql);
            writer.newLine();
            writer.flush();

            stmt.executeUpdate(sql);

            rs = stmt.executeQuery("SELECT sum(price*quantity) FROM books");
            if(rs.next()) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                Double value = rs.getDouble(1);
                String totalPrice = formatter.format(value);
                tableUI.getTotalPriceLabel().setText("Total: "+ totalPrice);
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Format time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);

        //Update log area
        tableUI.getLogArea().setText("Table updated " + currentTime);
    }

    private void clearDatabase() {
        try {
            //Connect as user to create necessary tables
            Statement stmt = connection.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SHOW TABLES LIKE 'books'");
            if(rs.next()) {
                stmt.executeUpdate("DELETE FROM books");
            }

            //Finished creating tables
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void constructTableModel() {

        HashMap<String,Integer> isbnToQuantity = new HashMap<>();
        tableUI.getInventoryTableModel().setRowCount(0);

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");

            while (rs.next()) {
                isbnToQuantity.put(rs.getString("isbn"), rs.getInt("quantity"));
            }

            rs = stmt.executeQuery("SELECT * FROM books");

            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");;
                String publisher = rs.getString("publisher");
                String year_published = rs.getString("year_published");
                Double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Object rowData[] = new Object[] {isbn,title,author,publisher,year_published,price,quantity};
                tableUI.getInventoryTableModel().addRow(rowData);
            }

            rs = stmt.executeQuery("SELECT * FROM records");

            while (rs.next()) {
                String id = rs.getString("id");
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");;
                String dateEntered = rs.getString("date_entered");

                Object rowData[] = new Object[] {id,isbn,title,dateEntered};
                tableUI.getHistoryTableModel().addRow(rowData);
            }

            rs = stmt.executeQuery("SELECT sum(price*quantity) FROM books");
            if(rs.next()) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                Double value = rs.getDouble(1);
                String totalPrice = formatter.format(value);
                tableUI.getTotalPriceLabel().setText("Total: "+ totalPrice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid(String ISBN) {

        if(ISBN.length() != 10) {
            return false;
        }
        char [] digits = ISBN.toCharArray();
        int i, a = 0, b = 0;
        for (i = 0; i < 10; i++) {
            a += digits[i];
            b += a;
        }
        return b % 11 == 0;
    }


}

