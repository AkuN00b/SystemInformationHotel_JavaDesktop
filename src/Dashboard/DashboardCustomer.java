package Dashboard;

import Database.DBConnect;
import Forms.Customer.FormBeliMenu;
import Forms.Profile.FormProfil;
import Forms.Resepsionis.FormUserMenu;
import Template.Greetings.Customer;
import Template.Greetings.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardCustomer extends JFrame {
    private JPanel MainPanel;
    private JPanel Panel_Kiri;
    private JPanel Panel_Akun;
    private JLabel Label_Program;
    private JPanel Panel_Bantuan_Akun;
    private JLabel Label_Nama;
    private JLabel Label_Jabatan;
    private JPanel Panel_Menu;
    private JButton Tombol_Halaman_Awal;
    private JButton Tombol_Logout;
    private JButton Tombol_Laporan_Transaksi;
    private JPanel Panel_Konten;
    private JLabel Label_Salam;

    DBConnect connection = new DBConnect();

    String[] user;

    public void loadAkun(String[] data){
        // Menampilkan nama akun melalui label nama
        Label_Nama.setText(data[2]);

        user = data;

        // Menampilkan jabatan akun melalui label jabatan
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();

            String query = "SELECT * FROM [Role] r INNER JOIN [User] u ON u.id_role = r.id_role WHERE u.id_user = '" + data[1] + "'";
            connection.result = connection.stat.executeQuery(query);

            if (!connection.result.next()) {
                JOptionPane.showMessageDialog(MainPanel, "User Tidak Ditemukan !", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }

            String role = connection.result.getString(2);
            Label_Jabatan.setText(role);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(MainPanel,ex.getMessage() + " !","Peringatan",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void loadProfile() {
        try {
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoginNow @id_user=?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, user[1]);
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                user[2] = connection.result.getString(2);
                user[3] = connection.result.getString(3);
            }

            connection.stat.close();
            connection.result.close();

            Label_Nama.setText(user[2]);
        } catch (Exception ex) {
            System.out.println("Terjadi Error saat cari data jenis kamar: " + ex);
        }
    }

    public DashboardCustomer(String[] data) {
        add(this.MainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        loadAkun(data);

        Tombol_Logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login.FormLogin form = new Login.FormLogin();
                form.setVisible(true);
            }
        });

        Tombol_Halaman_Awal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                Customer show = new Customer();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });
        Panel_Akun.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormProfil show = new FormProfil(user);
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        Tombol_Laporan_Transaksi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormBeliMenu show = new FormBeliMenu(user);
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });
    }
}
