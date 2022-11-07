package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class FormDetailFasilitas {
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
    private JTable tblMasterDetailFasilitas;
    private JComboBox cbFasilitas;
    private JComboBox cbKelasKamar;
    private JTextField tbJumlah;
    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id;
    String id_kelas_kamar;
    String id_fasilitas;
    int jumlah;
    int temp;

    public void addColumn(){
        model.addColumn("ID Detail Fasilitas Kamar");
        model.addColumn("Kelas Kamar");
        model.addColumn("Fasilitas");
        model.addColumn("Jumlah");
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

    public void tampilFasilitas() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxFasilitas";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbFasilitas.addItem(connection.result.getString("nama_fasilitas"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data combo box fasilitas: " + ex);
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

    public void tampilIdFasilitas() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxGetIDFasilitas @cari=?";

            connection.pstat = connection.conn.prepareStatement(sql1);
            connection.pstat.setString(1, String.valueOf(cbFasilitas.getSelectedItem()));
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                id_fasilitas = (connection.result.getString("id_fasilitas"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mendapatkan Id Fasilitas: " + ex);
        }
    }

    public void loadData() {
        model = new DefaultTableModel();
        tblMasterDetailFasilitas.setModel(model);
        addColumn();

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadDetailFasilitasKamar";
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
            System.out.println("Terjadi error saat load data detail fasilitas kamar: " +ex);
        }
    }

    public void clear() {
        cbFasilitas.setSelectedItem(null);
        cbKelasKamar.setSelectedItem(null);
        tbJumlah.setText(null);
        tbPencarian.setText(null);
        loadData();

        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    public boolean validasiNull() {
        if (id_kelas_kamar == null || id_fasilitas == null || tbJumlah.getText() == null) {
            return true;
        } else {
            return false;
        }
    }

    public FormDetailFasilitas() {
        tampilFasilitas();
        tampilKelasKamar();
        clear();

        String.valueOf(cbFasilitas.getSelectedItem());
        tampilIdFasilitas();

        String.valueOf(cbKelasKamar.getSelectedItem());
        tampilIdKelasKamar();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String.valueOf(cbFasilitas.getSelectedItem());
                tampilIdFasilitas();

                String.valueOf(cbKelasKamar.getSelectedItem());
                tampilIdKelasKamar();

                try {
                    if (validasiNull()) {
                        throw new Exception("Mohon untuk mengisi seluruh data");
                    } else {
                        try {
                            jumlah = Integer.parseInt(tbJumlah.getText());

                            String query = "EXEC sp_InsertDetailFasilitasKamar @id_kelas_kamar=?, @id_fasilitas=?, @qty=?" ;
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id_kelas_kamar);
                            connection.pstat.setString(2, id_fasilitas);
                            connection.pstat.setInt(3, jumlah);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        } catch (Exception ex) {
                            System.out.println("Error saat insert data detail fasilitas kamar ke database: " + ex);
                        }
                        JOptionPane.showMessageDialog(null, "Insert data detail fasilitas kamar berhasil");
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
                String.valueOf(cbFasilitas.getSelectedItem());
                tampilIdFasilitas();

                String.valueOf(cbKelasKamar.getSelectedItem());
                tampilIdKelasKamar();

                jumlah = Integer.parseInt(tbJumlah.getText());

                try {
                    if (validasiNull()) {
                        throw new Exception("Mohon untuk mengisi seluruh data");
                    } else {
                        try {
                            String query = "EXEC sp_UpdateDetailFasilitasKamar @id_kelas_kamar=?, @id_fasilitas=?, @qty=?, @temp=?, @id_dt_fasilitas=? ";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id_kelas_kamar);
                            connection.pstat.setString(2, id_fasilitas);
                            connection.pstat.setInt(3, jumlah);
                            connection.pstat.setInt(4, temp);
                            connection.pstat.setString(5, id);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        } catch (Exception ex) {
                            System.out.println("Error saat update data detail fasilitas kamar ke database: " + ex);
                        }
                        JOptionPane.showMessageDialog(null, "Update data detail fasilitas kamar berhasil");

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
                jumlah = Integer.parseInt(tbJumlah.getText());
                try {
                    opsi = JOptionPane.showConfirmDialog(null,"Apakah ingin menghapus data?",
                            "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (opsi != 0){
                        JOptionPane.showMessageDialog(null, "Data Detail Fasilitas Kamar Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteFasilitasKamar @id_dt_fasilitas=?, @id_fasilitas=?, @qty=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, id_fasilitas);
                        connection.pstat.setInt(3, jumlah);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data Detail Fasilitas Kamar Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error pada saat delete data detail fasilitas kamar: " + ex);
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
                    String query =  "EXEC sp_CariDetailFasilitasKamar @cari=?";

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
                        JOptionPane.showMessageDialog(null, "Data detail fasilitas kamar tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("Error saat pencarian data detail fasilitas kamar: " +ex);
                    clear();
                }
            }
        });

        tblMasterDetailFasilitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterDetailFasilitas.getSelectedRow();
                id = ((String) model.getValueAt(i, 0));
                cbKelasKamar.setSelectedItem(model.getValueAt(i, 1));
                cbFasilitas.setSelectedItem(model.getValueAt(i, 2));
                tbJumlah.setText((String) model.getValueAt(i, 3));
                String tempp = ((String) model.getValueAt(i, 3));
                temp = Integer.parseInt(tempp);

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
                    String query =  "EXEC sp_CariDetailFasilitasKamar @cari=?";

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
                    System.out.println("Error saat pencarian data detail fasilitas kamar: " +ex);
                    clear();
                }
            }
        });
    }
}
