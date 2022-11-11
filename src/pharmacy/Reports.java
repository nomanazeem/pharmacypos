package pharmacy;

import com.toedter.calendar.JDateChooser;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Reports {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JButton btnSearchByDate;
    private JTextField txtInvoiceNo;
    private JPanel pnlSales;
    private JPanel pnlInvoice;
    private JButton btnSearchByInvoice;
    private JPanel pnlFrom;
    private JPanel pnlTo;
    private JButton btnSearchExpiry;
    private JTextField txtDaysLeft;
    private JPanel pnlExpiryReport;
    private JButton btnSearchByDate2;
    private JPanel pnlFrom2;
    private JPanel pnlTo2;
    private JPanel pnlMostSalesItems;
    private JButton btnSearchDaywiseSale;
    private JPanel pnlFromDaywise;
    private JPanel pnlToDaywise;
    private JPanel pnlDaywise;
    private JComboBox cboProduct;
    private JComboBox cboProduct1;
    private JTextField txtStockQuantity;
    private JButton btnStockSearch;
    private JPanel pnlStockReport;
    private JButton btnMedicineListSearch;
    private JTextField txtSearchMedicine;
    private JPanel pnlMedicineList;
    private JComboBox cboCompany;
    private JComboBox cboFormation;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    JDateChooser dateChooser1;
    JDateChooser dateChooser2;

    JDateChooser dateChooser3;
    JDateChooser dateChooser4;

    JDateChooser dateChooser5;
    JDateChooser dateChooser6;

    private String role;

    public Reports(String role) {
        this.role=role;
        init();

        btnSearchByInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String invoiceNo = txtInvoiceNo.getText();

                HashMap para = new HashMap();

                para.put("invid", invoiceNo);  // inv_id  is ireport parameter name

                String fileName="/reports/Invoice_1.jasper";

                showReport(pnlInvoice, fileName, para);
            }
        });
        btnSearchByDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap para = new HashMap();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fromDate = sdf.format(dateChooser1.getDate());
                String toDate = sdf.format(dateChooser2.getDate());

                String productId = ((ComboKeyValue) cboProduct1.getSelectedItem()).getValue();

                para.put("pFromDate", fromDate);
                para.put("pToDate", toDate);
                para.put("pProduct", productId);


                System.out.println("pFromDate="+fromDate);
                System.out.println("pToDate="+toDate);
                System.out.println("pProduct="+productId);



                String fileName="/reports/SaleReport.jasper";

                showReport(pnlSales, fileName, para);
            }
        });
        btnSearchExpiry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String daysLeft = txtDaysLeft.getText();

                HashMap para = new HashMap();

                para.put("DaysLeft", daysLeft);  // inv_id  is ireport parameter name

                String fileName="/reports/ExpiryReport.jasper";

                showReport(pnlExpiryReport, fileName, para);
            }
        });
        txtDaysLeft.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });
        btnSearchByDate2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap para = new HashMap();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fromDate = sdf.format(dateChooser3.getDate());
                String toDate = sdf.format(dateChooser4.getDate());

                String productId = ((ComboKeyValue) cboProduct.getSelectedItem()).getValue();

                para.put("pFromDate", fromDate);
                para.put("pToDate", toDate);
                para.put("pProduct", productId);


                System.out.println("pFromDate="+fromDate);
                System.out.println("pToDate="+toDate);
                System.out.println("pProduct="+productId);

                String fileName="/reports/MostSaleItems.jasper";
                //String fileName="/reports/SaleReport.jasper";

                showReport(pnlMostSalesItems, fileName, para);
            }
        });
        btnSearchDaywiseSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap para = new HashMap();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fromDate = sdf.format(dateChooser5.getDate());
                String toDate = sdf.format(dateChooser6.getDate());

                para.put("pFromDate", fromDate);
                para.put("pToDate", toDate);


                System.out.println("pFromDate="+fromDate);
                System.out.println("pToDate="+toDate);

                String fileName="/reports/DailySaleReport.jasper";

                showReport(pnlDaywise, fileName, para);
            }
        });
        btnStockSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stockQuantity = txtStockQuantity.getText();

                HashMap para = new HashMap();

                para.put("StockQuantity", stockQuantity);  // inv_id  is ireport parameter name

                String fileName="/reports/StockReport.jasper";

                showReport(pnlStockReport, fileName, para);
            }
        });
        btnMedicineListSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchMedicineText = txtSearchMedicine.getText();
                String filter=" 1=1 ";


                HashMap para = new HashMap();

                String companyId = ((ComboKeyValue) cboCompany.getSelectedItem()).getValue();
                String formulationId = ((ComboKeyValue) cboFormation.getSelectedItem()).getValue();

                String companyName = "";
                String formulationName = "";

                if(!(searchMedicineText.equals(""))){
                    filter += " and (product_id = '" + searchMedicineText +"' or name like '%"+searchMedicineText +"%' or formula like '%"+searchMedicineText +"%') ";
                }

                if(!(companyId.equals("0"))){
                    companyName = ((ComboKeyValue) cboCompany.getSelectedItem()).getKey();

                    filter += " and company = '"+companyName +"' ";
                }
                if(!(formulationId.equals("0"))) {
                    formulationName = ((ComboKeyValue) cboFormation.getSelectedItem()).getKey();

                    filter += " and formulation = '"+formulationName +"' ";
                }


                /*filter = "product_id = '" + searchMedicineText +"'" +
                                " or name like '%"+searchMedicineText +"%' " +
                                " or formula like '%"+searchMedicineText +"%' " +
                                " or company = '"+companyName +"' " +
                                " or formulation = '"+formulationName +"' ";*/

                System.out.println(filter);

                para.put("pMedicineName", filter);  // inv_id  is ireport parameter name

                String fileName="/reports/MedicineList.jasper";

                showReport(pnlMedicineList, fileName, para);
            }
        });
    }
    private void fillCompany() {
        try {

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT company_id, name FROM company where is_deleted=0");

            cboCompany.removeAllItems();

            cboCompany.addItem(new ComboKeyValue("------------ All -----------", "0"));

            cboCompany.setSelectedIndex(0);

            while (rs.next()) {
                String word = String.format("%s"
                        , rs.getString("Name")
                );
                cboCompany.addItem(new ComboKeyValue(word, rs.getString(1)));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    private void fillFormulation() {
        try {

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT formulation_id, name FROM formulation where is_deleted=0");

            cboFormation.removeAllItems();

            cboFormation.addItem(new ComboKeyValue("------------ All -----------", "0"));

            cboFormation.setSelectedIndex(0);

            while (rs.next()) {
                String word = String.format("%s"
                        , rs.getString("Name")
                );
                cboFormation.addItem(new ComboKeyValue(word, rs.getString(1)));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void init() {
        System.out.println("role="+role);
        Date date = new Date();

        //Sales Report - Search by date
        dateChooser1 = new JDateChooser();
        dateChooser1.setDateFormatString("dd-MMM-yyyy");
        dateChooser1.setDate(date);
        pnlFrom.add(dateChooser1);

        dateChooser2 = new JDateChooser();
        dateChooser2.setDateFormatString("dd-MMM-yyyy");
        dateChooser2.setDate(date);
        pnlTo.add(dateChooser2);


        //Most Sales Items
        dateChooser3 = new JDateChooser();
        dateChooser3.setDateFormatString("dd-MMM-yyyy");
        dateChooser3.setDate(date);
        pnlFrom2.add(dateChooser3);

        dateChooser4 = new JDateChooser();
        dateChooser4.setDateFormatString("dd-MMM-yyyy");
        dateChooser4.setDate(date);
        pnlTo2.add(dateChooser4);


        //Daywise Sales
        dateChooser5 = new JDateChooser();
        dateChooser5.setDateFormatString("dd-MMM-yyyy");
        dateChooser5.setDate(date);
        pnlFromDaywise.add(dateChooser5);

        dateChooser6 = new JDateChooser();
        dateChooser6.setDateFormatString("dd-MMM-yyyy");
        dateChooser6.setDate(date);
        pnlToDaywise.add(dateChooser6);


        fillProduct();
        fillCompany();
        fillFormulation();
    }

    private void showReport(JPanel panel, String fileName, HashMap para){
        try
        {
            db d = new db();
            Connection con = d.mycon();

            /*InputStream file = getClass().getResourceAsStream(fileName);
            JasperDesign jasperDesign = JRXmlLoader.load(file);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);*/
            InputStream file = getClass().getResourceAsStream(fileName);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, para, con);

            JRViewer viewer = new JRViewer(print);
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.repaint();
            panel.add(viewer);
            panel.revalidate();
        }
        catch (NullPointerException nullPointerException)
        {
            //nullPointerException.printStackTrace();
            JOptionPane.showMessageDialog(null, "No data found.");
        }
        catch (Exception jRException)
        {
            jRException.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was some error for fetching data. Try again.");
        }
    }


    private void fillProduct() {
        try {

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT product_id, name, strength, formulation, formula FROM product where is_deleted=0");

            cboProduct.removeAllItems();
            cboProduct1.removeAllItems();

            cboProduct.addItem(new ComboKeyValue("------------ All -----------", "0"));
            cboProduct1.addItem(new ComboKeyValue("------------ All -----------", "0"));

            cboProduct.setSelectedIndex(0);
            cboProduct1.setSelectedIndex(0);

            while (rs.next()) {
                String word = String.format("%s %s %s # %s"
                        , rs.getString("Name")
                        , rs.getString("strength")
                        , rs.getString("formulation")
                        , rs.getString("formula")
                );
                cboProduct.addItem(new ComboKeyValue(word, rs.getString(1)));
                cboProduct1.addItem(new ComboKeyValue(word, rs.getString(1)));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
