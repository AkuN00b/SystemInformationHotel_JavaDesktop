package Login;

import Dashboard.DashboardCustomer;
import Dashboard.DashboardResepsionis;
import Database.DBConnect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormLogin extends JFrame {
    private JPanel MainPanel;
    private JPanel StartPanel;
    private JButton btnLogin;
    private JTextField tbEmail;
    private JPasswordField tbPassword;
    private JButton btnClear;
    private JButton btnExit;
    private JCheckBox cbVisible;

    public static void main (String[] args) {
        new SI_Hotel();
        new FormLogin();
    }

    public String[] validasi() {
        if (tbEmail.getText().isEmpty() || tbPassword.getText().isEmpty()) {
            JOptionPane.showMessageDialog(MainPanel, "Email / Password Kosong !", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                DBConnect connection = new DBConnect();
                connection.stat = connection.conn.createStatement();

                String query = "EXEC sp_Login @email_user=?, @password=? ";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, tbEmail.getText());
                connection.pstat.setString(2, tbPassword.getText());
                connection.result = connection.pstat.executeQuery();

                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(tbEmail.getText());

                if (!matcher.matches()) {
                    JOptionPane.showMessageDialog(MainPanel, "Email Tidak Sesuai Format !", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else if (tbPassword.getText().length() < 7) {
                    JOptionPane.showMessageDialog(MainPanel, "Password anda kurang dari 8 karakter !", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else if (!connection.result.next()) {
                    JOptionPane.showMessageDialog(MainPanel, "User Tidak Ditemukan !", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }

                String id_user = connection.result.getString(1);
                String nama_user = connection.result.getString(2);
                String email_user = connection.result.getString(3);
                String password = connection.result.getString(4);
                String id_role = connection.result.getString(5);

                return new String[] {"true", id_user, nama_user, email_user, password, id_role};
            } catch (Exception ex) {
                // System.out.println(ex.getMessage());
                // JOptionPane.showMessageDialog(MainPanel,ex.getMessage() + " !","Peringatan",JOptionPane.WARNING_MESSAGE);
            }
        }

        return new String[] {"false"};
    }

    public FormLogin() {
        add(MainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        btnLogin.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] value = validasi();
                Boolean valid = Boolean.parseBoolean(value[0]);

                if (valid) {
                    if(value[5].equals("1")) {
                        JOptionPane.showMessageDialog(MainPanel,"Selamat Datang di Sistem Informasi Hotel","Information",JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        Dashboard.DashboardManager form = new Dashboard.DashboardManager(value);
                        form.setVisible(true);
                    }else if(value[5].equals("2")) {
                        JOptionPane.showMessageDialog(MainPanel,"Selamat Datang di Sistem Informasi Hotel","Information",JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        DashboardResepsionis form = new DashboardResepsionis(value);
                        form.setVisible(true);
                    }else if(value[5].equals("3")) {
                        JOptionPane.showMessageDialog(MainPanel,"Selamat Datang di Sistem Informasi Hotel","Information",JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        Dashboard.DashboardCustomer form = new Dashboard.DashboardCustomer(value);
                        form.setVisible(true);
                    } else {
                        dispose();
                        Dashboard.CantAccessPage form = new Dashboard.CantAccessPage();
                        form.setVisible(true);
                    }
                }
            }
        });

        cbVisible.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbVisible.isSelected() == true) {
                    tbPassword.setEchoChar('\0');
                } else {
                    tbPassword.setEchoChar('*');
                }
            }
        });
    }

    public void clear() {
        tbEmail.setText(null);
        tbPassword.setText(null);
    }
}
