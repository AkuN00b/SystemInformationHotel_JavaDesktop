package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormJenisKamar {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JTextField tbNamaJenisKamar;
    private JPanel Panel_Tombol;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JPanel Panel_Tabel;
    private JTable tblMasterJenisKamar;
    private JTextArea tbDeskripsiJenisKamar;
    private JTextField tbHargaJenisKamar;

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

    public FormJenisKamar() {
        clear();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaJenisKamar.getText();
                deskripsi = tbDeskripsiJenisKamar.getText();
                harga = tbHargaJenisKamar.getText();
                String nilai_ribuan = harga;
                harga = nilai_ribuan.replace(",", "");

                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = nama;
                obj[1] = tbDeskripsiJenisKamar.getText();


                int j = tblMasterJenisKamar.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[0].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama Jenis Kamar Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertJenisKamar @nama_jenis_kamar=?, @deskripsi_jenis_kamar=?, @harga_jenis_kamar=? ";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.setString(2, deskripsi);
                                connection.pstat.setString(3, harga);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data ke database" + ex);
                            }

                            JOptionPane.showMessageDialog(null, "Insert data Jenis Kamar berhasil");

                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                    clear();
                }
            }
        });

        tblMasterJenisKamar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterJenisKamar.getSelectedRow();
                id = ((String) model.getValueAt(i,0));
                tbNamaJenisKamar.setText((String) model.getValueAt(i,1));
                tbDeskripsiJenisKamar.setText((String) model.getValueAt(i,2));
                String nilai_ribuan = (String) model.getValueAt(i,3);
                String nilai_angka = nilai_ribuan.replace("Rp. ", "");
                nilai_angka = nilai_angka.replace(",", "");
                int a = Integer.parseInt(nilai_angka) / 100;

                try{
                    String sbyr = String.valueOf(a).replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        tbHargaJenisKamar.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }

                btnTambah.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
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
                        JOptionPane.showMessageDialog(null, "Data jenis kamar Tidak Berhasil Dihapus");
                    } else {
                        String query = "EXEC sp_DeleteJenisKamar @id_jenis_kamar = ?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data jenis kamar Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data jenis kamar: " + ex);
                }

                clear();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaJenisKamar.getText();
                deskripsi= tbDeskripsiJenisKamar.getText();
                harga= tbHargaJenisKamar.getText();
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = tbNamaJenisKamar.getText();

                int j = tblMasterJenisKamar.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 2))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama jenis kamar Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_UpdateJenisKamar @nama_jenis_kamar=?,@deskripsi_jenis_kamar=?, @harga_jenis_kamar=?, @id_jenis_kamar=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.setString(2, deskripsi);
                                connection.pstat.setString(3, harga);
                                connection.pstat.setString(4, id);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data jenis kamar ke database" + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Update data jenis kamar Berhasil");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    clear();
                }
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        tbNamaJenisKamar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiHuruf(e);
            }
        });

        tbHargaJenisKamar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiAngka(e);
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
                    String query = "EXEC sp_CariJenisKamar @cari=?";
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
                        JOptionPane.showMessageDialog(null, "Data jenis kamar tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data jenis kamar: " + e);
                }
            }
        });

        tbHargaJenisKamar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try{
                    String sbyr = tbHargaJenisKamar.getText().replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        tbHargaJenisKamar.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

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
                    String query = "EXEC sp_CariJenisKamar @cari=?";
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
                    System.out.println("Terjadi Error saat cari data jenis kamar: " + e);
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
        if (!((c >= '0') && (c <= '9') && tbHargaJenisKamar.getText().length() < 14
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            e.consume();
        }
    }

    public void addColumn(){
        model.addColumn("ID Jenis Kamar");
        model.addColumn("Nama Jenis Kamar");
        model.addColumn("Deskripsi Jenis Kamar");
        model.addColumn("Harga Jenis Kamar");
    }

    public void loadData() {
        model = new DefaultTableModel();
        tblMasterJenisKamar.setModel(model);
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
            String query = "EXEC sp_LoadJenisKamar";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString("id_jenis_kamar");
                obj[1] = connection.result.getString("nama_jenis_kamar");
                obj[2] = connection.result.getString("deskripsi_jenis_kamar");
                obj[3] = kursIndonesia.format(connection.result.getInt("harga_jenis_kamar"));
                model.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data jenis kamar: " +ex);
        }
    }
    public void clear() {
        tbNamaJenisKamar.setText(null);
        tbDeskripsiJenisKamar.setText(null);
        tbHargaJenisKamar.setText(null);
        tbPencarian.setText(null);
        loadData();

        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
    }
}
