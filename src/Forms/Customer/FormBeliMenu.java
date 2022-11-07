package Forms.Customer;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormBeliMenu {
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Bawah;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Tengah;
    private JPanel TransaksiPanel;
    private JPanel Panel_Data;
    private JTextField tbTrsKamar;
    private JPanel Panel_Menu;
    private JPanel Panel_Kontrol_Menu;
    private JTextField tbMenu;
    private JTable tblMenu;
    private JTextField tbJumlah;
    private JPanel Panel_Button;
    private JButton tambahMenuButton;
    private JButton editMenuButton;
    private JButton hapusMenuButton;
    private JPanel Panel_Button_2;
    private JButton hapusSemuaMenuButton;
    private JButton batalPembelianMenuButton;
    private JTextField tbCustomer;
    private JTextField tbKamar;
    private JTextField tbSearchMenu;
    private JPanel Panel_Detail_Pembelian;
    private JTable tblDetailPembelian;
    private JTextField tbSearchDetailPembelian;
    private JPanel Panel_Tombol;
    private JButton beliMenuButton;
    private JTextField tbTotalHarga;
    public JPanel MainPanel;

    private DefaultTableModel modelMenu;
    private DefaultTableModel modelDetailPembelian;
    private DefaultTableModel modelCheckIn;
    DBConnect connection = new DBConnect();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String id, id_kamar, id_menu, id_customer, hari, id_receptionist, harga_kelas, harga_jenis, id_trs_kamar;
    String a = null, b = null, c = null, d = null, e = null, f = null;
    Double harga, total_harga, tempHarga;
    String[] user;

    public FormBeliMenu(String[] data) {
        id_customer = data[1];
        user = data;

        clear();

        tbSearchMenu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                modelMenu.getDataVector().removeAllElements(); //menghapus semua data ditamp
                modelMenu.fireTableDataChanged(); // memberitahu data telah ksong

                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariMenuTransaksi @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbSearchMenu.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbaris data
                    while (connection.result.next()) {
                        Object[] obj = new Object[6];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = kursIndonesia.format(connection.result.getInt(4));
                        obj[4] = connection.result.getString(5);
                        obj[5] = connection.result.getString(6);

                        modelMenu.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data menu: " +ex);
                    clear();
                }
            }
        });

        tbSearchDetailPembelian.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                modelDetailPembelian.getDataVector().removeAllElements(); //menghapus semua data ditamp
                modelDetailPembelian.fireTableDataChanged(); // memberitahu data telah ksong

                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariDetailTrsMenu @cari=?, @id_tr_dapur=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbSearchDetailPembelian.getText());
                    connection.pstat.setString(2, id);
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbaris data
                    while (connection.result.next()) {
                        Object[] obj = new Object[5];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        obj[4] = kursIndonesia.format(connection.result.getInt(5));
                        modelDetailPembelian.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data transaksi detail pembelian menu: " +ex);
                    clear();
                }
            }
        });

        tblDetailPembelian.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                tambahMenuButton.setEnabled(false);
                editMenuButton.setEnabled(true);
                hapusMenuButton.setEnabled(true);

                int i = tblDetailPembelian.getSelectedRow();
                id_menu = (String) modelDetailPembelian.getValueAt(i, 1);
                tbMenu.setText((String) modelDetailPembelian.getValueAt(i, 2));
                tbJumlah.setText((String) modelDetailPembelian.getValueAt(i, 3));

                String nilai_ribuan = (String) modelDetailPembelian.getValueAt(i, 4);
                String nilai_angka = nilai_ribuan.replace("Rp. ", "");
                nilai_angka = nilai_angka.replace(",", "");
                int a = Integer.parseInt(nilai_angka) / 100;
                tempHarga = Double.parseDouble(String.valueOf(a));
                harga = tempHarga / Double.parseDouble(tbJumlah.getText());
            }
        });

        editMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jumlah = tbJumlah.getText();

                if (jumlah.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Isi kolom jumlah");
                } else {
                    total_harga = Double.parseDouble(jumlah) * harga;
                    tempHarga = total_harga - tempHarga;
                    tbTotalHarga.setText(String.valueOf(tempHarga + Double.parseDouble(tbTotalHarga.getText())));

                    try {
                        String query = "EXEC sp_UpdateDetailTrsMenu @id_tr_dapur=?, @id_menu_makanan=?, @qty=?, @total_harga=?, @tempHarga=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, id_menu);
                        connection.pstat.setString(3, jumlah);
                        connection.pstat.setString(4, String.valueOf(total_harga));
                        connection.pstat.setString(5, String.valueOf(tempHarga));
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                    } catch (Exception ex) {
                        System.out.println("Error saat edit data menu ke database: " + ex);
                    }
                    JOptionPane.showMessageDialog(null, "Data transaksi menu berhasil diperbaharui !");
                }

                tambahMenuButton.setEnabled(true);
                editMenuButton.setEnabled(false);
                hapusMenuButton.setEnabled(false);

                tbMenu.setText(null);
                tbJumlah.setText(null);
                loadDetailTrsMenu();
            }
        });

        hapusMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "EXEC sp_DeleteTransaksiMenu @id_tr_dapur=?, @id_menu_makanan=?, @harga=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, id);
                    connection.pstat.setString(2, id_menu);
                    connection.pstat.setString(3, String.valueOf(tempHarga));
                    connection.pstat.executeUpdate();
                    connection.pstat.close();

                    tbTotalHarga.setText(String.valueOf(Double.parseDouble(tbTotalHarga.getText()) - (tempHarga)));
                } catch (Exception ex) {
                    System.out.println("Error saat hapus data pembelian menu ke database: " + ex);
                }
                JOptionPane.showMessageDialog(null, "Data transaksi menu berhasil dihapus !");

                tambahMenuButton.setEnabled(true);
                editMenuButton.setEnabled(false);
                hapusMenuButton.setEnabled(false);
                tbJumlah.setText(null);
                tbMenu.setText(null);
                loadDetailTrsMenu();
            }
        });

        tambahMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jumlah = tbJumlah.getText();

                if (validasiNull()) {
                    JOptionPane.showMessageDialog(null, "Mohon untuk mengisi seluruh data !");
                } else {
                    total_harga = Double.parseDouble(jumlah) * harga;

                    try {
                        String query = "EXEC sp_InsertDetailTrsPembelianCus @id_tr_dapur=?, @id_tr_kamar=?, @id_menu_makanan=?, @qty=?, @total_harga=?, @id_customer=?, @id_kamar=?, @tgl_transaksi=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, id_trs_kamar);
                        connection.pstat.setString(3, id_menu);
                        connection.pstat.setString(4, jumlah);
                        connection.pstat.setString(5, String.valueOf(total_harga));
                        connection.pstat.setString(6, id_customer);
                        connection.pstat.setString(7, id_kamar);
                        connection.pstat.setString(8, formatter.format(new Date()));
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data transaksi pembelian menu berhasil disimpan !");
                    } catch (Exception ex) {

                    }
                }

                total_harga = total_harga + Double.parseDouble(tbTotalHarga.getText());
                tbTotalHarga.setText(total_harga.toString());

                harga = Double.valueOf(0);
                total_harga = Double.valueOf(0);

                tbMenu.setText(null);
                tbJumlah.setText(null);
                loadDetailTrsMenu();
            }
        });

        hapusSemuaMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "EXEC sp_DeleteAllTransaksiMenu @id_tr_dapur=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, id);
                    connection.pstat.executeUpdate();
                    connection.pstat.close();

                    tbTotalHarga.setText("0");
                    JOptionPane.showMessageDialog(null, "Data transaksi menu berhasil dihapus !");
                } catch (Exception ex) {
                    System.out.println("Error saat menghapus data transaksi menu ke database: " + ex);
                }

                tambahMenuButton.setEnabled(true);
                editMenuButton.setEnabled(false);
                hapusMenuButton.setEnabled(false);

                tbMenu.setText(null);
                tbJumlah.setText(null);
                loadDetailTrsMenu();
            }
        });

        batalPembelianMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "EXEC sp_DeleteTransaksi @id_tr_dapur=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, id);
                    connection.pstat.executeUpdate();
                    connection.pstat.close();

                    tbTotalHarga.setText("0");
                    JOptionPane.showMessageDialog(null, "Data transaksi berhasil dibatalkan !");
                } catch (Exception ex) {
                    System.out.println("Error saat membatalkan data transaksi menu ke database: " + ex);
                }

                clear();
            }
        });

        beliMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                total_harga = Double.parseDouble(tbTotalHarga.getText());

                try {
                    String query = "EXEC sp_UpdatePembayaranTransaksiMenu @id_tr_dapur=?, @total_harga=?, @status_transaksi=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, id);
                    connection.pstat.setString(2, String.valueOf(total_harga));
                    connection.pstat.setString(3, String.valueOf(1));
                    connection.pstat.executeUpdate();
                    connection.pstat.close();

                    JOptionPane.showMessageDialog(null, "Transaksi berhasil disimpan, status belum dibayar !");
                    clear();
                } catch (Exception ex) {
                    System.out.println("Error saat membatalkan data transaksi menu ke database: " + ex);
                }
            }
        });

        tblMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (f != null) {
                    int i = tblMenu.getSelectedRow();
                    tbMenu.setText((String) modelMenu.getValueAt(i, 2));
                    id_menu = ((String) modelMenu.getValueAt(i, 0));

                    String nilai_ribuan = (String) modelMenu.getValueAt(i, 3);
                    String nilai_angka = nilai_ribuan.replace("Rp. ", "");
                    nilai_angka = nilai_angka.replace(",", "");
                    harga = Double.parseDouble(nilai_angka) / 100;

                    if (!tbJumlah.getText().isEmpty()) {
                        total_harga = harga * Double.parseDouble(tbJumlah.getText());
                    }
                }
            }
        });
    }

    public boolean validasiNull() {
        if (id_menu.isEmpty() || id_customer.isEmpty() || id_kamar.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private String autoID()
    {
        String maxID = null;
        int idmm = 0;
        String id = null;

        try
        {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_IDTrsPembelian3";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next())
            {
                maxID = connection.result.getString("ID");
            }

            connection.stat.close();
            connection.result.close();

            if (maxID != null)
            {
                //Substring digunakan untuk memecah string id_ jadi JM + 005
                //Substring(2, 5) maksudnya, mulai dari digit ke 3 diambil 3 karakter (0, 0 dan 5)
                //Kemudian di convert ke int, sehingga 5 + 1 = 6
                idmm = Integer.parseInt(maxID.substring(2, 5))+1;
                //Jika id nya kurang dari 10
                if (idmm < 10)
                {
                    id = "TP00" + idmm;
                }
                //Jika id nya lebih dari sama dengan 10 dan kurang dari 100 (10 - 99)
                else if (idmm >= 10 && idmm < 100)
                {
                    id = "TP0" + idmm;
                }
                else
                {
                    id = "TP" + idmm;
                }
            }
            //Jika maxid kosong
            else
            {
                id = "FS001";
            }
        } catch(Exception e)
        {
            JOptionPane.showInputDialog("Terjadi error saat load data id otomatis transaksi pembelian menu: " + e);
        }

        return id;
    }

    public void clear() {
        id = autoID();

        tbMenu.setText(null);
        tbJumlah.setText(null);
        tbTotalHarga.setText("0");

        tbCustomer.setEnabled(false);
        tbKamar.setEnabled(false);
        tbMenu.setEnabled(false);

        tambahMenuButton.setEnabled(true);
        editMenuButton.setEnabled(false);
        hapusMenuButton.setEnabled(false);

        tblDetailPembelian.setEnabled(true);
        tblMenu.setEnabled(true);

        loadCheckIn();
        loadMenu();
        loadDetailTrsMenu();
    }

    public void addColumnMenu() {
        modelMenu.addColumn("ID Menu");
        modelMenu.addColumn("Jenis Menu");
        modelMenu.addColumn("Nama Menu");
        modelMenu.addColumn("Harga Menu");
        modelMenu.addColumn("Deskripsi Menu");
        modelMenu.addColumn("Status Menu");
    }

    public void loadMenu() {
        modelMenu = new DefaultTableModel();
        tblMenu.setModel(modelMenu);
        addColumnMenu();

        modelMenu.getDataVector().removeAllElements();
        modelMenu.fireTableDataChanged();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadMenuTransaksi";

            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[6];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = kursIndonesia.format(connection.result.getInt(4));
                obj[4] = connection.result.getString(5);
                obj[5] = connection.result.getString(6);

                modelMenu.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data menu: " +ex);
        }
    }

    public void addColumnDetailPembelian() {
        modelDetailPembelian.addColumn("ID Menu");
        modelDetailPembelian.addColumn("Jenis Menu");
        modelDetailPembelian.addColumn("Nama Menu");
        modelDetailPembelian.addColumn("Jumlah");
        modelDetailPembelian.addColumn("Total Harga");

        tblDetailPembelian.getColumnModel().getColumn(1).setMinWidth(0);
        tblDetailPembelian.getColumnModel().getColumn(1).setMaxWidth(0);
        tblDetailPembelian.getColumnModel().getColumn(1).setWidth(0);
    }

    public void loadDetailTrsMenu() {
        modelDetailPembelian = new DefaultTableModel();
        tblDetailPembelian.setModel(modelDetailPembelian);
        addColumnDetailPembelian();

        modelDetailPembelian.getDataVector().removeAllElements();
        modelDetailPembelian.fireTableDataChanged();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadDetailTrsMenu @id_tr_dapur=?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, id);
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = kursIndonesia.format(connection.result.getInt(5));
                modelDetailPembelian.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data detail pembelian menu: " +ex);
        }
    }

    public void loadCheckIn() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_StatusCheckIn @id_customer=?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, id_customer);
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                a = connection.result.getString(1);
                b = connection.result.getString(2);
                c = connection.result.getString(3);
                d = connection.result.getString(4);
                e = connection.result.getString(5);
                f = connection.result.getString(6);
            }

            if (f == null) {
                JOptionPane.showMessageDialog(null, "Anda sudah checkout");
                disabled();
            } else {
                tbCustomer.setText(b);
                tbKamar.setText(c + " - " + d + " - " + e);

                id_customer = a;
                id_kamar = c;

                id_trs_kamar = f;
            }


            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data detail pembelian menu: " +ex);
        }
    }

    public void disabled() {
        tbJumlah.setEnabled(false);
        tambahMenuButton.setEnabled(false);
        editMenuButton.setEnabled(false);
        hapusMenuButton.setEnabled(false);
        hapusSemuaMenuButton.setEnabled(false);
        batalPembelianMenuButton.setEnabled(false);
        tambahMenuButton.setEnabled(false);
        tbSearchMenu.setEnabled(false);
        tbMenu.setEnabled(false);
        tblDetailPembelian.setEnabled(false);
        tbSearchDetailPembelian.setEnabled(false);
        tblMenu.setEnabled(false);
        beliMenuButton.setEnabled(false);
    }
}
