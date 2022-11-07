package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class FormJenisMenu {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JTextField tbNamaJenisMenu;
    private JTextArea tbDeskripsiJenisMenu;
    private JPanel Panel_Tombol;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JPanel Panel_Tabel;
    private JTable tblMasterJenisMenu;

    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id;
    String nama;
    String deskripsi;

    public FormJenisMenu() {
        clear();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaJenisMenu.getText();
                deskripsi = tbDeskripsiJenisMenu.getText();
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = tbNamaJenisMenu;
                obj[1] = tbDeskripsiJenisMenu.getText();

                int j = tblMasterJenisMenu.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama Jenis Menu Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertJenisMenu @nama_jenis_menu=?, @deskripsi_jenis_menu=? ";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.setString(2, deskripsi);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat insert data jenis menu ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Insert data berhasil");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    clear();
                }
            }
        });

        tbNamaJenisMenu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiHuruf(e);
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaJenisMenu.getText();
                deskripsi = tbDeskripsiJenisMenu.getText();
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[3];
                obj[0] = id;
                obj[1] = tbNamaJenisMenu.getText();
                obj[2] = tbDeskripsiJenisMenu.getText();

                int j = tblMasterJenisMenu.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 2))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama jenis menu Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_UpdateJenisMenu @nama_jenis_menu=?, @deskripsi_jenis_menu=?, @id_jenis_menu=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.setString(2, deskripsi);
                                connection.pstat.setString(3, id);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data jenis menu ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Update data jenis menu Berhasil");

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
                        String query = "EXEC sp_DeleteJenisMakanan @id_jenis_menu =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data jenis menu Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error saat hapus data jenis menu: " + ex);
                }
                clear();
            }
        });

        tblMasterJenisMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterJenisMenu.getSelectedRow();
                id = ((String) model.getValueAt(i,0));
                tbNamaJenisMenu.setText((String) model.getValueAt(i,1));
                tbDeskripsiJenisMenu.setText((String) model.getValueAt(i,2));

                btnUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
                btnTambah.setEnabled(false);
            }
        });

        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong
                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariJenisMenu @cari=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[3];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);

                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Data jenis menu tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data jenis menu: " + e);
                }
            }
        });

        tbNamaJenisMenu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiHuruf(e);
            }
        });
        tbPencarian.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong
                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariJenisMenu @cari=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[3];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);

                        model.addRow(obj);
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data jenis menu: " + e);
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

    public void addColumn(){
        model.addColumn("ID Jenis Menu");
        model.addColumn("Nama Jenis Menu");
        model.addColumn("Deskripsi Jenis Menu");
    }

    public boolean validasiNull(){
        if(nama.isEmpty() || deskripsi.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    public void loadData(){
        model = new DefaultTableModel();
        tblMasterJenisMenu.setModel(model);
        addColumn();

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadJenisMenu";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[3];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data jenis menu: " +ex);
        }
    }

    public void clear(){
        tbNamaJenisMenu.setText(null);
        tbDeskripsiJenisMenu.setText(null);
        tbPencarian.setText(null);
        loadData();

        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
    }
}
