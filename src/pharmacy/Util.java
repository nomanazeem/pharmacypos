package pharmacy;

import java.util.HashMap;

public class Util {
    public static void preview(String invoiceNo) {
        // Print or view ireport bill
        try {
            HashMap para = new HashMap();

            para.put("invid", invoiceNo);  // inv_id  is ireport parameter name

            ReportView r = new ReportView("/reports/Invoice_1.jasper", para);
            r.setVisible(true);
        } catch (Exception e) {
        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public static double floor(double value) {
        return Math.floor(value);
    }
    public static double ceil(double value) {
        return Math.ceil(value);
    }
}
