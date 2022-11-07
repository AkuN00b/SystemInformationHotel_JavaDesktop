package Login;

import javax.swing.*;

public class SI_Hotel extends JFrame {
    private JPanel MainPanel;
    private JPanel StartPanel;
    private JProgressBar progressBar1;
    private JLabel message;

    public SI_Hotel() {
        add(MainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        progressBar();
    }

    public void progressBar() {
        int i = 0;

        while(i <= 100) {
            try {
                Thread.sleep(40);
                progressBar1.setValue(i);
                message.setText("LOADING....(" + (i) + "%)");   //Setting text of the message JLabel
                i++;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        dispose();
    }
}
