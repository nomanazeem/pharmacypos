package pharmacy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Company {
    private JPanel searchPanel;
    private JTextField txtSearch;
    private JTable tblCompany;
    private JPanel createPanel;
    private JTextField txtName;
    private JTextField txtContactPerson;
    private JTextField txtContactNo;
    private JButton btnClear;
    private JButton btnSave;
    private JButton btnDelete;
    private JTextField txtCompanyId;
    private JPanel mainPanel;

    public Company(){
        init();
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
        tblCompany.addMouseListener(new MouseAdapter() {
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
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    private void init() {
        tblCompany.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Id", "Name", "Person", "Contact"
                }
        ));
        fillGrid("");
    }

    public void searchGrid() {
        String filter = txtSearch.getText();
        fillGrid(filter);
    }

    public void fillGrid(String filter) {
        try {

            DefaultTableModel dt = (DefaultTableModel) tblCompany.getModel();
            dt.setRowCount(0);

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT company_id, name, contact_person, contact_number, is_deleted " +
                    "FROM Company where 1=1 " +
                    "AND is_deleted=0 " +
                    "AND (company_id like '%" + filter + "%' OR name like '%" + filter + "%' OR contact_person like '%" + filter + "%')");

            while (rs.next()) {

                Vector v = new Vector();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));

                dt.addRow(v);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void fetchData() {
        try {
            int index = tblCompany.getSelectedRow();
            TableModel model = tblCompany.getModel();
            String id = model.getValueAt(index, 0).toString();

            //System.out.println("id" + id);

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT company_id, name, contact_person, contact_number,is_deleted " +
                    "FROM Company " +
                    "where 1=1 " +
                    "AND is_deleted=0 " +
                    "AND company_id=" + id);

            if (rs.next()) {
                txtCompanyId.setText(rs.getString(1));
                txtName.setText(rs.getString(2));
                txtContactPerson.setText(rs.getString(3));
                txtContactNo.setText(rs.getString(4));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void clearData() {
        txtCompanyId.setText("0");
        txtName.setText("");
        txtContactPerson.setText("");
        txtContactNo.setText("");
    }

    public void deleteData() {
        try {
            String id = txtCompanyId.getText();
            if (!(id.equals("0"))) {
                Integer confirm = JOptionPane.showConfirmDialog(null, "Are you sure want to delete?");
                if (confirm == 0) {
                    //Delete it

                    Statement s = db.mycon().createStatement();
                    Integer done = s.executeUpdate("UPDATE company set is_deleted=1 where company_id=" + id);

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

        if (name.equals("")) {
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
            String id = txtCompanyId.getText();
            Statement s = db.mycon().createStatement();

            String name = txtName.getText();
            String person = txtContactPerson.getText();
            String contact = txtContactNo.getText();

            if (id.equals("0")) {
                //Insert
                Integer done = s.executeUpdate("INSERT INTO company (name, contact_person, contact_number) " +
                        " VALUES('" + name + "', '" + person + "', '" + contact + "');");

                JOptionPane.showMessageDialog(null, "Inserted successfully.");

                clearData();
                searchGrid();
            } else {
                //Update
                Integer done = s.executeUpdate("UPDATE company SET name = '" + name + "', contact_person = '" + person + "', contact_number = '" + contact + "'" +
                        "WHERE company_id=" + id);

                JOptionPane.showMessageDialog(null, "Updated successfully.");

                clearData();
                searchGrid();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
