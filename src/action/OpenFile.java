package action;

import gui.TableUI;
import model.Book;
import paths.InFile;
import paths.OutFile;
import web.BookMiner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *Class defines an action for opening and reading files
 */
public class OpenFile implements ActionListener {

    private TableUI tableUI;
    private Connection connection;

    private ArrayList<Book> bookList;
    private BufferedWriter writer;

    public OpenFile(TableUI tableUI, Connection connection) {
        this.tableUI = tableUI;
        this.connection = connection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("New") && !(OutFile.fileSpecified || InFile.fileSpecified)) {

            int result = JOptionPane.showConfirmDialog(null,"WARNING! This will erase your current database and create a new one. Do still wish to proceed?");
            if(result == JOptionPane.NO_OPTION || result == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        if(InFile.fileSpecified) {

            File file = new File(InFile.filePath);
            try {

                read(file);
                tableUI.addToTable(bookList,connection);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            InFile.fileSpecified = false;
            return;
        }

        //Display File Chooser
        JFileChooser fileDialog = new JFileChooser();
        tableUI.getLogArea().setText("Fetching data...");
        int returnVal = fileDialog.showOpenDialog(tableUI);

        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            //No file selected
            tableUI.getLogArea().setText("No changes.");
            return;

        } else {
            //Open file
            File file = fileDialog.getSelectedFile();
            try {
                read(file);
                if(e.getActionCommand().equals("New")) {

                    tableUI.createTable(bookList, connection);

                } else if (e.getActionCommand().equals("Add")) {

                    tableUI.addToTable(bookList,connection);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String currentTime = dtf.format(now);
            tableUI.getLogArea().setText("Table updated " + currentTime);
        }
    }

    private void read(File file) throws IOException {

        FileReader fileReader;
        if (InFile.fileSpecified) {

            fileReader = new FileReader(InFile.filePath);
        } else {

            fileReader = new FileReader(file);
        }

        BufferedReader br = new BufferedReader(fileReader);

        bookList = new ArrayList<>();
        BookMiner miner = new BookMiner();
        String invalidISBNs = "";

        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {

            String ISBN = sCurrentLine;
            if(isValid(ISBN)) {
                Book book = miner.getBookInfo(ISBN);
                bookList.add(book);
            } else {
                invalidISBNs += "\n"+ISBN;
            }
        }
        if(invalidISBNs.length() > 0) {
            JOptionPane.showMessageDialog(null, "Invalid ISBN number(s):" + invalidISBNs);
        }

        if(OutFile.fileSpecified) {
            writer = new BufferedWriter(new FileWriter(OutFile.filePath));
            Book.printFields(writer);
            writer.append(" | Quantity");
            writer.newLine();
        }

        String unrecognizedBooks = "";
        Set<Book> toRemove  = new HashSet<Book>();
        for (Book book : bookList) {
            if (book.getTitle() == null) {
                unrecognizedBooks += "\n" + book.getISBN();
                toRemove.add(book);
            } else if(OutFile.fileSpecified){
                book.printDetails(writer);
                writer.newLine();
            }
        }
        bookList.removeAll(toRemove);

        if(OutFile.fileSpecified) {
            writer.close();
        }

        if(unrecognizedBooks.length() > 0) {
            JOptionPane.showMessageDialog(null, "Could not recognize books with ISBN number(s):" + unrecognizedBooks);
        }

        OutFile.fileSpecified = false;
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
