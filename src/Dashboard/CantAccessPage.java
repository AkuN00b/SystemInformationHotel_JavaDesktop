package Dashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CantAccessPage extends JFrame {
    private JPanel MainPanel;
    private JButton kembaliButton;

    public CantAccessPage() {
        add(this.MainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login.FormLogin form = new Login.FormLogin();
                form.setVisible(true);
            }
        });
    }
}
