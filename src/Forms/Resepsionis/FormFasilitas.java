package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class FormFasilitas {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JTextField tbNamaFasilitas;
    private JPanel Panel_Tombol;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JPanel Panel_Tabel;
    private JTable tblMasterFasilitas;
    private JTextField tbJumlah;

    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id;
    String nama;
    String jumlah;

    public boolean validasiNull(){
        if (nama.isEmpty() || jumlah.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public FormFasilitas() {
        clear();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaFasilitas.getText();
                jumlah = tbJumlah.getText();
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = tbNamaFasilitas.getText();

                int j = tblMasterFasilitas.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }

                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama Fasilitas Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertFasilitas @id_fasilitas=?, @nama_fasilitas=?, @qty=? ";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, autoID());
                                connection.pstat.setString(2, nama);
                                connection.pstat.setString(3, jumlah);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat insert data fasilitas ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Insert data fasilitas berhasil");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    clear();
                }
            }
        });

        tblMasterFasilitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterFasilitas.getSelectedRow();
                id = ((String) model.getValueAt(i,0));
                tbNamaFasilitas.setText((String) model.getValueAt(i,1));
                tbJumlah.setText((String) model.getValueAt(i,2));

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
                    opsi = JOptionPane.showConfirmDialog(null,"Apakah ingin menghapus data fasilitas?",
                            "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (opsi != 0){
                        JOptionPane.showMessageDialog(null, "Data fasilitas Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteFasilitas @id_fasilitas =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data fasilitas Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data fasilitas: " + ex);
                }
                clear();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaFasilitas.getText();
                jumlah= tbJumlah.getText();
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[3];
                obj[0] = autoID();
                obj[1] = tbNamaFasilitas.getText();
                obj[2] = tbJumlah.getText();


                int j = tblMasterFasilitas.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama fasilitas Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_UpdateFasilitas @nama_fasilitas=?, @qty=?, @id_fasilitas=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.setString(2, jumlah);
                                connection.pstat.setString(3, id);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data fasiitas ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Update data fasilitas Berhasil");
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

        tbJumlah.addKeyListener(new KeyAdapter() {
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

                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariFasilitas @cari=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Data fasilitas tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data fasilitas: " + e);
                }
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
                    String query = "EXEC sp_CariFasilitas @cari=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        model.addRow(obj);
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data fasilitas: " + e);
                }
            }
        });
    }

    void validasiAngka(KeyEvent e){
        char c = e.getKeyChar();
        if (!((c >= '0') && (c <= '9') && tbJumlah.getText().length() < 9
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            e.consume();
        }
    }

    public void loadData(){
        model = new DefaultTableModel();
        tblMasterFasilitas.setModel(model);
        addColumn();

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadFasilitas";

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
            System.out.println("Terjadi error saat load data fasilitas: " +ex);
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
            String query = "EXEC sp_IDFasilitas3";
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
                    id = "FS00" + idmm;
                }
                //Jika id nya lebih dari sama dengan 10 dan kurang dari 100 (10 - 99)
                else if (idmm >= 10 && idmm < 100)
                {
                    id = "FS0" + idmm;
                }
                else
                {
                    id = "FS" + idmm;
                }
            }
            //Jika maxid kosong
            else
            {
                id = "FS001";
            }
        }catch(Exception e)
        {
            JOptionPane.showInputDialog("Terjadi error saat load data fasilitas: " + e);
        }

        return id;
    }

    public void clear(){
        tbNamaFasilitas.setText(null);
        tbJumlah.setText(null);
        tbPencarian.setText(null);
        loadData();

        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    public void addColumn(){
        model.addColumn("ID Fasilitas");
        model.addColumn("Nama Fasilitas");
        model.addColumn("Jumlah Fasilitas");
    }
}
