package pharmacy;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Login {
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnClose;
    private JButton btnLogin;
    private JLabel lblError;
    private JLabel lblVersion;
    private boolean isDarkTheme;

    public Login() {
        isDarkTheme=true;
        //should be removed later


        txtUsername.setText("admin");
        txtPassword.setText("123456");


        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin2();
            }
        });
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jLoginFrame.dispose();
            }
        });
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin2();
            }
        });
        txtUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin2();
            }
        });
    }
    private void doLogin2(){
        LoginParam param = doLogin();
        if(param.getSuccess()) {
            showDashboard(param.getUserId(), param.getUserName(), param.getRole());
        }else {
            lblError.setVisible(true);
        }
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }

    static JFrame jDashboardFrame;
    private void showDashboard(Integer userId, String username, String role){
        jLoginFrame.setVisible(false);

        jDashboardFrame = new JFrame("Dashboard");
        jDashboardFrame.setTitle("POS Pharmacy System");
        jDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        int width=(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-1);//1200
        int height=(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50);//700

        //jDashboardFrame.setSize( width, height);
        //jFrame.setMaximizedBounds(new Rectangle(0,0 , width, height));
        jDashboardFrame.setLayout(new BorderLayout());

        Dashboard dashboard2 = new Dashboard(jDashboardFrame, isDarkTheme, userId, username, role, width, height);

        jDashboardFrame.add(dashboard2.getMainPanel());
        //----- frame end ------//

        //center location
        /*Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - jDashboardFrame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - jDashboardFrame.getHeight()) / 2);*/

        jDashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //jDashboardFrame.setLocation(0, 0);
        jDashboardFrame.setVisible(true);
        //jDashboardFrame.setUndecorated(true);
    }

    /*public static void darkTheme(){
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
    }*/

    public static void defaultTheme(){
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize default theme" );
        }
    }

    static JFrame jLoginFrame;
    public static void showLogin(){
        defaultTheme();

        if(jDashboardFrame != null){
            jDashboardFrame.setVisible(false);
        }

        int width = 570;//(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-1);
        int height = 260;//(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50);

        Login login = new Login();

        jLoginFrame = new JFrame("Login");
        jLoginFrame.setTitle("POS Pharmacy System");
        jLoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jLoginFrame.setUndecorated(true);
        jLoginFrame.setSize(width, height);
        jLoginFrame.setMaximizedBounds(new Rectangle(0, 0, width, height));
        jLoginFrame.setLayout(new BorderLayout());

        jLoginFrame.add(login.mainPanel);

        //center location
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - jLoginFrame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - jLoginFrame.getHeight()) / 2);
        jLoginFrame.setLocation(x, y);
        jLoginFrame.setVisible(true);
    }

    private static Logger logger = Logger.getLogger(Login.class.getName());
    public static void main(String args[]) {
        FileHandler fh;

        try {
            // This block configure the logger with handler and formatter
            //fh = new FileHandler("C:/Pharmacy.log");
            //logger.addHandler(fh);
            //SimpleFormatter formatter = new SimpleFormatter();
            //fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("My first log");

            showLogin();

        }catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println("x="+x+",y="+y);
        //----- frame end ------//
    }



    private boolean validateData() {
        boolean isValid = true;
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.equals("")|| password.equals("")) {
            isValid = false;
        }

        return isValid;
    }
    public LoginParam doLogin() {
        try {
//            if (!validateData()) {
//                JOptionPane.showMessageDialog(null, "Please fill out username and password.");
//                return new LoginParam(false, 0);
//            }

            String username = txtUsername.getText();
            String password = String.valueOf(txtPassword.getPassword());

            Statement s = db.mycon().createStatement();
            String sql = "SELECT user_id, role, name, username, password, phone " +
                    "FROM User " +
                    "where 1=1 " +
                    "AND is_deleted=0 " +
                    "AND (username='"+ username +"' AND password='"+ password +"');";

            ResultSet rs = s.executeQuery(sql);

            //System.out.println(sql);

            if (rs.next()) {
                System.out.println("found....");
                return new LoginParam(true
                        , Integer.parseInt(rs.getString("user_id"))
                        , rs.getString("username")
                        , rs.getString("role")
                );
            }else {
                return new LoginParam(false, 0,"", "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.toString());
            return new LoginParam(false, 0,"", "");
        }
    }
}
