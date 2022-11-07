package Forms.Profile;

import Database.DBConnect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEditProfile {
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JTextField tbNama;
    private JTextField tbEmail;
    private JPanel Panel_Tombol;
    private JButton btnCancel;
    private JButton btnUpdate;
    public JPanel MainPanel;

    DBConnect connection = new DBConnect();

    String nama;
    String email;

    public FormEditProfile(String[] data) {
        clear(data);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear(data);
            }
        });


        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNama.getText();
                email = tbEmail.getText();

                try {
                    if (validasiNull()) {
                        throw new Exception("Mohon untuk mengisi seluruh data");
                    } else {
                        try {
                            String query = "EXEC sp_UpdateProfil @id=?, @nama=?, @email=? ";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, data[1]);
                            connection.pstat.setString(2, nama);
                            connection.pstat.setString(3, email);

                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        } catch (Exception ex) {
                            System.out.println("Error saat update data ke database" + ex);
                        }

                        JOptionPane.showMessageDialog(null, "Insert data berhasil");

                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }

                try {
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_LoginNow @id_user=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, data[1]);
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        data[2] = connection.result.getString(2);
                        data[3] = connection.result.getString(3);
                    }

                    connection.stat.close();
                    connection.result.close();

                    clear(data);
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data jenis kamar: " + e);
                }
            }
        });
    }

    public boolean validasiNull(){
        if (nama.isEmpty() || email.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    public void clear(String[] data) {
        tbNama.setText(data[2]);
        tbEmail.setText(data[3]);
    }
}
