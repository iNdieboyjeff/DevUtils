import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.event.*;
import java.util.Map;

public class APICall extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private RTextScrollPane RTextScrollPane1;
    private RSyntaxTextArea RSyntaxTextArea1;
    private JComboBox comboBox1;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea RTextArea1;
    Map<String, String> items;

    public APICall(Map<String, String> items) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.items = items;
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });


// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        RTextScrollPane1.setViewportView(RSyntaxTextArea1);
        RTextScrollPane1.setFoldIndicatorEnabled(true);
        RTextScrollPane1.setLineNumbersEnabled(true);

        comboBox1.setModel(new DefaultComboBoxModel<>(items.keySet().toArray()));
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    selectItem(item.toString());
                }
            }
        });
        comboBox1.setSelectedIndex(0);
        selectItem(comboBox1.getSelectedItem().toString());
    }

    private void selectItem(String item) {
        String code = items.get(item.toString());
        RSyntaxTextArea1.setText(code);

        if (item.toString().equalsIgnoreCase("Java")) {
            RSyntaxTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        } else  if (item.toString().equalsIgnoreCase("PHP")) {
            RSyntaxTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP);
        } else  if (item.toString().equalsIgnoreCase("Bash")) {
            RSyntaxTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        } else  if (item.toString().equalsIgnoreCase("Javascript")) {
            RSyntaxTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        }else  if (item.toString().equalsIgnoreCase("jQuery")) {
            RSyntaxTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        }
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
