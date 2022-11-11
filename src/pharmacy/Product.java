package pharmacy;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Product {
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel createPanel;
    private JTextField txtName;
    private JTextField txtFormula;
    private JButton btnClear;
    private JButton btnSave;
    private JComboBox cboFormulation;
    private JButton btnDelete;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JTable tblProduct;
    private JTextField txtProductId;
    private JTextField txtPackSize;
    private JTextField txtCode;
    private JTextField txtStrength;
    private JTextField txtOneStrip;
    private JTabbedPane tabbedPane1;
    private JTextField txtTotalPack;


    private JTable tblCategory;
    private JTextField txtCategorySearch;
    private JButton btnSaveCategory;
    private JButton btnClearCategory;
    private JButton btnDeleteCategory;
    private JTextField txtCategoryId;
    private JTextField txtCategoryName;
    private JTextField txtPackSalePrice;
    private JComboBox cboCompany;
    private JTextField txtItemStockQty;
    private JTextField txtItemSalePrice;
    private JTextField txtItemDiscountPercent;
    private JTextField txtItemDiscountValue;
    private JTextField txtItemPriceAfterDiscount;
    private JPanel pnlExpiry;
    private JTextField txtPackPurchasePrice;
    private JTextField txtItemPurchasePrice;
    private JTextField txtItemProfit;


    JDateChooser dateChooser1;

    public Product() {
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
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                searchGrid();
            }
        });
        tblProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                fetchData();
            }
        });
        txtPackPurchasePrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                calculatePurchasePrice();
            }
        });
        txtPackSalePrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                calculateSalePrice();
                calculateDiscount();
            }
        });
        txtItemDiscountPercent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                calculateDiscount();
            }
        });
        txtPackSize.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                calculatePurchasePrice();
                calculateSalePrice();
                calculateDiscount();
            }
        });
        txtTotalPack.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                calculateItemStock();
            }
        });

        txtOneStrip.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyIntegers(e);
            }
        });
        txtPackSize.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyIntegers(e);
            }
        });
        txtTotalPack.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyIntegers(e);
            }
        });

        txtPackPurchasePrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyDecimal(e);
            }
        });
        txtPackSalePrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyDecimal(e);
            }
        });
        txtItemDiscountPercent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyDecimal(e);
            }
        });
        txtItemStockQty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyIntegers(e);
            }
        });
    }
    private void onlyIntegers(KeyEvent e){
        char c = e.getKeyChar();
        if (!((c >= '0') && (c <= '9') ||
                (c == KeyEvent.VK_BACK_SPACE) ||
                (c == KeyEvent.VK_DELETE))) {
            //getToolkit().beep();
            e.consume();
        }
    }
    private void onlyDecimal(KeyEvent e){
        char c = e.getKeyChar();
        if (!((c >= '0') && (c <= '9') ||
                (c == KeyEvent.VK_BACK_SPACE) ||
                (c == KeyEvent.VK_DELETE) ||
                (c== '.')
        )) {
            //getToolkit().beep();
            e.consume();
        }
    }
    private void calculatePurchasePrice() {
        try {
            Double packPurchasePrice = Double.valueOf(txtPackPurchasePrice.getText());
            Double itemQty = Double.valueOf(txtPackSize.getText());
            Double itemPurchasePrice = Util.round(packPurchasePrice / itemQty, 2);
            txtItemPurchasePrice.setText(itemPurchasePrice.toString());

            //-profit
            Double itemUnitPrice = Double.valueOf(txtItemSalePrice.getText());
            Double itemDiscountPercent = Double.valueOf(txtItemDiscountPercent.getText());
            Double itemDiscountValue = Util.round((itemUnitPrice * (itemDiscountPercent / 100)), 2);
            Double itemPriceAfterDiscount = Util.round(itemUnitPrice - itemDiscountValue, 2);
            Double itemProfit = Util.round(itemPriceAfterDiscount - itemPurchasePrice, 2);

            txtItemProfit.setText(itemProfit.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateSalePrice() {
        try {
            Double packUnitPrice = Double.valueOf(txtPackSalePrice.getText());
            Double itemQty = Double.valueOf(txtPackSize.getText());
            Double itemUnitPrice = Util.round(packUnitPrice / itemQty, 2);

            txtItemSalePrice.setText(itemUnitPrice.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateDiscount() {
        try {
            Double itemPurchasePrice = Double.valueOf(txtItemPurchasePrice.getText());

            Double itemUnitPrice = Double.valueOf(txtItemSalePrice.getText());
            Double itemDiscountPercent = Double.valueOf(txtItemDiscountPercent.getText());
            Double itemDiscountValue = Util.round((itemUnitPrice * (itemDiscountPercent / 100)), 2);
            Double itemPriceAfterDiscount = Util.round(itemUnitPrice - itemDiscountValue, 2);
            Double itemProfit = Util.round(itemPriceAfterDiscount - itemPurchasePrice, 2);

            txtItemDiscountValue.setText(itemDiscountValue.toString());
            txtItemPriceAfterDiscount.setText(itemPriceAfterDiscount.toString());

            //-profit
            txtItemProfit.setText(itemProfit.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateItemStock() {
        try {
            Double totalPacks = Double.valueOf(txtTotalPack.getText());
            Double packSize = Double.valueOf(txtPackSize.getText());

            Double totalItemStock = Util.round(totalPacks * packSize, 2);
            txtItemStockQty.setText(totalItemStock.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void init() {
        Date date = new Date();

        dateChooser1 = new JDateChooser();
        dateChooser1.setDateFormatString("dd-MMM-yyyy");
        dateChooser1.setDate(date);
        dateChooser1.setBounds(-1, -1, -1, 50);
        pnlExpiry.add(dateChooser1);

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Id", "Name", "Formulation", "Strength", "Item Qty", "Item Unit Price", "Item Discount"
                }
        ));
        fillGrid("");
        fillFormulation();
        fillCompany();
    }

    //---------------- Product --------------------//
    public void searchGrid() {
        String filter = txtSearch.getText();
        fillGrid(filter);
    }

    public void fillGrid(String filter) {
        try {

            DefaultTableModel dt = (DefaultTableModel) tblProduct.getModel();
            dt.setRowCount(0);

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT product_id, name, formulation, strength, item_stock_qty, item_sale_price, item_discount_value " +
                    "FROM Product where 1=1 " +
                    "AND is_deleted=0 " +
                    "AND (product_id like '%" + filter + "%' OR formulation like '%" + filter + "%' OR name like '%" + filter + "%' OR bar_code like '%" + filter + "%')");

            while (rs.next()) {

                Vector v = new Vector();
                v.add(rs.getString("product_id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("formulation"));
                v.add(rs.getString("strength"));
                v.add(rs.getString("item_stock_qty"));
                v.add(rs.getString("item_sale_price"));
                v.add(rs.getString("item_discount_value"));

                dt.addRow(v);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void fetchData() {
        try {
            int index = tblProduct.getSelectedRow();
            TableModel model = tblProduct.getModel();
            String id = model.getValueAt(index, 0).toString();
            String expiryDate;

            System.out.println("id" + id);

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery(
                    "SELECT product_id, name, formulation, bar_code, description, formula, strength" +
                            ", one_strip, pack_size, total_pack, pack_purchase_price, pack_sale_price, company, date_format(expiry, '%d-%b-%Y') as expiry " +
                            ", item_stock_qty, item_purchase_price, item_sale_price, item_discount_percent, item_discount_value, item_price_after_discount, item_profit, is_deleted " +
                            "FROM Product " +
                            "where 1=1 " +
                            "AND is_deleted=0 " +
                            "AND product_id=" + id);

            if (rs.next()) {

                txtProductId.setText(rs.getString("product_id"));
                txtName.setText(rs.getString("name"));
                cboFormulation.setSelectedItem(rs.getString("formulation"));
                txtCode.setText(rs.getString("bar_code"));
                txtFormula.setText(rs.getString("formula"));
                txtStrength.setText(rs.getString("strength"));
                txtOneStrip.setText(rs.getString("one_strip"));
                txtPackSize.setText(rs.getString("pack_size"));
                txtTotalPack.setText(rs.getString("total_pack"));
                txtPackPurchasePrice.setText(rs.getString("pack_purchase_price"));
                txtPackSalePrice.setText(rs.getString("pack_sale_price"));
                cboCompany.setSelectedItem(rs.getString("company"));

                expiryDate =  rs.getString("expiry");
                if(expiryDate != null || !(rs.getString("expiry").equals(""))) {
                    try {
                        //dateChooser1.
                        Date dt = new SimpleDateFormat("dd-MMM-yyyy").parse(rs.getString("expiry"));

                        dateChooser1.setDate(dt);
                    }catch (ParseException e){
                        System.out.println(e);
                    }
                }

                txtItemStockQty.setText(rs.getString("item_stock_qty"));
                txtItemPurchasePrice.setText(rs.getString("item_purchase_price"));
                txtItemSalePrice.setText(rs.getString("item_sale_price"));
                txtItemDiscountPercent.setText(rs.getString("item_discount_percent"));
                txtItemDiscountValue.setText(rs.getString("item_discount_value"));
                txtItemPriceAfterDiscount.setText(rs.getString("item_price_after_discount"));
                txtItemProfit.setText(rs.getString("item_profit"));
            }
        }catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void clearData() {
        txtProductId.setText("0");
        txtName.setText("");
        txtFormula.setText("");
        txtCode.setText("");
        txtStrength.setText("");
        txtOneStrip.setText("");
        txtPackSize.setText("");

        txtCode.setText("");
        txtFormula.setText("");
        txtStrength.setText("");
        txtOneStrip.setText("");
        txtPackSize.setText("");
        txtTotalPack.setText("");
        txtPackPurchasePrice.setText("");
        txtPackSalePrice.setText("");
        cboCompany.setSelectedItem("");
        dateChooser1.setDate(new Date());

        txtItemStockQty.setText("");
        txtItemPurchasePrice.setText("");
        txtItemSalePrice.setText("");
        txtItemDiscountPercent.setText("");
        txtItemDiscountValue.setText("");
        txtItemPriceAfterDiscount.setText("");
        txtItemProfit.setText("");
    }

    public void deleteData() {
        try {
            String id = txtProductId.getText();
            if (!(id.equals("0"))) {
                Integer confirm = JOptionPane.showConfirmDialog(null, "Are you sure want to delete?");
                if (confirm == 0) {
                    //Delete it

                    Statement s = db.mycon().createStatement();
                    Integer done = s.executeUpdate("UPDATE product set is_deleted=1 where product_id=" + id);

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
        if (txtName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter name.");
            txtName.grabFocus();
            return false;
        }
        if (txtCode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter code.");
            txtCode.grabFocus();
            return false;
        }
        if (txtStrength.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter strength.");
            txtStrength.grabFocus();
            return false;
        }
        if (cboCompany.getSelectedIndex()==0) {
            JOptionPane.showMessageDialog(null, "Please select company.");
            cboCompany.grabFocus();
            return false;
        }
        if (txtOneStrip.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter one strip.");
            txtOneStrip.grabFocus();
            return false;
        }
        if (txtPackPurchasePrice.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter pack purchase price.");
            txtPackPurchasePrice.grabFocus();
            return false;
        }
        if (txtPackSalePrice.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter pack sale price.");
            txtPackSalePrice.grabFocus();
            return false;
        }
        if (txtPackSize.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter pack size.");
            txtPackSize.grabFocus();
            return false;
        }
        if (txtItemDiscountPercent.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter item discount percentage.");
            txtItemDiscountPercent.grabFocus();
            return false;
        }
        if (txtTotalPack.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter total pack.");
            txtTotalPack.grabFocus();
            return false;
        }

        return true;
    }

    public void saveData() {
        try {
            if (!validateData()) {
                return;
            }

            String id = txtProductId.getText();
            Statement s = db.mycon().createStatement();


            String name = txtName.getText();
            String formulation = cboFormulation.getSelectedItem().toString();
            String bar_code = txtCode.getText();
            String formula = txtFormula.getText();
            String strength = txtStrength.getText();
            String one_strip = txtOneStrip.getText();
            String pack_size = txtPackSize.getText();
            String total_pack = txtTotalPack.getText();
            String pack_purchase_price = txtPackPurchasePrice.getText();
            String pack_sale_price = txtPackSalePrice.getText();

            String company = cboCompany.getSelectedItem().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String expiry = sdf.format(dateChooser1.getDate());

            String item_stock_qty = txtItemStockQty.getText();
            String item_purchase_price = txtItemPurchasePrice.getText();
            String item_sale_price = txtItemSalePrice.getText();
            String item_discount_percent = txtItemDiscountPercent.getText();
            String item_discount_value = txtItemDiscountValue.getText();
            String item_price_after_discount = txtItemPriceAfterDiscount.getText();
            String item_profit = txtItemProfit.getText();

            if (id.equals("0")) {
                //Insert
                Integer done = s.executeUpdate(
                        "INSERT INTO product (" +
                                "name" +
                                ", formulation" +
                                ", bar_code" +
                                ", formula" +
                                ", strength" +
                                ", one_strip" +
                                ", pack_size" +
                                ", total_pack" +
                                ", pack_purchase_price" +
                                ", pack_sale_price" +
                                ", company" +
                                ", expiry" +
                                ", item_stock_qty" +
                                ", item_purchase_price" +
                                ", item_sale_price" +
                                ", item_discount_percent" +
                                ", item_discount_value" +
                                ", item_price_after_discount" +
                                ", item_profit" +
                                ") " +
                                " VALUES('" + name +
                                "', '" + formulation +
                                "', '" + bar_code +
                                "', '" + formula +
                                "', '" + strength +
                                "', '" + one_strip +
                                "', '" + pack_size +
                                "' , '" + total_pack +
                                "', '" + pack_purchase_price +
                                "', '" + pack_sale_price +
                                "', '" + company +
                                "', '" + expiry + "'" +
                                ",'" + item_stock_qty +
                                "','" + item_purchase_price +
                                "','" + item_sale_price +
                                "','" + item_discount_percent +
                                "','" + item_discount_value +
                                "','" + item_price_after_discount +
                                "','" + item_profit +
                                "');");

                JOptionPane.showMessageDialog(null, "Inserted successfully.");

                clearData();
                searchGrid();
            } else {
                //Update
                Integer done = s.executeUpdate(
                        "UPDATE product SET name = '" + name + "'" +
                                ", formulation = '" + formulation + "'" +
                                ", bar_code= '" + bar_code + "'" +
                                ", formula= '" + formula + "'" +
                                ", strength= '" + strength + "'" +
                                ", one_strip= '" + one_strip + "'" +
                                ", pack_size= '" + pack_size + "'" +
                                ", total_pack= '" + total_pack + "'" +
                                ", pack_purchase_price= '" + pack_purchase_price + "'" +
                                ", pack_sale_price= '" + pack_sale_price + "'" +
                                ", company= '" + company + "'" +
                                ", expiry= '" + expiry + "'" +
                                ", item_stock_qty= '" + item_stock_qty + "'" +
                                ", item_purchase_price= '" + item_purchase_price + "'" +
                                ", item_sale_price= '" + item_sale_price + "'" +
                                ", item_discount_percent= '" + item_discount_percent + "'" +
                                ", item_discount_value= '" + item_discount_value + "'" +
                                ", item_price_after_discount= '" + item_price_after_discount + "'" +
                                ", item_profit= '" + item_profit + "'" +
                                "WHERE product_id=" + id);

                JOptionPane.showMessageDialog(null, "Updated successfully.");

                clearData();
                searchGrid();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void fillFormulation() {
        try {

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT formulation_id, name from formulation where is_deleted=0");

            cboFormulation.removeAllItems();

            while (rs.next()) {
                cboFormulation.addItem(rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void fillCompany() {
        try {

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT company_id, name from company where is_deleted=0");

            cboCompany.removeAllItems();

            while (rs.next()) {
                cboCompany.addItem(rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    //----------------------------------------------//

}
