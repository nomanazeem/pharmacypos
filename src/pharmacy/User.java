package pharmacy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class User {
    private JPanel searchPanel;
    private JTextField txtSearch;
    private JTable tblUser;
    private JPanel createPanel;
    private JTextField txtName;
    private JTextField txtContactNo;
    private JButton btnClear;
    private JButton btnSave;
    private JButton btnDelete;
    private JTextField txtUserId;
    private JPanel mainPanel;
    private JComboBox cboRole;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public User() {
        init();

        tblUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                fetchData();
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                searchGrid();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearData();
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    private void init() {
        tblUser.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Id", "Role", "Name", "Username","Phone"
                }
        ));
        fillGrid("");
        fillRole();
    }

    public void searchGrid() {
        String filter = txtSearch.getText();
        fillGrid(filter);
    }

    public void fillGrid(String filter) {
        try {

            DefaultTableModel dt = (DefaultTableModel) tblUser.getModel();
            dt.setRowCount(0);

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT user_id, role, name, username, phone " +
                    "FROM user where 1=1 " +
                    "AND is_deleted=0 " +
                    "AND (user_id like '%" + filter + "%' OR name like '%" + filter + "%' OR role like '%" + filter + "%' OR username like '%" + filter + "%')");

            while (rs.next()) {

                Vector v = new Vector();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));

                dt.addRow(v);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    private void fillRole() {
        cboRole.addItem("Admin");
        cboRole.addItem("Salesman");
    }

    public void fetchData() {
        try {
            int index = tblUser.getSelectedRow();
            TableModel model = tblUser.getModel();
            String id = model.getValueAt(index, 0).toString();

            //System.out.println("id" + id);

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT user_id, role, name, username, password, phone " +
                    "FROM User " +
                    "where 1=1 " +
                    "AND is_deleted=0 " +
                    "AND user_id=" + id);

            if (rs.next()) {
                txtUserId.setText(rs.getString(1));
                cboRole.setSelectedItem(rs.getString(2));
                txtName.setText(rs.getString(3));
                txtUsername.setText(rs.getString(4));
                txtPassword.setText(rs.getString(5));
                txtContactNo.setText(rs.getString(6));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void clearData() {
        txtUserId.setText("0");
        txtName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtContactNo.setText("");
    }

    public void deleteData() {
        try {
            String id =txtUserId.getText();
            if (!(id.equals("0"))) {
                Integer confirm = JOptionPane.showConfirmDialog(null, "Are you sure want to delete?");
                if (confirm == 0) {
                    //Delete it

                    Statement s = db.mycon().createStatement();
                    Integer done = s.executeUpdate("UPDATE user set is_deleted=1 where user_id=" + id);

                    JOptionPane.showMessageDialog(null, "Deleted successfully.");

                    clearData();
                    searchGrid();
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    private boolean validateData() {
        boolean isValid = true;
        String name = txtName.getText();

        if (name.equals("")|| name.equals("")) {
            isValid = false;
        }

        return isValid;
    }

    public void saveData() {
        try {
            if (!validateData()) {
                JOptionPane.showMessageDialog(null, "Please fill out name.");
                return;
            }
            String id = txtUserId.getText();
            Statement s = db.mycon().createStatement();

            String name = txtName.getText();
            String role = cboRole.getSelectedItem().toString();
            String phone = txtContactNo.getText();
            String username = txtUsername.getText();
            String password = String.valueOf(txtPassword.getPassword());

            if (id.equals("0")) {
                //Insert
                Integer done = s.executeUpdate("INSERT INTO user (role, name, phone, username, password) " +
                        " VALUES('" + role + "', '" + name + "', '" + phone + "', '" + username + "', '" + password + "');");

                JOptionPane.showMessageDialog(null, "Inserted successfully.");

                clearData();
                searchGrid();
            } else {
                //Update
                Integer done = s.executeUpdate("UPDATE user SET role = '" + role + "', name = '" + name + "', phone = '" + phone + "', username = '" + username + "', password = '" + password + "'" +
                        "WHERE user_id=" + id);

                JOptionPane.showMessageDialog(null, "Updated successfully.");

                clearData();
                searchGrid();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
