package action;

import gui.TableUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 *Class defines an action that switches the table view between the inventory and history model
 */
public class ChangeView implements ActionListener {

    private TableUI tableUI;

    public ChangeView(TableUI tableUI, Connection connection) {
        this.tableUI = tableUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //If clicked on View > History
        if(e.getActionCommand().equals("Change to History")) {

            tableUI.changeTableModel(tableUI.getHistoryTableModel());
            tableUI.getDeleteButton().setEnabled(false);
            tableUI.getRemoveButton().setEnabled(false);
            tableUI.getInsertButton().setEnabled(false);
            tableUI.getInputField().setEnabled(false);
            tableUI.getTableTitleLabel().setText("History Table");
            return;
        }
        //If clicked on View > Inventory
        if (e.getActionCommand().equals("Change to Inventory")) {

            tableUI.changeTableModel(tableUI.getInventoryTableModel());
            tableUI.getDeleteButton().setEnabled(true);
            tableUI.getRemoveButton().setEnabled(true);
            tableUI.getInsertButton().setEnabled(true);
            tableUI.getInputField().setEnabled(true);
            tableUI.getTableTitleLabel().setText("Inventory Table");
            return;
        }
    }
}
