package pharmacy;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Shop {
    private JPanel mainPanel;
    private JTextField txtShopName;
    private JButton btnSave;
    private JTextField txtPhone;
    private JTextField txtShopAddress;

    public Shop() {
        fetchData();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
    }

    public JPanel getMainPanel(){
        return mainPanel;
    }
    private boolean validateData() {
        boolean isValid = true;
        String name = txtShopName.getText();
        String address = txtShopAddress.getText();

        if (name.equals("") || address.equals("") ) {
            isValid = false;
        }

        return isValid;
    }
    public void fetchData() {
        try {
            String id="1";//hard coded

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT name, address, phone " +
                    "FROM shop " +
                    "where 1=1 " +
                    "AND shop_id=" + id);

            if (rs.next()) {
                txtShopName.setText(rs.getString(1));
                txtShopAddress.setText(rs.getString(2));
                txtPhone.setText(rs.getString(3));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void saveData() {
        try {
            if (!validateData()) {
                JOptionPane.showMessageDialog(null, "Please fill out shop name or address.");
                return;
            }
            String shopId = "1";//hardcoded only 1
            Statement s = db.mycon().createStatement();

            String name = txtShopName.getText();
            String address = txtShopAddress.getText();
            String phone = txtPhone.getText();

            //Update
            Integer done = s.executeUpdate("UPDATE shop SET name = '" + name + "', address = '" + address + "', phone= '" + phone + "' " +
                    "WHERE shop_id=" + shopId);

            JOptionPane.showMessageDialog(null, "Updated successfully.");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
