package action;

import gui.TableUI;
import model.Book;
import paths.OutFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *Class defines an action that exports files based on an input file or the entire database
 */
public class Export implements ActionListener {

    TableUI tableUI;
    Connection connection;

    public Export(TableUI tableUI, Connection connection) {
        this.tableUI = tableUI;
        this.connection = connection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            String exportFileName = "";
            while (exportFileName.length() == 0) {

                exportFileName = JOptionPane.showInputDialog(null, "File name (.txt):");
                if (!exportFileName.matches("[a-zA-Z0-9\\._\\-]{1,}")) {
                    JOptionPane.showMessageDialog(null, "Please enter a proper file name.");
                }
            }

            if (!isTextFile(exportFileName)) {
                exportFileName = exportFileName + ".txt";
            }

            //Writer for export file
            BufferedWriter writer = new BufferedWriter(new FileWriter(exportFileName));

            if (e.getActionCommand().equals("Database")) {
                Statement stmt = connection.createStatement();
                ResultSet rs;

                rs = stmt.executeQuery("SELECT * FROM books");

                //Print book fields to writer
                Book.printFields(writer);
                writer.append(" | Quantity");
                writer.newLine();
                while (rs.next()) {

                    String isbn = rs.getString("isbn");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String publisher = rs.getString("publisher");
                    String year_published = rs.getString("year_published");
                    Double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");

                    writer.append(isbn + " | ");
                    writer.append(title + " | ");
                    writer.append(author + " | ");
                    writer.append(publisher + " | ");
                    writer.append(year_published + " | ");
                    writer.append(price + " | ");
                    writer.append(quantity + "");
                    writer.newLine();
                    writer.flush();
                }

            } else if (e.getActionCommand().equals("Report")) {
                OutFile.filePath = exportFileName;
                OutFile.fileSpecified = true;
                tableUI.getNewMenuItem().doClick();
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }


    private boolean isTextFile(String fileName) {

        int i = fileName.lastIndexOf(".");

        if(fileName.substring(i+1).equals("txt")) {
            return true;
        }

        return false;
    }
}
