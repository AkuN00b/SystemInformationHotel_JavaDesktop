package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class FormKamar {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JPanel Panel_Tombol;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JPanel Panel_Tabel;
    private JTable tblMasterKamar;
    private JComboBox cbJenisKamar;
    private JComboBox cbKelasKamar;
    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id;
    String id_kelas_kamar;
    String id_jenis_kamar;

    public void addColumn(){
        model.addColumn("ID Kamar");
        model.addColumn("Kelas Kamar");
        model.addColumn("Jenis Kamar");
        model.addColumn("Status Kamar");
    }

    public void tampilKelasKamar() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxKelasKamar";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbKelasKamar.addItem(connection.result.getString("nama_kelas_kamar"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data combo box kelas kamar: " + ex);
        }
    }

    public void tampilJenisKamar() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxJenisKamar";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbJenisKamar.addItem(connection.result.getString("nama_jenis_kamar"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data combo box jenis kamar: " + ex);
        }
    }

    public void tampilIdKelasKamar() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxGetIDKelasKamar @cari=?";

            connection.pstat = connection.conn.prepareStatement(sql1);
            connection.pstat.setString(1, String.valueOf(cbKelasKamar.getSelectedItem()));
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                id_kelas_kamar = (connection.result.getString("id_kelas_kamar"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mendapatkan Id kelas kamar: " + ex);
        }
    }

    public void tampilIdJenisKamar() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxGetIDJenisKamar @cari=?";

            connection.pstat = connection.conn.prepareStatement(sql1);
            connection.pstat.setString(1, String.valueOf(cbJenisKamar.getSelectedItem()));
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                id_jenis_kamar = (connection.result.getString("id_jenis_kamar"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mendapatkan Id jenis kamar: " + ex);
        }
    }

    public void loadData() {
        model = new DefaultTableModel();
        tblMasterKamar.setModel(model);
        addColumn();

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadKamar";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                model.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data kamar: " +ex);
        }
    }

    public void clear() {
        cbJenisKamar.setSelectedItem(null);
        cbKelasKamar.setSelectedItem(null);
        tbPencarian.setText(null);
        loadData();

        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    public boolean validasiNull() {
        if (id_kelas_kamar == null || id_jenis_kamar == null) {
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
            String query = "EXEC sp_IDKamar3";
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
                idmm = Integer.parseInt(maxID.substring(3, 6))+1;
                //Jika id nya kurang dari 10
                if (idmm < 10)
                {
                    id = "KMR00" + idmm;
                }
                //Jika id nya lebih dari sama dengan 10 dan kurang dari 100 (10 - 99)
                else if (idmm >= 10 && idmm < 100)
                {
                    id = "KMR0" + idmm;
                }
                else
                {
                    id = "KMR" + idmm;
                }
            }
            //Jika maxid kosong
            else
            {
                id = "FS001";
            }
        }catch(Exception e)
        {
            JOptionPane.showInputDialog("Terjadi error saat load data kamar: " + e);
        }

        return id;
    }

    public FormKamar() {
        tampilJenisKamar();
        tampilKelasKamar();
        clear();

        String.valueOf(cbJenisKamar.getSelectedItem());
        tampilIdJenisKamar();

        String.valueOf(cbKelasKamar.getSelectedItem());
        tampilIdKelasKamar();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String.valueOf(cbJenisKamar.getSelectedItem());
                tampilIdJenisKamar();

                String.valueOf(cbKelasKamar.getSelectedItem());
                tampilIdKelasKamar();

                try {
                    if (validasiNull()) {
                        throw new Exception("Mohon untuk mengisi seluruh data");
                    } else {
                        try {
                            String query = "EXEC sp_InsertKamar @id_kamar=?, @id_kelas_kamar=?, @id_jenis_kamar=?, @status_kamar=?" ;
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, autoID());
                            connection.pstat.setString(2, id_kelas_kamar);
                            connection.pstat.setString(3, id_jenis_kamar);
                            connection.pstat.setInt(4, 1);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        } catch (Exception ex) {
                            System.out.println("Error saat insert data kamar ke database: " + ex);
                        }
                        JOptionPane.showMessageDialog(null, "Insert data kamar berhasil");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }

                clear();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String.valueOf(cbJenisKamar.getSelectedItem());
                tampilIdJenisKamar();

                String.valueOf(cbKelasKamar.getSelectedItem());
                tampilIdKelasKamar();

                try {
                    if (validasiNull()) {
                        throw new Exception("Mohon untuk mengisi seluruh data");
                    } else {
                        try {
                            String query = "EXEC sp_UpdateKamar @id_kelas_kamar=?, @id_jenis_kamar=?, @id_kamar=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id_kelas_kamar);
                            connection.pstat.setString(2, id_jenis_kamar);
                            connection.pstat.setString(3, id);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        } catch (Exception ex) {
                            System.out.println("Error saat update data kamar ke database: " + ex);
                        }
                        JOptionPane.showMessageDialog(null, "Update data kamar berhasil");

                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
                clear();
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
                        JOptionPane.showMessageDialog(null, "DataKamar Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteKamar @id_kamar=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data Kamar Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error pada saat delete data kamar: " + ex);
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

        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariKamar @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        model.addRow(obj);
                    }
                    if(model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Data kamar tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("Error saat pencarian data kamar: " +ex);
                    clear();
                }
            }
        });

        tblMasterKamar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterKamar.getSelectedRow();
                id = ((String) model.getValueAt(i, 0));
                cbKelasKamar.setSelectedItem(model.getValueAt(i, 1));
                cbJenisKamar.setSelectedItem(model.getValueAt(i, 2));

                btnTambah.setEnabled(false);
                btnHapus.setEnabled(true);
                btnUpdate.setEnabled(true);
            }
        });
        tbPencarian.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariKamar @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        model.addRow(obj);
                    }
                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("Error saat pencarian data kamar: " +ex);
                    clear();
                }
            }
        });
    }
}
