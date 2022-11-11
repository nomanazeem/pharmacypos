/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pharmacy;

/**
 *
 * @author wow
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.awt.Container;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import javax.swing.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;


public class ReportView extends JFrame
{
    public ReportView(String fileName, HashMap para)
    {
        super("www.CreativeTechHouse.com (Report Viewer)");

        db d = new db();
        Connection con = d.mycon();

        try
        {
            /*InputStream file = getClass().getResourceAsStream(fileName);
            JasperDesign jasperDesign = JRXmlLoader.load(file);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);*/
            InputStream file = getClass().getResourceAsStream(fileName);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, para, con);

            JRViewer viewer = new JRViewer(print);
            Container c = getContentPane();
            c.add(viewer);            
        } 
        catch (JRException jRException)
        {
            System.out.println(jRException);
        }
        setBounds(2, 2, 500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
