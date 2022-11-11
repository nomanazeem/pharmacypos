package pharmacy;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Theme {
    private JPanel mainPanel;
    private JButton btnBlackTheme;
    private JButton btnWhiteTheme;
    private JButton btnDefaultTheme;
    private JButton btnWindowsTheme;

    private JFrame jDashboardFrame;
    public Theme(JFrame _jDashboardFrame) {
        this.jDashboardFrame=_jDashboardFrame;

        btnBlackTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                darkTheme();
            }
        });
        btnWhiteTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTheme();
            }
        });
        btnDefaultTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultTheme();
            }
        });
        btnWindowsTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowsTheme();
            }
        });
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void windowsTheme(){
        try {
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

            SwingUtilities.updateComponentTreeUI(this.jDashboardFrame);
            this.jDashboardFrame.repaint();
            jDashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
    }

    public void defaultTheme(){
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            SwingUtilities.updateComponentTreeUI(this.jDashboardFrame);
            this.jDashboardFrame.repaint();
            jDashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
    }

    public void darkTheme(){
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
            SwingUtilities.updateComponentTreeUI(this.jDashboardFrame);
            this.jDashboardFrame.repaint();
            jDashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            //JOptionPane.showMessageDialog(null, "Dark theme has been setup successfully....");

        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
    }
    public void lightTheme(){
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
            SwingUtilities.updateComponentTreeUI(this.jDashboardFrame);
            this.jDashboardFrame.repaint();
            jDashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            //JOptionPane.showMessageDialog(null, "Light theme has been setup successfully....");

        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
    }

}
