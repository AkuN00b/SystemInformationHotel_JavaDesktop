package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormKelasKamar {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JTextField tbNamaKelasKamar;
    private JTextField tbHargaKelasKamar;
    private JTextArea tbDeskripsiKelasKamar;
    private JPanel Panel_Tombol;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JPanel Panel_Tabel;
    private JTable tblMasterKelasKamar;

    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id;
    String nama;
    String deskripsi;
    String harga;

    //validasi Jika Kosong
    public boolean validasiNull(){
        if (nama.isEmpty() || deskripsi.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    public FormKelasKamar() {
        clear();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaKelasKamar.getText();
                deskripsi = tbDeskripsiKelasKamar.getText();
                harga = tbHargaKelasKamar.getText();
                String nilai_ribuan = harga;
                harga = nilai_ribuan.replace(",", "");

                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = nama;
                obj[1] = tbDeskripsiKelasKamar.getText();

                int j =tblMasterKelasKamar.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[0].equals(model.getValueAt(k, 1)))  {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama Kelas Kamar Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertKelasKamar @nama_kelas_kamar=?, @deskripsi_kelas_kamar=?, @harga_kelas_kamar=? ";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.setString(2, deskripsi);
                                connection.pstat.setString(3, harga);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat tambah data kelas kamar ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Insert data kelas kamar berhasil");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    clear();
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaKelasKamar.getText();
                deskripsi= tbDeskripsiKelasKamar.getText();
                harga= tbHargaKelasKamar.getText();
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = tbNamaKelasKamar.getText();

                int j = tblMasterKelasKamar.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama kelas kamar Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_UpdateKelasKamar @nama_kelas_kamar=?, @deskripsi_kelas_kamar=?, @harga_kelas_kamar=?, @id_kelas_kamar=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.setString(2, deskripsi);
                                connection.pstat.setString(3, harga);
                                connection.pstat.setString(4, id);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data kelas kamar ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Update data kelas kamar Berhasil");

                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    clear();
                }

            }
        });

        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                try {
                    opsi = JOptionPane.showConfirmDialog(null,"Apakah ingin menghapus data?",
                            "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (opsi != 0){
                        JOptionPane.showMessageDialog(null, "Data Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteKelasKamar @id_kelas_kamar =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data" + ex);
                }
                clear();

            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        tbNamaKelasKamar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiHuruf(e);
            }
        });

        tbHargaKelasKamar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiAngka(e);
            }
        });

        tblMasterKelasKamar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterKelasKamar.getSelectedRow();
                id = ((String) model.getValueAt(i,0));
                tbNamaKelasKamar.setText((String) model.getValueAt(i,1));
                tbDeskripsiKelasKamar.setText((String) model.getValueAt(i,2));

                String nilai_ribuan = (String) model.getValueAt(i,3);
                String nilai_angka = nilai_ribuan.replace("Rp. ", "");
                nilai_angka = nilai_angka.replace(",", "");
                int a = Integer.parseInt(nilai_angka) / 100;

                try{
                    String sbyr = String.valueOf(a).replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        tbHargaKelasKamar.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }

                btnTambah.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
            }
        });

        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong

                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);

                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariKelasKamar @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = kursIndonesia.format(connection.result.getInt(4));
                        model.addRow(obj);
                    }

                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Data kelas kamar tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data kelas kamar: " + e);
                    clear();
                }
            }
        });
        tbPencarian.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong

                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);

                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariKelasKamar @cari=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = kursIndonesia.format(connection.result.getInt(4));
                        model.addRow(obj);
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data kelas kamar: " + e);
                    clear();
                }
            }
        });
        tbHargaKelasKamar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                try{
                    String sbyr = tbHargaKelasKamar.getText().replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        tbHargaKelasKamar.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }
            }
        });
    }

    //validasi hanya memasukkan huruf
    void validasiHuruf(KeyEvent b){
        if(Character.isDigit(b.getKeyChar())){
            b.consume();
        }
    }

    void validasiAngka(KeyEvent e){
        char c = e.getKeyChar();
        if (!((c >= '0') && (c <= '9') && tbHargaKelasKamar.getText().length() < 16
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            e.consume();
        }
    }
    public void addColumn(){
        model.addColumn("ID Kelas Kamar");
        model.addColumn("Nama Kelas Kamar");
        model.addColumn("Deskripsi Kelas Kamar");
        model.addColumn("Harga Kelas Kamar");
    }

    public void loadData(){
        model = new DefaultTableModel();
        tblMasterKelasKamar.setModel(model);
        addColumn();

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try{
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadKelasKamar";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString("id_kelas_kamar");
                obj[1] = connection.result.getString("nama_kelas_kamar");
                obj[2] = connection.result.getString("deskripsi_kelas_kamar");
                obj[3] = kursIndonesia.format(connection.result.getInt("harga_kelas_kamar"));
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data kelas kamar: " +ex);
        }
    }

    public void clear(){
        tbNamaKelasKamar.setText(null);
        tbDeskripsiKelasKamar.setText(null);
        tbHargaKelasKamar.setText(null);
        tbPencarian.setText(null);
        loadData();

        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
    }
}
