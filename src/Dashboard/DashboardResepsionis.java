package Dashboard;

import Database.DBConnect;
import Forms.Resepsionis.FormUserMenu;
import Forms.Resepsionis.*;
import Forms.Profile.*;
import Template.Greetings.Resepsionis;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardResepsionis extends JFrame {
    private JPanel MainPanel;
    private JPanel SubMainPanel;
    private JPanel Panel_Kiri;
    private JPanel Panel_Akun;
    private JLabel Label_Program;
    private JPanel Panel_Bantuan_Akun;
    private JLabel Label_Nama;
    private JLabel Label_Jabatan;
    private JPanel Panel_Menu;
    private JButton btnDashboard;
    private JButton btnUser;
    private JButton btnRole;
    private JPanel Panel_Bantuan;
    private JPanel Panel_Tengah;
    private JLabel Label_Salam;
    private JButton btnKamar;
    private JButton btnJenisKamar;
    private JButton btnLogout;
    private JButton btnKelasKamar;
    private JButton btnFasilitas;
    private JButton btnDetailFasilitas;
    private JButton btnMenuMakanan;
    private JButton btnJenisMakanan;
    private JButton btnRuangan;
    private JButton btnTransaksiKamar;
    private JButton btnTransaksiPembelian;
    private JButton btnTransaksiRuangan;
    private JPanel Panel_Konten;

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

    public DashboardResepsionis(String[] data) {
        add(this.MainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        user = data;

        loadAkun(user);

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login.FormLogin form = new Login.FormLogin();
                form.setVisible(true);
            }
        });

        btnUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormUserMenu show = new FormUserMenu();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                Resepsionis show = new Resepsionis();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormRole show = new FormRole();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnKamar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormKamar show = new FormKamar();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnJenisKamar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormJenisKamar show = new FormJenisKamar();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnKelasKamar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormKelasKamar show = new FormKelasKamar();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnFasilitas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormFasilitas show = new FormFasilitas();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnDetailFasilitas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormDetailFasilitas show = new FormDetailFasilitas();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnMenuMakanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormMenu show = new FormMenu();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnJenisMakanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormJenisMenu show = new FormJenisMenu();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnRuangan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormRuangan show = new FormRuangan();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnTransaksiKamar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormTransaksiKamar show = new FormTransaksiKamar(data);
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnTransaksiPembelian.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormTransaksiPembelian show = new FormTransaksiPembelian(data);
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        btnTransaksiRuangan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();

                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormTransaksiRuangan show = new FormTransaksiRuangan(data);
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
    }
}
