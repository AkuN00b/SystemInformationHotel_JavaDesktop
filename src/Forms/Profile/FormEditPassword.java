package Forms.Profile;

import Database.DBConnect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEditPassword {
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JPasswordField tbPasswordBaruu;
    private JPanel Panel_Tombol;
    private JButton btnCancel;
    private JButton btnUpdate;
    public JPanel MainPanel;
    private JPasswordField tbPasswordLama;
    private JPasswordField tbPasswordBaru;
    private JCheckBox cbVisible;

    DBConnect connection = new DBConnect();

    String password;

    String passwordLama, passwordBaru, passwordBaruu;

    public FormEditPassword(String[] data) {
        clear(data);

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordLama = tbPasswordLama.getText();
                passwordBaru = tbPasswordBaru.getText();
                passwordBaruu = tbPasswordBaruu.getText();

                if (tbPasswordLama.getText().length() < 7 || tbPasswordBaru.getText().length() < 7 || tbPasswordBaruu.getText().length() < 7) {
                    JOptionPane.showMessageDialog(MainPanel, "Password anda kurang dari 8 karakter !", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (passwordLama.isEmpty() || passwordBaru.isEmpty() || passwordBaruu.isEmpty()){
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            if (password.equals(passwordLama)) {
                                if (passwordBaru.equals(passwordBaruu)) {
                                    try {
                                        String query = "EXEC sp_UpdatePassword @id=?, @password=?";
                                        connection.pstat = connection.conn.prepareStatement(query);
                                        connection.pstat.setString(1, data[1]);
                                        connection.pstat.setString(2, passwordBaruu);

                                        connection.pstat.executeUpdate();
                                        connection.pstat.close();
                                    } catch (Exception ex) {
                                        System.out.println("Error saat update data ke database: " + ex);
                                    }

                                    data[4] = passwordBaruu;

                                    clear(data);
                                    JOptionPane.showMessageDialog(null, "Update Password berhasil");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Password baru anda tidak sesuai dengan verifikasi password baru !");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Password anda tidak sesuai, dengan password sebelumnya !");
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tbPasswordLama.setText(null);
                tbPasswordBaru.setText(null);
                tbPasswordBaruu.setText(null);
            }
        });
        cbVisible.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbVisible.isSelected() == true) {
                    tbPasswordLama.setEchoChar('\0');
                    tbPasswordBaru.setEchoChar('\0');
                    tbPasswordBaruu.setEchoChar('\0');
                } else {
                    tbPasswordLama.setEchoChar('*');
                    tbPasswordBaru.setEchoChar('*');
                    tbPasswordBaruu.setEchoChar('*');
                }
            }
        });
    }

    public void clear (String[] data) {
        tbPasswordLama.setText(null);
        tbPasswordBaru.setText(null);
        tbPasswordBaruu.setText(null);
        password = data[4];
    }
}
