package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class FormRole extends JFrame {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JTextField tbNamaRole;
    private JPanel Panel_Tombol;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JPanel Panel_Tabel;
    private JTable tblMasterRole;

    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id;
    String nama;

    //validasi Jika Kosong
    public boolean validasiNull() {
        if (nama.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void addColumn(){
        model.addColumn("ID Role");
        model.addColumn("Nama Role");
    }

    public void clear(){
        loadData();
        tbNamaRole.setText(null);
        tbPencarian.setText(null);
        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    public void loadData() {
        model = new DefaultTableModel();
        tblMasterRole.setModel(model);
        addColumn();

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadRole";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString("id_role");
                obj[1] = connection.result.getString("nama_role");
                model.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data role: " +ex);
        }
    }

    public FormRole() {
        clear();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNamaRole.getText();
                Boolean found = false;

                // Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[1];
                obj[0] = tbNamaRole.getText();

                int j = tblMasterRole.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[0].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }

                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama Role Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertRole @nama_role=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data ke database" + ex);
                            }

                            JOptionPane.showMessageDialog(null, "Insert Data Role Berhasil");
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
                nama = tbNamaRole.getText();
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang Sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = tbNamaRole.getText();

                int j = tblMasterRole.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if (obj[1].equals(model.getValueAt(k, 1))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Nama role Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_UpdateRole @id_role=?, @nama_role=? ";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, nama);

                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data role ke database" + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Update data role Berhasil");

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
                    opsi = JOptionPane.showConfirmDialog(null,"Apakah ingin menghapus data role?",
                            "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (opsi != 0){
                        JOptionPane.showMessageDialog(null, "Data Role Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteRole @id_role =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data Role Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data role: " + ex);
                }
                clear();
            }
        });

        tblMasterRole.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterRole.getSelectedRow();
                id = ((String) model.getValueAt(i,0));
                tbNamaRole.setText((String) model.getValueAt(i,1));

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

                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariRole @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[2];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        model.addRow(obj);
                    }

                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Data role tidak ditemukan");
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data role: " + e);
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

                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariRole @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    while (connection.result.next()) {
                        Object[] obj = new Object[2];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        model.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi Error saat cari data role: " + e);
                    clear();
                }
            }
        });
    }
}
