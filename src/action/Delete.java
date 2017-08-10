package action;

import gui.TableUI;
import paths.OutFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *Class defines an action that deletes entries from the table and updates the database of any changes
 */
public class Delete implements ActionListener {

    private TableUI tableUI;
    private Connection connection;

    public Delete(TableUI tableUI, Connection connection) {
        this.tableUI = tableUI;
        this.connection = connection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //Writer created to append to a log file
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true));

            Statement stmt;
            String sql;
            stmt = connection.createStatement();

            //Get ISBN number from selected row
            int row = tableUI.getTable().getSelectedRow();
            String isbn = tableUI.getTable().getValueAt(row, 0) + "";
            boolean quantityZero = false;

            //Current time to be displayed on log area
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String currentTime = dtf.format(now);

            //If remove button pressed
            if (e.getActionCommand().equals("Remove")) {
                //Quantity value of selected row reduced by one
                int quantity = Integer.parseInt(tableUI.getTable().getValueAt(row, 6)+"") - 1;

                //Update quantity value for ISBN in database
                sql = "UPDATE books SET quantity = '" + quantity + "'WHERE isbn = " + isbn;
                stmt.executeUpdate(sql);

                //Update interface table
                tableUI.getInventoryTableModel().setValueAt(quantity,row,6);
                if(quantity == 0) {
                    quantityZero = true;
                }

                //Write to log file
                tableUI.getLogArea().setText("Reduced quantity of: " + isbn + " " + currentTime);
                writer.write(sql);
                writer.newLine();
                writer.flush();
            }
            //If delete button pressed or quantity reduced reached zero
            if (e.getActionCommand().equals("Delete") || quantityZero == true) {
                //Deletion SQL
                sql = "DELETE FROM books " +
                        "WHERE isbn = " + isbn;
                stmt.executeUpdate(sql);

                //Remove selected row from interface table
                tableUI.getInventoryTableModel().removeRow(row);

                //Write to log file
                tableUI.getLogArea().setText("Deleted: " + isbn + " " + currentTime);
                writer.write(sql);
                writer.newLine();
                writer.flush();
            }

            //Get current sum of prices from database
            ResultSet rs;
            rs = stmt.executeQuery("SELECT sum(price*quantity) FROM books");
            if(rs.next()) {
                //Format and update price on interface
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                Double value = rs.getDouble(1);
                String totalPrice = formatter.format(value);
                tableUI.getTotalPriceLabel().setText("Total: "+ totalPrice);
            }
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}
