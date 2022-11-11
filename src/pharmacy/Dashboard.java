package pharmacy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard {
    private JButton btnShop;
    private JPanel mainPanel;
    private JButton btnUser;
    private JButton btnCompany;
    private JButton btnProduct;
    private JButton btnSaleOrder;
    private JButton btnReport;
    private JPanel contentPanel;
    private JLabel lblBackground;
    private JButton btnLogout;
    private JButton btnTheme;
    private JButton btnSalesReport;

    private Integer userId;
    private String username;
    private String role;

    private Integer width;
    private Integer height;
    JFrame jDashboardFrame;
    boolean isDarkTheme;

    public Dashboard(JFrame _jDashboardFrame, boolean _isDarkTheme, Integer _userId, String _username, String _role, Integer _width, Integer _height) {
        //Logged-in user id
        this.jDashboardFrame=_jDashboardFrame;
        this.userId = _userId;
        this.username = _username;
        this.role = _role;
        this.width = _width;
        this.height = _height;
        this.isDarkTheme=_isDarkTheme;


        if(!role.equals("Admin")) {
            btnReport.setEnabled(false);
        }

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/pharmacy/img/img-medicine.jpg"))
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH));
        lblBackground.setIcon(imageIcon);

        btnShop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shop shop2 = new Shop();
                contentPanel.removeAll();
                contentPanel.setLayout(new BorderLayout());
                contentPanel.setSize(new Dimension(350, 200));

                contentPanel.add(shop2.getMainPanel());
                contentPanel.repaint();
                contentPanel.revalidate();
            }
        });
        btnUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user2 = new User();
                contentPanel.removeAll();
                contentPanel.setLayout(new BorderLayout());
                contentPanel.setSize(new Dimension(800, 500));

                contentPanel.add(user2.getMainPanel());

                contentPanel.repaint();
                contentPanel.revalidate();
            }
        });
        btnCompany.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Company company2 = new Company();
                contentPanel.removeAll();
                contentPanel.setLayout(new BorderLayout());
                contentPanel.setSize(new Dimension(800, 500));

                contentPanel.add(company2.getMainPanel());

                //contentPanel.validate();
                contentPanel.repaint();
                contentPanel.revalidate();
            }
        });
        btnProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Product product2 = new Product();
                contentPanel.removeAll();
                contentPanel.setLayout(new BorderLayout());
                contentPanel.setSize(new Dimension(800, 500));

                contentPanel.add(product2.getMainPanel());

                contentPanel.repaint();
                contentPanel.revalidate();
            }
        });
        btnSaleOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SaleOrder saleOrder2 = new SaleOrder(userId, username);
                contentPanel.removeAll();
                contentPanel.setSize(new Dimension(800, 500));
                contentPanel.setLayout(new BorderLayout());

                contentPanel.add(saleOrder2.getMainPanel());
                contentPanel.repaint();
                contentPanel.revalidate();
            }
        });

        btnReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reports reports = new Reports(role);
                contentPanel.removeAll();
                contentPanel.setSize(new Dimension(900, 500));
                contentPanel.setLayout(new BorderLayout());

                contentPanel.add(reports.getMainPanel());
                contentPanel.repaint();
                contentPanel.revalidate();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure want to logout?");
                if (response == 0) {
                    showLogin();
                }
            }
        });
        btnTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Theme theme = new Theme(jDashboardFrame);
                contentPanel.removeAll();
                contentPanel.setLayout(new BorderLayout());
                contentPanel.setSize(new Dimension(800, 500));

                contentPanel.add(theme.getMainPanel());

                //contentPanel.validate();
                contentPanel.repaint();
                contentPanel.revalidate();

                /*if (isDarkTheme) {
                    lightTheme();
                    isDarkTheme=false;
                    JOptionPane.showMessageDialog(null, "Light theme has been setup successfully....");
                }
                else {
                    darkTheme();
                    isDarkTheme=true;
                    JOptionPane.showMessageDialog(null, "Dark theme has been setup successfully....");
                }*/
            }
        });
    }


    private void showLogin() {
        Login.showLogin();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /*public static void main(String args[]) {

        JFrame jFrame = new JFrame("Dashboard");
        jFrame.setTitle("POS Pharmacy System");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int width=850;//(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-1);
        int height=650;//(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50);

        jFrame.setSize( width, height);
        jFrame.setMaximizedBounds(new Rectangle(0,0 , 500, 500));
        jFrame.setLayout(new BorderLayout());


        Dashboard2 dashboard2 = new Dashboard2(0);

        jFrame.add(dashboard2.getMainPanel());
        //----- frame end ------//

        jFrame.setVisible(true);
    }*/
}
