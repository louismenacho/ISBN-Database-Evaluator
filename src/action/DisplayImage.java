package action;

import gui.TableUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *Class defines an action that display a book's image in the control panel
 */
public class DisplayImage extends MouseAdapter implements TableModelListener {

    private TableUI tableUI;
    private Connection connection;

    private int selectedRow;

    public DisplayImage(TableUI tableUI, Connection connection) {
        this.tableUI = tableUI;
        this.connection = connection;
        updateOnStartUp();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        selectedRow = tableUI.getTable().getSelectedRow();
        update();
    }

    private void update() {

        if(tableUI.getInventoryTableModel().getRowCount() == 0) {
            BufferedImage blackImage = new BufferedImage(230,340,BufferedImage.TYPE_BYTE_GRAY);
            tableUI.getImageLabel().setIcon(new ImageIcon(blackImage));
            return;
        }

        String ISBN = tableUI.getTable().getValueAt(selectedRow, 0) + "";

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs;

            String imageURL = "";
            rs = stmt.executeQuery("SELECT * FROM books WHERE isbn = '" + ISBN + "'");
            if(rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");;
                String publisher = rs.getString("publisher");
                String year_published = rs.getString("year_published");
                Double price = rs.getDouble("price");
                imageURL = rs.getString("image_link");

                tableUI.getBookInfoArea().setText("Title: \n" + title +
                        "\n\nAuthor: \n" + author +
                        "\n\nPublisher: \n" + publisher +
                        "\n\nYear: \n" + year_published +
                        "\n\nPrice: \n" + price
                );

                URL url = new URL(imageURL);
                BufferedImage image = ImageIO.read(url);
                tableUI.getImageLabel().setIcon(new ImageIcon(image));
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void updateOnStartUp() {
        try {

            Statement stmt = connection.createStatement();
            ResultSet rs;

            String imageURL = "";
            rs = stmt.executeQuery("SELECT * FROM books");
            if(rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");;
                String publisher = rs.getString("publisher");
                String year_published = rs.getString("year_published");
                Double price = rs.getDouble("price");
                imageURL = rs.getString("image_link");

                tableUI.getBookInfoArea().setText("Title: \n" + title +
                        "\n\nAuthor: \n" + author +
                        "\n\nPublisher: \n" + publisher +
                        "\n\nYear: \n" + year_published +
                        "\n\nPrice: \n" + price
                );

                URL url = new URL(imageURL);
                BufferedImage image = ImageIO.read(url);
                tableUI.getImageLabel().setIcon(new ImageIcon(image));
            } else {
                return;
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if(e.getType() == TableModelEvent.DELETE) {

            int rowCount = tableUI.getInventoryTableModel().getRowCount();
            if(selectedRow == rowCount && rowCount > 0 ) {
                selectedRow += -1;
            }
            update();
        } else
        if (e.getType() == TableModelEvent.INSERT){
            updateOnStartUp();
        }
    }

}
