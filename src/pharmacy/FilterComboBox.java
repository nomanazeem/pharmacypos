package pharmacy;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FilterComboBox extends JComboBox {
    //private List<String> array;
    private List<ComboKeyValue> array;

    /*public FilterComboBox() {

    }*/
    //public FilterComboBox(List<String> array) {
    public FilterComboBox(List<ComboKeyValue> array) {
        super(array.toArray());
        this.array = array;
        this.setEditable(true);
        final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        comboFilter(textfield.getText());
                    }
                });
            }
        });

    }

    public void comboFilter(String enteredText) {
        if (!this.isPopupVisible()) {
            this.showPopup();
        }

        List<ComboKeyValue> filterArray= new ArrayList<ComboKeyValue>();
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getKey().toLowerCase().contains(enteredText.toLowerCase())) {
                filterArray.add(array.get(i));
            }
        }
        if (filterArray.size() > 0) {
            DefaultComboBoxModel model = (DefaultComboBoxModel) this.getModel();
            model.removeAllElements();
            for (ComboKeyValue s: filterArray)
                model.addElement(s);

            JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
            textfield.setText(enteredText);
        }
    }

    /* Testing Codes */
    /*public static List<ComboKeyValue> populateArray() {
        List<ComboKeyValue> test = new ArrayList<ComboKeyValue>();
        test.add(new ComboKeyValue("", "0"));
        test.add(new ComboKeyValue("Mountain Flight", "1"));
        test.add(new ComboKeyValue("Mount Climbing", "2"));
        test.add(new ComboKeyValue("Trekking", "3"));
        test.add(new ComboKeyValue("Rafting", "4"));
        test.add(new ComboKeyValue("Jungle Safari", "5"));
        test.add(new ComboKeyValue("Bungie Jumping", "6"));
        test.add(new ComboKeyValue("Para Gliding", "7"));
        return test;
    }

    public static void makeUI() {
        JFrame frame = new JFrame("Adventure in Nepal - Combo Filter Test");
        FilterComboBox acb = new FilterComboBox(populateArray());
        frame.getContentPane().add(acb);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
*/
    /*public static void main(String[] args) throws Exception {

        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        makeUI();
    }*/

}