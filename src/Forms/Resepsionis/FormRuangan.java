package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormRuangan {
    public JPanel MainPanel;
    private JTextArea tbDeskripsiRuangan;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JTable tblMasterRuangan;
    private JTextField tbHargaRuangan;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JRadioButton tersediaRadioButton;
    private JRadioButton tidakTersediaRadioButton;
    private JPanel Panel_Tombol;
    private JPanel Panel_Tabel;
    private JTextField tbNamaRuangan;

    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id;
    String deskripsi;
    String harga;
    int status;

    public boolean validasiNull(){
        if(deskripsi.isEmpty()){
            return true;
        }else {
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
            String query = "EXEC sp_IDRuangan3";
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
                    id = "RN00" + idmm;
                }
                //Jika id nya lebih dari sama dengan 10 dan kurang dari 100 (10 - 99)
                else if (idmm >= 10 && idmm < 100)
                {
                    id = "RN0" + idmm;
                }
                else
                {
                    id = "RN" + idmm;
                }
            }
            //Jika maxid kosong
            else
            {
                id = "MM001";
            }
        } catch(Exception e)
        {
            JOptionPane.showInputDialog("Terjadi error saat load id data ruangan: " + e);
        }
        return id;
    }

    public void loadData(){
        model = new DefaultTableModel();
        tblMasterRuangan.setModel(model);
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
            String query = "EXEC sp_LoadRuangan";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()){
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString("id_ruangan");
                obj[1] = connection.result.getString("nama_ruangan");
                obj[2] = kursIndonesia.format(connection.result.getInt("harga_ruangan"));
                obj[3] = connection.result.getString("status");
                model.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data ruangan: " +ex);
        }
    }
    //validasi hanya memasukkan huruf
    void validasiHuruf(KeyEvent b){
        if(Character.isDigit(b.getKeyChar())){
            b.consume();
        }
    }
    public void clear(){
        tbNamaRuangan.setText(null);
        tbHargaRuangan.setText(null);
        tbPencarian.setText(null);
        tersediaRadioButton.setSelected(false);
        tidakTersediaRadioButton.setSelected(false);
        loadData();

        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
    }
    public void addColumn(){
        model.addColumn("ID Ruangan");
        model.addColumn("Nama Ruangan");
        model.addColumn("Harga Ruangan");
        model.addColumn("Status Ruangan");
    }

    public FormRuangan() {
        clear();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deskripsi = tbNamaRuangan.getText();
                harga = tbHargaRuangan.getText();
                String nilai_ribuan = harga;
                harga = nilai_ribuan.replace(",", "");

                if (tersediaRadioButton.isSelected()) {
                    status = 1;
                } else {
                    status = 0;
                }

                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[1];
                obj[0] = tbNamaRuangan.getText();

                int j = tblMasterRuangan.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[0].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama Ruangan Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertRuangan @id_ruangan=?, @deskripsi_ruangan=?, @harga_ruangan=?, @status_ruangan=? ";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, autoID());
                                connection.pstat.setString(2, deskripsi);
                                connection.pstat.setString(3, harga);
                                connection.pstat.setInt(4, status);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat insert data ruangan ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Insert data ruangan berhasil");
                            clear();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });

        tblMasterRuangan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterRuangan.getSelectedRow();
                id = ((String) model.getValueAt(i,0));
                tbNamaRuangan.setText((String) model.getValueAt(i,1));
                String ab = (String) model.getValueAt(i,3);

                if (ab.equals("Tersedia")) {
                    tersediaRadioButton.setSelected(true);
                } else {
                    tidakTersediaRadioButton.setSelected(true);
                }

                String nilai_ribuan = (String) model.getValueAt(i,2);
                String nilai_angka = nilai_ribuan.replace("Rp. ", "");
                nilai_angka = nilai_angka.replace(",", "");
                long a = Long.parseLong(nilai_angka) / 100;

                try{
                    String sbyr = String.valueOf(a).replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        tbHargaRuangan.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }

                btnTambah.setEnabled(false);
                btnHapus.setEnabled(true);
                btnUpdate.setEnabled(true);
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
                    String query = "EXEC sp_CariRuangan @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = kursIndonesia.format(connection.result.getInt(3));
                        obj[3] = connection.result.getString(4);
                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Data ruangan tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data ruangan: " + e);
                    clear();
                }

            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deskripsi= tbNamaRuangan.getText();
                harga = tbHargaRuangan.getText();

                if (tersediaRadioButton.isSelected()) {
                    status = 1;
                } else {
                    status = 0;
                }

                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = tbNamaRuangan.getText();

                int j = tblMasterRuangan.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 2))) {
                        found = true;
                    }
                }

                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama ruangan Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_UpdateRuangan @deskripsi_ruangan=?, @harga_ruangan=?, @status_ruangan=?, @id_ruangan=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, deskripsi);
                                connection.pstat.setString(2, harga);
                                connection.pstat.setInt(3, status);
                                connection.pstat.setString(4, id);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data ruangan ke database: " + ex);
                            }

                            JOptionPane.showMessageDialog(null, "Update data ruangan Berhasil");
                            clear();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
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
                        JOptionPane.showMessageDialog(null, "Data Ruangan Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteRuangan @id_ruangan =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data Ruangan Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error pada saat delete data ruangan: " + ex);
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
                    String query = "EXEC sp_CariRuangan @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = kursIndonesia.format(connection.result.getInt(3));
                        obj[3] = connection.result.getString(4);
                        model.addRow(obj);
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data ruangan: " + e);
                    clear();
                }
            }
        });
        tbHargaRuangan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                try{
                    String sbyr = tbHargaRuangan.getText().replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        tbHargaRuangan.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }
            }
        });
        tbHargaRuangan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiAngka(e);
            }
        });
    }

    void validasiAngka(KeyEvent e) {
        char c = e.getKeyChar();
        if (!((c >= '0') && (c <= '9') && tbHargaRuangan.getText().length() < 16
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            e.consume();
        }
    }
}
