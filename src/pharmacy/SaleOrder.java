package pharmacy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class SaleOrder {
    private JPanel mainPanel;
    private JTable tblProduct;
    private JButton btnSaveAndPrint;
    private JTextField txtBill;
    private JTextField txtPay;
    private JButton btnAddToCart;
    private JComboBox cboProduct;
    private JTextField txtComments;
    private JTextField txtQty;
    private JComboBox cboCustomer;
    private JTextField txtDiscountValue;
    private JLabel txtTotalQty;
    private JLabel txtBalance;
    private JLabel txtTotalAmount;
    private JLabel txtName;
    private JLabel txtFormulation;
    private JLabel txtFormula;
    private JLabel txtInvoiceNo;
    private JButton btnRemove;
    private JButton btnMinus;
    private JButton btnPlus;
    private JLabel txtStrength;
    private JLabel txtPackSize;
    private JLabel txtPackSalePrice;
    private JLabel txtItemDiscountValue;
    private JLabel txtExpiry;
    private JLabel txtItemPriceAfterDiscount;
    private JLabel txtCompany;
    private JLabel txtItemStockQty;
    private JLabel txtItemSalePrice;
    private JLabel txtItemDiscountPercent;
    private JLabel txtTotalItemAmount;
    private JLabel txtTotalItemQty;
    private JButton btnNewOrder;
    private JLabel txtProductId;
    private JLabel txtBarCode;
    private JLabel txtPackPurchasePrice;
    private JLabel txtItemPurchasePrice;
    private JLabel txtItemProfit;

    private Integer userId;
    private String username;

    Integer iProductId=0, iProductName=1, iQty=2, iUnitPrice=3, iTotalPrice=4
            , iDiscount=5, iTotalAfterDiscount=6
            , iProfit = 7, iTotalProfit = 8;

    private void init(){
        txtInvoiceNo.setText(newInvoiceNo());

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "ProductId", "Name", "Qty", "Unit Price", "Total","Discount","Total After Disc.","Profit","T Profit"
                }
        ));
   }

    public SaleOrder(Integer userId, String username) {
        this.userId = userId;
        this.username=username;

        init();
        cboProduct.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                productSelection();
            }
        });
        /*cboProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                productSelection();
            }
        });*/

        txtQty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                productTotalCal();
            }
        });
        txtQty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    //getToolkit().beep();
                    e.consume();
                }
            }
        });

        txtDiscountValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                //discountCalculate();
            }
        });
        txtBill.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                total();
            }
        });
        txtPay.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                total();
            }
        });
        btnSaveAndPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
                //Util.preview(txtInvoiceNo.getText());//preview();
            }
        });
        btnAddToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DefaultTableModel dt = (DefaultTableModel) tblProduct.getModel();
                    int rw = tblProduct.getSelectedRow();
                    if(rw>-1) {
                        dt.removeRow(rw);

                        cartTotal();
                        total();
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        btnMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int rw = tblProduct.getSelectedRow();

                    if(rw>-1) {
                        int availableQty = 1;//Integer.parseInt(tblProduct.getValueAt(rw, 2).toString());
                        int minimumQty = availableQty;

                        int qty = Integer.parseInt(tblProduct.getValueAt(rw, iQty).toString());
                        if (qty > minimumQty) {
                            double unitDiscount = Double.valueOf(tblProduct.getValueAt(rw, iDiscount).toString());
                            unitDiscount = Util.round(unitDiscount/qty,2);

                            qty -= 1;

                            double discount = unitDiscount;

                            double totalDiscount = Util.round(qty * discount,2);

                            double unitPrice = Double.parseDouble(tblProduct.getValueAt(rw, iUnitPrice).toString());
                            double totalPrice = qty * unitPrice;
                            totalPrice = Util.round(totalPrice,2);
                            double totalAfterDiscount= Util.round(totalPrice-totalDiscount,2);

                            tblProduct.setValueAt(qty, rw, iQty);//for qty
                            tblProduct.setValueAt(totalDiscount, rw, iDiscount);//for discount
                            tblProduct.setValueAt(totalPrice, rw, iTotalPrice);//for totalPrice
                            tblProduct.setValueAt(totalAfterDiscount, rw, iTotalAfterDiscount);//for total after discount

                            //get profit
                            double itemProfit = Double.parseDouble(tblProduct.getValueAt(rw, iProfit).toString());

                            double dTotalProfit = Util.round(Double.valueOf(Integer.valueOf(qty) * Double.valueOf(itemProfit)),2);
                            tblProduct.setValueAt(dTotalProfit, rw, iTotalProfit);//total profit

                            //Calculate total
                            cartTotal();
                            total();
                        }//available qty
                    }//row index
                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        });

        btnPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int rw = tblProduct.getSelectedRow();

                    if(rw>-1) {
                        //int availableQty = 100;//Integer.parseInt(tblProduct.getValueAt(rw, 2).toString());
                        //int maximumQty = availableQty;

                        int qty = Integer.parseInt(tblProduct.getValueAt(rw, iQty).toString());
                        if (qty > 0) {
                            double unitDiscount = Double.valueOf(tblProduct.getValueAt(rw, iDiscount).toString());
                            unitDiscount = Util.round(unitDiscount/qty,2);

                            qty += 1;

                            //Stock validation
                            if(stockValidate(qty)==false) return;



                            double discount = unitDiscount;

                            double totalDiscount = Util.round(qty * discount,2);

                            double unitPrice = Double.parseDouble(tblProduct.getValueAt(rw, iUnitPrice).toString());
                            double totalPrice = qty * unitPrice;
                            totalPrice = Util.round(totalPrice,2);
                            double totalAfterDiscount= Util.round(totalPrice-totalDiscount,2);


                            tblProduct.setValueAt(qty, rw, iQty);//for qty
                            tblProduct.setValueAt(totalDiscount, rw, iDiscount);//for discount
                            tblProduct.setValueAt(totalPrice, rw, iTotalPrice);//for totalPrice
                            tblProduct.setValueAt(totalAfterDiscount, rw, iTotalAfterDiscount);//for total after discount

                            //get profit
                            double itemProfit = Double.parseDouble(tblProduct.getValueAt(rw, iProfit).toString());

                            double dTotalProfit = Util.round(Double.valueOf(Integer.valueOf(qty) * Double.valueOf(itemProfit)),2);
                            tblProduct.setValueAt(dTotalProfit, rw, iTotalProfit);//total profit

                            //Calculate total
                            cartTotal();
                            total();
                        }//available qty
                    }//row index
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        btnNewOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel dm = (DefaultTableModel) tblProduct.getModel();
                int rc = dm.getRowCount();

                if((rc>0)){
                    int response = JOptionPane.showConfirmDialog(null, "Are you sure want to create new order and clear/cancel this order?");
                    if(response==0) {
                        ClearOrder(dm);
                    }
                }
            }
        });
        txtBill.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    //getToolkit().beep();
                    e.consume();
                }
            }
        });
        txtPay.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    //getToolkit().beep();
                    e.consume();
                }
            }
        });



    }
    private void ClearOrder(DefaultTableModel dm) {
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();
        cboProduct.setSelectedIndex(0);

        cartTotal();
        total();
    }
    private void NewOrder(){
        DefaultTableModel dm = (DefaultTableModel) tblProduct.getModel();
        ClearOrder(dm);

        cboProduct.setSelectedIndex(0);
        txtInvoiceNo.setText(newInvoiceNo());
    }
    public JPanel getMainPanel(){
        return mainPanel;
    }



    private void dataLoad() {
        // load customer
        try {
            cboCustomer = new JComboBox();

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM customer");

            while (rs.next()) {
                cboCustomer.addItem(rs.getString("customer_name"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        // load Product
        try {

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM product where is_deleted=0");
            ArrayList<ComboKeyValue> myWords = new ArrayList<ComboKeyValue>();

            myWords.add(new ComboKeyValue("", "0"));


            while (rs.next()) {
                String word = String.format("%s %s %s # %s"
                        , rs.getString("Name")
                        , rs.getString("strength")
                        , rs.getString("formulation")
                        , rs.getString("formula")
                );

                myWords.add(new ComboKeyValue(word, rs.getString("product_id")));
            }

            cboProduct = new FilterComboBox(myWords);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    private String newInvoiceNo(){
        // load last invoice number
        try {

            Statement s = db.mycon().createStatement();
            //ResultSet rs = s.executeQuery("SELECT * FROM extra WHERE exid =1");
            ResultSet rs = s.executeQuery("select ifnull(max(INID),0)+1 as max_id from sales where is_deleted=0;");

            if (rs.next()) {
                //String invId = rs.getString("val");
                return rs.getString("max_id");
                //txtInvoiceNo.setText(invId);

                // pluss new invoice
                //int i = Integer.valueOf(invId);
                //i++;
                //txtInvoiceNo.setText(String.valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NumberFormatException("Invoice number is null");
        //return invId;
    }
    private void productTotalCal() {

        // product calculation
        if(txtQty.getText().length()>0) {
            Double qt = Double.valueOf(txtQty.getText());
            Double price = Util.round(Double.valueOf(txtItemSalePrice.getText()),2);
            Double discount = Util.round(Double.valueOf(txtItemDiscountValue.getText()),2);

            Double tot, totalDiscount;
            totalDiscount = Util.round(qt * discount,2);

            //tot = Util.round(qt * price-totalDiscount,2);
            tot = Util.round(qt * price,2);

            txtTotalItemQty.setText(String.valueOf(qt));
            txtTotalItemAmount.setText(String.valueOf(tot));
        }
    }
/*
    private void discountCalculate() {

        // product calculation
        if(txtDiscountValue.getText().length()>0) {
            Double disct = Double.valueOf(txtDiscountValue.getText());
            Double total = Double.valueOf(txtTotalAmount.getText());

            Double billAmt;

            billAmt = Util.round(total,2) - Util.round(disct,2);
            billAmt = Util.round(billAmt,0);
            txtBill.setText(String.valueOf(billAmt));

            //Balance
            Double bal = 0.0;
            Double pay = Util.round(Double.valueOf(txtPay.getText()),2);

            Double paidAmt = Double.valueOf(pay);
            bal = Util.round(paidAmt - total + disct, 2);
            txtBalance.setText(String.valueOf(bal));
        }
    }
*/

    private void cartTotal() {

        int rows = tblProduct.getRowCount();
        double dTotalPrice = 0, dTotalAfterDiscount=0, dTotalDiscount=0;
        Integer iTotalQty=0;
        for (int i = 0; i < rows; i++) {
            dTotalPrice += Double.valueOf(tblProduct.getValueAt(i, iTotalPrice).toString());

            dTotalDiscount += Double.valueOf(tblProduct.getValueAt(i, iDiscount).toString());

            dTotalAfterDiscount += Double.valueOf(tblProduct.getValueAt(i, iTotalAfterDiscount).toString());

            iTotalQty += Integer.valueOf(tblProduct.getValueAt(i, iQty).toString());
        }
        dTotalPrice = Util.round(dTotalPrice,2);
        txtTotalAmount.setText(Double.toString(dTotalPrice));

        dTotalDiscount = Util.round(dTotalDiscount,2);
        txtDiscountValue.setText(Double.toString(dTotalDiscount));

        dTotalAfterDiscount = Util.ceil(dTotalAfterDiscount);
        txtBill.setText(Double.toString(dTotalAfterDiscount));

        txtTotalQty.setText(Integer.toString(iTotalQty));
        txtPay.setText("0");
    }
    private void total() {
        try {

            Double paid = Util.round(Double.valueOf(txtPay.getText()), 2);
            Double tot = Util.round(Double.valueOf(txtTotalAmount.getText()), 2);
            Double dis = Util.round(Double.valueOf(txtDiscountValue.getText()), 2);
            Double due;

            due = Util.round(paid - tot + dis, 2);

            txtBalance.setText(String.valueOf(due));
        }catch (Exception e){

        }
    }
    private String lastSearchProductId="0";
    private void productSelection(){
        if(cboProduct.getSelectedItem()==null) return;

        String productId = ((ComboKeyValue) cboProduct.getSelectedItem()).getValue();

        //if someone is pressing backspace or searching same item
        if(lastSearchProductId.equals(productId)) return;

        //Set last search product id
        lastSearchProductId = productId;

        System.out.println(cboProduct.getSelectedItem());
        System.out.println("product_id="+productId);

        try {

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM product WHERE product_id ='" + productId + "'  ");
            if (rs.next()) {
                txtQty.setText("1");

                txtProductId.setText(rs.getString("product_id"));
                txtBarCode.setText(rs.getString("bar_code"));
                txtName.setText(rs.getString("name"));
                txtFormula.setText(rs.getString("formula"));
                txtFormulation.setText(rs.getString("formulation"));
                txtStrength.setText(rs.getString("strength"));
                txtPackSize.setText(rs.getString("pack_size"));
                txtPackPurchasePrice.setText(rs.getString("pack_purchase_price"));
                txtPackSalePrice.setText(rs.getString("pack_sale_price"));
                txtCompany.setText(rs.getString("company"));
                txtExpiry.setText(rs.getString("expiry"));

                txtItemStockQty.setText(rs.getString("item_stock_qty"));
                txtItemPurchasePrice.setText(rs.getString("item_purchase_price"));

                txtItemSalePrice.setText(rs.getString("item_sale_price"));
                txtItemDiscountPercent.setText(rs.getString("item_discount_percent"));
                txtItemDiscountValue.setText(rs.getString("item_discount_value"));
                txtItemPriceAfterDiscount.setText(rs.getString("item_price_after_discount"));
                txtItemProfit.setText(rs.getString("item_profit"));
            }
            productTotalCal();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    private boolean stockValidate(Integer qty){
        Integer itemStockQty = Integer.parseInt(txtItemStockQty.getText());
        if(qty>itemStockQty){
            Integer option = JOptionPane.showConfirmDialog(null, "Stock Qty is less then you want to order. Do you want to continue?");
            if (option == JOptionPane.NO_OPTION || option == JOptionPane.CANCEL_OPTION){
                return false;
            }
        }
        return true;
    }
    private void addToCart(){
        if(cboProduct.getSelectedItem()==null) return;

        //String item = cboProduct.getSelectedItem().toString();
        //if(findElementInList(item))return;

        ComboKeyValue selectedItem = (ComboKeyValue) cboProduct.getSelectedItem();

        String productId = selectedItem.getValue();
        if(productId.equals("0")) return;

        String productName = selectedItem.getKey();
        //remove | sign and use proper name
        //Product name - strength - formulation
        String[] str = productName.split("#");
        productName = str[0].trim();
        System.out.println("Product name:"+productName);


        //Check if item already added in cart then ignore it
        if(findElementInList(productName))return;

        Integer qty = Integer.parseInt(txtQty.getText());
        if(qty >=1) {

            //Stock validation
            if (stockValidate(qty) == false) return;


            DefaultTableModel dt = (DefaultTableModel) tblProduct.getModel();

            Vector v = new Vector();

            v.add(txtProductId.getText()); // product id
            v.add(productName); // product name
            v.add(txtQty.getText()); // p qyt

            double dTotalDiscount = Double.valueOf(Integer.valueOf(txtQty.getText()) * Double.valueOf(txtItemDiscountValue.getText()));
            dTotalDiscount = Util.round(dTotalDiscount, 2);
            double dTotalAfterDiscount = Double.valueOf(txtTotalItemAmount.getText()) - dTotalDiscount;
            dTotalAfterDiscount = Util.round(dTotalAfterDiscount, 2);

            v.add(txtItemSalePrice.getText()); // unit price
            v.add(txtTotalItemAmount.getText()); // get totle price
            v.add(dTotalDiscount); // Discount
            v.add(dTotalAfterDiscount); // Total  after Discount

            double txtItemProfitValue=1;

            if (txtItemProfit.getText() !=null) {
                txtItemProfitValue = Double.valueOf(txtItemProfit.getText());

            }
            double dTotalProfit = Util.round(Double.valueOf(Integer.valueOf(txtQty.getText()) * txtItemProfitValue),2);

            v.add(txtItemProfitValue); // profit
            v.add(dTotalProfit); // unit price


            dt.addRow(v);

            cartTotal();
            total();
        }else{
            JOptionPane.showMessageDialog(null,"Qty must be greater then 0.");
        }
    }
    private boolean findElementInList(String item){
        boolean found=false;

        DefaultTableModel dt = (DefaultTableModel) tblProduct.getModel();
        int rc = dt.getRowCount();

        for (int i = 0; i < rc; i++) {
            String product_name = dt.getValueAt(i, 1).toString(); // get product name
            if(product_name.equals(item)){
                found=true;
                break;
            }
        }
        return found;
    }

    private void save() {
        try {
            //Pay amount greater then 0
            Integer payAmount = Integer.parseInt(txtPay.getText());
            if(payAmount<=0) {
                JOptionPane.showMessageDialog(null, "Pay amount greater then 0");
                txtPay.setFocusable(true);
                return;
            }

            DefaultTableModel dt = (DefaultTableModel) tblProduct.getModel();
            int rc = dt.getRowCount();

            //Get latest invoice number
            String inid = newInvoiceNo();
            //String inid = txtInvoiceNo.getText();
            Double dGrandTotalProfit=0D;

            for (int i = 0; i < rc; i++) {
                String product_id = dt.getValueAt(i, iProductId).toString(); // get product id
                String product_name = dt.getValueAt(i, iProductName).toString(); // get product name
                String qty = dt.getValueAt(i, iQty).toString(); // get product qty
                String unit_price = dt.getValueAt(i, iUnitPrice).toString(); // get product unit price
                String total_price = dt.getValueAt(i, iTotalPrice).toString(); // get product total Price
                String discount = dt.getValueAt(i, iDiscount).toString(); // get product discount
                String total_after_discount = dt.getValueAt(i, iTotalAfterDiscount).toString(); // get product total after discount

                String item_profit = dt.getValueAt(i, iProfit).toString(); // get product total after discount
                String total_profit = dt.getValueAt(i, iTotalProfit).toString(); // get product total after discount

                dGrandTotalProfit += Double.parseDouble(total_profit);

                // cart DB
                Statement s = db.mycon().createStatement();
                s.executeUpdate(" INSERT INTO cart (INID, product_id, product_name, qty, unit_price, total_price, discount, total_after_discount, profit, total_profit) " +
                        "VALUES ('" + inid + "','" + product_id + "','" + product_name + "','" + qty + "','" + unit_price + "','" + total_price + "','" + discount + "','" + total_after_discount + "','" + item_profit + "','" + total_profit + "') ");

                s.executeUpdate("Update Product set item_stock_qty=item_stock_qty-"+qty+" where product_id="+product_id);
                s.close();
            }

            // sales DB

            //`saleid`, `INID`, `Cid`, `Customer_Name`, `Total_Qty`, `Total_Bill`, `Status`, `Balance`
            //String inv_id = txtInvoiceNo.getText();
            String customer_name = "Test";//cboCustomer.getSelectedItem().toString();
            String total_qty = txtTotalQty.getText();
            String total_bill = txtBill.getText();
            String total_paid = txtPay.getText();
            String total_discount = txtDiscountValue.getText();


            String balance = txtBalance.getText();
            String created_by_user = username;

            // paid check

            Double tot = Double.valueOf(txtBill.getText());
            Double discount = Double.valueOf(txtDiscountValue.getText());

            Double total_amount = Util.round(tot+discount,2);
            Double pid = Double.valueOf(txtPay.getText());


            String status = null;
            if (pid.equals(0.0)) {
                status = "UnPaid";
            } else if (tot > pid) {
                status = "Partial";

            } else if (tot <= pid) {
                status = "Paid";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String order_date =formatter.format(date);

            Statement ss = db.mycon().createStatement();
            ss.executeUpdate("INSERT INTO sales(INID, order_date, created_by_user, total_qty, total_amount, total_bill, total_paid, total_discount, status, balance, total_profit) " +
                    "VALUES('" + inid + "', '"+ order_date +"', '"+ created_by_user +"','" + total_qty + "','" + total_amount + "','" + total_bill + "','" + total_paid + "','" + total_discount + "','" + status + "','" + balance + "','" + dGrandTotalProfit + "')");

            ss.close();

            JOptionPane.showMessageDialog(null, "Data has been saved successfully.");
            Util.preview(inid);

            NewOrder();
        } catch (NumberFormatException | SQLException e) {
            System.out.println(e);
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        dataLoad();
    }
}
