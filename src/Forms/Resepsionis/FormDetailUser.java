package Forms.Resepsionis;

import Database.DBConnect;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class FormDetailUser {
    public JPanel MainPanel;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JTextField tbNama;
    private JTextField tbNIK;
    private JPanel Panel_Tombol;
    private JButton btnTambah;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JButton btnUpdate;
    private JPanel Panel_Tabel;
    private JTable tblDetailUser;
    private JPanel dpTglLahir;
    private JTextArea tbAlamat;
    private JTextField tbUmur;
    private DefaultTableModel model;

    DBConnect connection = new DBConnect();
    JDateChooser datechoos = new JDateChooser();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String nama, nik, alamat, id;
    Date date = new Date();

    public FormDetailUser() {
        clear();
        dpTglLahir.add(datechoos);

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = tbNama.getText();

                // stores each characters to a char array
                char[] charArray = nama.toCharArray();
                boolean foundSpace = true;

                for (int i = 0; i < charArray.length; i++) {
                    // if the array element is a letter
                    if (Character.isLetter(charArray[i])) {
                        // check space is present before the letter
                        if(foundSpace) {
                            // change the letter into uppercase
                            charArray[i] = Character.toUpperCase(charArray[i]);
                            foundSpace = false;
                        }
                    } else {
                        // if the new character is not character
                        foundSpace = true;
                    }
                }

                // convert the char array to the string
                nama = String.valueOf(charArray);
                nik = tbNIK.getText();
                alamat = tbAlamat.getText();

                try {
                    if (validasiNull()) {
                        throw new Exception("Mohon untuk mengisi seluruh data");
                    } else {
                        try {
                            String query = "EXEC sp_InsertDetailUser @nama=?, @nik=?, @tanggal_lahir=?, @alamat=?" ;
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, nama);
                            connection.pstat.setString(2, nik);
                            connection.pstat.setString(3, formatter.format(datechoos.getDate()));
                            connection.pstat.setString(4, alamat);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        } catch (Exception ex) {
                            System.out.println("Error saat insert data detail user ke database: " + ex);
                        }
                        JOptionPane.showMessageDialog(null, "Insert data detail user berhasil");
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
                nama = tbNama.getText();
                nik = tbNIK.getText();
                alamat = tbAlamat.getText();

                try {
                    if (validasiNull()) {
                        throw new Exception("Mohon untuk mengisi seluruh data");
                    } else {
                        try {
                            String query = "EXEC sp_UpdateDetailUser @nama=?, @nik=?, @tanggal_lahir=?, @alamat=?, @id=?" ;
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, nama);
                            connection.pstat.setString(2, nik);
                            connection.pstat.setString(3, formatter.format(datechoos.getDate()));
                            connection.pstat.setString(4, alamat);
                            connection.pstat.setString(5, id);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        } catch (Exception ex) {
                            System.out.println("Error saat update data detail user ke database: " + ex);
                        }
                        JOptionPane.showMessageDialog(null, "Update data detail user berhasil");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
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
                        JOptionPane.showMessageDialog(null, "Data Detail User Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteDetailUser @id =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data Detail User Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error pada saat delete data detail user: " + ex);
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
                    String query =  "EXEC sp_CariDetailUser @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[5];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        obj[4] = connection.result.getString(5);
                        model.addRow(obj);
                    }

                    if(model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Data detail user tidak ditemukan");
                    }

                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("Error saat pencarian data detail user: " +ex);
                    clear();
                }
            }
        });
        tblDetailUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblDetailUser.getSelectedRow();
                id = ((String) model.getValueAt(i, 0));
                tbNama.setText((String) model.getValueAt(i, 1));
                tbNIK.setText((String) model.getValueAt(i, 2));
                try {
                    Date tglLahir = new SimpleDateFormat("yyyy-MM-dd").parse((String) model.getValueAt(i, 3));
                    datechoos.setDate(tglLahir);

                    LocalDate today = LocalDate.now();
                    LocalDate birthday = LocalDate.from(LocalDateTime.ofInstant(datechoos.getDate().toInstant(), ZoneId.systemDefault()));

                    Period period = Period.between(birthday, today);
                    tbUmur.setText(String.valueOf(period.getYears()) + " Tahun");
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                tbAlamat.setText((String) model.getValueAt(i, 4));

                btnTambah.setEnabled(false);
                btnHapus.setEnabled(true);
                btnUpdate.setEnabled(true);
            }
        });

        tbNama.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                validasiHuruf(e);
            }
        });
        tbAlamat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                LocalDate today = LocalDate.now();
                LocalDate birthday = LocalDate.from(LocalDateTime.ofInstant(datechoos.getDate().toInstant(), ZoneId.systemDefault()));

                Period period = Period.between(birthday, today);
                tbUmur.setText(String.valueOf(period.getYears()) + " Tahun");
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
                    String query =  "EXEC sp_CariDetailUser @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[5];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        obj[4] = connection.result.getString(5);
                        model.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("Error saat pencarian data detail user: " +ex);
                    clear();
                }
            }
        });
    }

    public void addColumn(){
        model.addColumn("ID User");
        model.addColumn("Nama User");
        model.addColumn("NIK User");
        model.addColumn("Tanggal Lahir");
        model.addColumn("Alamat");
    }

    public void clear() {
        tbNama.setText(null);
        tbNIK.setText(null);
        tbUmur.setText(null);
        tbAlamat.setText(null);
        datechoos.setDate(date);
        loadData();

        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    public void loadData() {
        model = new DefaultTableModel();
        tblDetailUser.setModel(model);
        addColumn();

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadDetailUser";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = connection.result.getString(5);
                model.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data detail user: " +ex);
        }
    }

    public boolean validasiNull(){
        if (nama.isEmpty() || nik.isEmpty() || alamat.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    //validasi hanya memasukkan huruf
    void validasiHuruf(KeyEvent b) {
        if (Character.isDigit(b.getKeyChar())){
            b.consume();
        }
    }
}
