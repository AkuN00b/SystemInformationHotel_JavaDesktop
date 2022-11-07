package Dashboard;

import Database.DBConnect;
import Forms.Manager.FormReportKamar;
import Forms.Manager.FormReportMenu;
import Forms.Manager.FormReportRuangan;
import Forms.Profile.FormProfil;
import Template.Greetings.Manager;
import Template.Greetings.Resepsionis;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardManager extends JFrame {
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
    private JPanel Panel_Konten;
    private JLabel Label_Salam;
    private JButton Tombol_Laporan_Transaksi_Kamar;
    private JButton Tombol_Laporan_Transaksi_Menu;
    private JButton Tombol_Laporan_Transaksi_Ruangan;

    DBConnect connection = new DBConnect();

    String[] user;

    public void loadAkun(String[] data){
        // Menampilkan nama akun melalui label nama
        Label_Nama.setText(data[2]);

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

    public DashboardManager(String[] data) {
        add(this.MainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        user = data;

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
                Manager show = new Manager();
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
                FormProfil show = new FormProfil(data);
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        Tombol_Laporan_Transaksi_Kamar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormReportKamar show = new FormReportKamar();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        Tombol_Laporan_Transaksi_Menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormReportMenu show = new FormReportMenu();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        Tombol_Laporan_Transaksi_Ruangan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormReportRuangan show = new FormReportRuangan();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });
    }
}
