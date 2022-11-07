package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormMenu {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JComboBox cbJeniMenu;
    private JPanel Panel_Tombol;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JPanel Panel_Tabel;
    private JTable tblMasterMenu;
    private JTextField tbNamaMenu;
    private JTextArea tbDeskripsiMenu;
    private JTextField tbHargaMenu;
    private JRadioButton tersediaRadioButton;
    private JRadioButton tidakTersediaRadioButton;

    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id ;
    String idJenisMenu;
    String nama;
    String deskripsi;
    String harga;
    int status;

    public FormMenu() {
        tampilJenisMenu();
        clear();

        String.valueOf(cbJeniMenu.getSelectedItem());
        tampilIdJenisMenu();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String.valueOf(cbJeniMenu.getSelectedItem());
                tampilIdJenisMenu();

                nama = tbNamaMenu.getText();
                harga = tbHargaMenu.getText();
                deskripsi = tbDeskripsiMenu.getText();

                String nilai_ribuan = harga;
                harga = nilai_ribuan.replace(",", "");

                if (tersediaRadioButton.isSelected()) {
                    status = 1;
                } else {
                    status = 0;
                }

                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = tbNamaMenu.getText();

                int j = tblMasterMenu.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }

                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama Menu Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertMenu @id_menu=?,@id_jenis_menu=?, @nama_menu=?, @deskripsi_menu=?, @harga_menu=?, @status_menu=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, autoID());
                                connection.pstat.setString(2, idJenisMenu);
                                connection.pstat.setString(3, nama);
                                connection.pstat.setString(4, deskripsi);
                                connection.pstat.setString(5, harga);
                                connection.pstat.setInt(6,status);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data menu ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Insert data menu berhasil");

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
                String.valueOf(cbJeniMenu.getSelectedItem());
                tampilIdJenisMenu();

                nama = tbNamaMenu.getText();
                harga = tbHargaMenu.getText();
                deskripsi= tbDeskripsiMenu.getText();

                if (tersediaRadioButton.isSelected()) {
                    status = 1;
                } else {
                    status = 0;
                }

                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = tbDeskripsiMenu.getText();

                int j = tblMasterMenu.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 2))) {
                        found = true;
                    }
                }

                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama Menu Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_UpdateMenu @id_jenis_menu=?, @nama_menu=?, @harga_menu=?, @deskripsi_menu=?, @status_menu=?, @id_menu=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, idJenisMenu);
                                connection.pstat.setString(2, nama);
                                connection.pstat.setString(3, harga);
                                connection.pstat.setString(4, deskripsi);
                                connection.pstat.setInt(5, status);
                                connection.pstat.setString(6, id);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data menu ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Update data menu Berhasil");

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
                        JOptionPane.showMessageDialog(null, "Data menu Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteMenu @id_Menu =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data menu Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data" + ex);
                }
                clear();
            }
        });

        tblMasterMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int a = 0;
                
                int i = tblMasterMenu.getSelectedRow();
                id = ((String) model.getValueAt(i,0));
                cbJeniMenu.setSelectedItem(model.getValueAt(i,1));
                tbNamaMenu.setText((String) model.getValueAt(i,2));
                tbDeskripsiMenu.setText((String) model.getValueAt(i,4));
                String ab = (String) model.getValueAt(i,5);

                if (ab.equals("Tersedia")) {
                    tersediaRadioButton.setSelected(true);
                } else {
                    tidakTersediaRadioButton.setSelected(true);
                }

                try {
                    String nilai_ribuan = (String) model.getValueAt(i,3);
                    String nilai_angka = nilai_ribuan.replace("Rp. ", "");
                    nilai_angka = nilai_angka.replace(",", "");
                    a = Integer.parseInt(nilai_angka) / 100;
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }

                try {
                    String sbyr = String.valueOf(a).replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        tbHargaMenu.setText(df.format(dblByr));
                    } else {
                        tbHargaMenu.setText(String.valueOf(a));
                    }
                } catch (Exception ex) {

                }

                btnTambah.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
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
                    String query =  "EXEC sp_CariMenu @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[6];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = kursIndonesia.format(connection.result.getInt(4));
                        obj[4] = connection.result.getString(5);
                        obj[5] = connection.result.getString(6);
                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Data menu tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data menu: " + e);
                }
            }
        });
        tbNamaMenu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiHuruf(e);
            }
        });

        tbHargaMenu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiAngka(e);
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
                    String query =  "EXEC sp_CariMenu @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[6];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = kursIndonesia.format(connection.result.getInt(4));
                        obj[4] = connection.result.getString(5);
                        obj[5] = connection.result.getString(6);
                        model.addRow(obj);
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data menu: " + e);
                }
            }
        });
        tbHargaMenu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                try{
                    String sbyr = tbHargaMenu.getText().replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        tbHargaMenu.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }
            }
        });
    }

    public boolean validasiNull(){
        if(nama.isEmpty() || deskripsi.isEmpty() || idJenisMenu.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    public  void tampilJenisMenu(){
        try{
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxJenisMenu";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()){
                cbJeniMenu.addItem(connection.result.getString("nama_jenis_makanan"));
            }
            connection.stat.close();
            connection.result.close();
        }catch (SQLException ex){
            System.out.println("Terjadi error saat load data combo box menu: " +ex);
        }
    }

    public void tampilIdJenisMenu() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxGetIDJenisMenu @cari=?";
            connection.pstat = connection.conn.prepareStatement(sql1);
            connection.pstat.setString(1, String.valueOf(cbJeniMenu.getSelectedItem()));
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                idJenisMenu = (connection.result.getString("id_jenis_makanan"));
            }
            connection.stat.close();
            connection.result.close();
        }catch (SQLException ex){
            System.out.println("Terjadi error saat load data jenis menu: " +ex);
        }
    }
    //validasi hanya memasukkan huruf
    void validasiHuruf(KeyEvent b){
        if(Character.isDigit(b.getKeyChar())){
            b.consume();
        }
    }

    void validasiAngka(KeyEvent e){
        char c = e.getKeyChar();
        if (!((c >= '0') && (c <= '9') && tbHargaMenu.getText().length() < 16
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            e.consume();
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
            String query = "EXEC sp_IDMenu3";
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
                    id = "MN00" + idmm;
                }
                //Jika id nya lebih dari sama dengan 10 dan kurang dari 100 (10 - 99)
                else if (idmm >= 10 && idmm < 100)
                {
                    id = "MN0" + idmm;
                }
                else
                {
                    id = "MN" + idmm;
                }
            }
            //Jika maxid kosong
            else
            {
                id = "MN001";
            }
        }catch(Exception e)
        {
            JOptionPane.showInputDialog("Terjadi error saat load id data menu: " + e);
        }
        return id;
    }

    public void addColumn(){
        model.addColumn("ID Menu");
        model.addColumn("ID Jenis Menu");
        model.addColumn("Nama Menu");
        model.addColumn("Harga Menu");
        model.addColumn("Deskripsi Menu");
        model.addColumn("Status Menu");
    }

    public void clear(){
        tbNamaMenu.setText(null);
        tbDeskripsiMenu.setText(null);
        tbHargaMenu.setText(null);
        cbJeniMenu.setSelectedItem(null);
        tbPencarian.setText(null);
        loadData();

        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    public void loadData(){
        model = new DefaultTableModel();
        tblMasterMenu.setModel(model);
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
            String query = "EXEC sp_LoadMenu";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()){
                Object[] obj = new Object[6];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = kursIndonesia.format(connection.result.getInt(4));
                obj[4] = connection.result.getString(5);
                obj[5] = connection.result.getString(6);
                model.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data menu: " +ex);
        }
    }
}
