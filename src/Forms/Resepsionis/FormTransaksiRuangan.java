package Forms.Resepsionis;

import Database.DBConnect;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class FormTransaksiRuangan {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Bawah;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Tengah;
    private JPanel TransaksiPanel;
    private JPanel Panel_Kontrol;
    private JTextField tbTotalHarga;
    private JPanel JCalendarCheckIn;
    private JPanel JCalendarCheckOut;
    private JPanel Panel_Tombol;
    private JButton simpanButton;
    private JButton batalButton;
    private JButton checkOutButton;
    private JPanel Panel_Data;
    private JPanel Panel_Customer;
    private JTextField tbCustomer;
    private JTable tblCustomer;
    private JPanel Panel_Kamar;
    private JTextField tbRuangan;
    private JTable tblRuangan;
    private JPanel Pabel_Transaksi;
    private JPanel Panel_Data_CheckIn;
    private JTable tblTrsRuangan;
    private JTextField tbTrsRuangan;

    private DefaultTableModel modelCustomer;
    private DefaultTableModel modelRuangan;
    private DefaultTableModel modelCheckin;

    DBConnect connection = new DBConnect();
    JDateChooser datechoos = new JDateChooser();
    JDateChooser datechooss = new JDateChooser();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();

    String id, id_ruangan, id_customer, id_trs_ruangan, hari, harga_ruangan, id_receptionist, total;
    double total_harga;
    int differenceInDays, isEdit;
    String[] user;

    public FormTransaksiRuangan(String[] data) {
        clear();
        JCalendarCheckIn.add(datechooss);
        JCalendarCheckOut.add(datechoos);

        user = data;
        id_receptionist = user[1];


        batalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        tblCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int i = tblCustomer.getSelectedRow();
                id_customer = ((String) modelCustomer.getValueAt(i, 0));
                tbCustomer.setText((String) modelCustomer.getValueAt(i, 1));

                tblCustomer.setEnabled(false);
                tbCustomer.setEnabled(false);
            }
        });

        tblRuangan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                tblCustomer.setEnabled(false);
                tbRuangan.setEnabled(false);
                tbTrsRuangan.setEnabled(false);

                isEdit = 1;

                int i = tblRuangan.getSelectedRow();
                id_ruangan = ((String) modelRuangan.getValueAt(i, 0));
                tbRuangan.setText((String) modelRuangan.getValueAt(i,0) + " - " + (String) modelRuangan.getValueAt(i,1));

                String nilai_ribuan = (String) modelRuangan.getValueAt(i,2);
                String nilai_angka = nilai_ribuan.replace("Rp. ", "");
                nilai_angka = nilai_angka.replace(",", "");
                total_harga = Long.parseLong(nilai_angka) / 100;

                tblRuangan.setEnabled(false);
                tbRuangan.setEnabled(false);

                if (differenceInDays == 0) {

                } else {
                    if (simpanButton.isEnabled()) {
                        double aa = total_harga * differenceInDays;
                        long ab = new Double(aa).longValue();
                        String nilai_ribuann = String.valueOf(ab);
                        try {
                            String sbyr = nilai_ribuann.replaceAll("\\,", "");
                            double dblByr = Double.parseDouble(sbyr);
                            DecimalFormat df = new DecimalFormat("#,###,###");
                            if (dblByr > 999) {
                                tbTotalHarga.setText(df.format(dblByr));
                            }
                        }catch (Exception ex){

                        }
                    }
                }

            }
        });

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "EXEC sp_UpdateTrsRuangan @id_tr_ruangan=?, @id_ruangan=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, id_trs_ruangan);
                    connection.pstat.setString(2, id_ruangan);
                    connection.pstat.executeUpdate();
                    connection.pstat.close();
                } catch (Exception ex) {
                    System.out.println("Error saat check out ke database: " + ex);
                }
                JOptionPane.showMessageDialog(null, "Data transaksi berhasil berubah menjadi check out");

                clear();
            }
        });

        tblTrsRuangan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                simpanButton.setEnabled(false);
                tblCustomer.setEnabled(false);
                tblRuangan.setEnabled(false);
                tbCustomer.setEnabled(false);
                tbRuangan.setEnabled(false);
                checkOutButton.setEnabled(true);
                isEdit = 1;

                int i = tblTrsRuangan.getSelectedRow();
                id_trs_ruangan = ((String) modelCheckin.getValueAt(i, 0));
                tbCustomer.setText((String) modelCheckin.getValueAt(i, 1));
                id_ruangan = (String) modelCheckin.getValueAt(i, 3);
                tbRuangan.setText((String) modelCheckin.getValueAt(i, 3) + " - " + (String) modelCheckin.getValueAt(i, 4));
                tbTotalHarga.setText((String) modelCheckin.getValueAt(i, 8));
                try {
                    Date chkIn = new SimpleDateFormat("yyyy-MM-dd").parse((String) modelCheckin.getValueAt(i, 6));
                    datechooss.setDate(chkIn);

                    Date chkOut = new SimpleDateFormat("yyyy-MM-dd").parse((String) modelCheckin.getValueAt(i, 7));
                    datechoos.setDate(chkOut);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nilai_ribuan = tbTotalHarga.getText();

                if (total_harga == 0) {
                    JOptionPane.showMessageDialog(null, "Mohon untuk mengisi seluruh data", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    String ttotal = nilai_ribuan.replace("Rp. ", "");
                    total = ttotal.replace(",", "");

                    long aa = Long.parseLong(total);
                    aa /= 100;

                    total = String.valueOf(aa);
                    hari = String.valueOf(differenceInDays);

                    int status = 1;
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertTransaksiRuangan @id_transaksi_ruangan=?, @id_customer=?, @id_receptionist=?, @id_ruangan=?, @tgl_transaksi=?, @check_in=?, @check_out=?, @total_harga=?, @status_transaksi=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, autoID());
                                connection.pstat.setString(2, id_customer);
                                connection.pstat.setString(3, id_receptionist);
                                connection.pstat.setString(4, id_ruangan);
                                connection.pstat.setString(5, formatter.format(new Date()));
                                connection.pstat.setString(6, formatter.format(datechooss.getDate()));
                                connection.pstat.setString(7, formatter.format(datechoos.getDate()));
                                connection.pstat.setString(8, total);
                                connection.pstat.setInt(9, status);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat insert data transaksi ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Data transaksi ruangan berhasil disimpan !");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }

                clear();
            }
        });

        tbTotalHarga.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);

                LocalDateTime from = LocalDateTime.ofInstant(datechooss.getDate().toInstant(), ZoneId.systemDefault());
                LocalDateTime to = LocalDateTime.ofInstant(datechoos.getDate().toInstant(), ZoneId.systemDefault());

                Duration d = Duration.between(from, to);
                differenceInDays = (int) d.toDays();

                if (differenceInDays < 1 && differenceInDays != 0) {
                    JOptionPane.showMessageDialog(null, "Tanggal tidak sesuai");
                    datechoos.setDate(date);
                    datechooss.setDate(date);
                } else {
                    if (simpanButton.isEnabled()) {
                        double aa = total_harga * differenceInDays;
                        long ab = new Double(aa).longValue();
                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');

                        kursIndonesia.setDecimalFormatSymbols(formatRp);

                        String vv = kursIndonesia.format(ab);
                        tbTotalHarga.setText(vv);
                    }
                }
            }
        });

        tbCustomer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                modelCustomer.getDataVector().removeAllElements(); //menghapus semua data ditamp
                modelCustomer.fireTableDataChanged(); // memberitahu data telah ksong

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariCustomer @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbCustomer.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[3];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        modelCustomer.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data customer: " +ex);
                    clear();
                }
            }
        });

        tbRuangan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                modelRuangan.getDataVector().removeAllElements(); //menghapus semua data ditamp
                modelRuangan.fireTableDataChanged(); // memberitahu data telah ksong

                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariRuanganTransaksi @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbRuangan.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[6];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = kursIndonesia.format(connection.result.getInt(3));
                        obj[3] = connection.result.getString(4);
                        modelRuangan.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data ruangan: " + ex);
                    clear();
                }
            }
        });
        tbTrsRuangan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                modelCheckin.getDataVector().removeAllElements(); //menghapus semua data ditamp
                modelCheckin.fireTableDataChanged(); // memberitahu data telah ksong

                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariTransaksiRuangan @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbTrsRuangan.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbaris data
                    while (connection.result.next()) {
                        Object[] obj = new Object[10];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        obj[4] = connection.result.getString(5);
                        obj[5] = connection.result.getString(6);
                        obj[6] = connection.result.getString(7);
                        obj[7] = connection.result.getString(8);
                        obj[8] = kursIndonesia.format(connection.result.getInt(9));
                        obj[9] = connection.result.getString(10);
                        modelCheckin.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data transaksi kamar: " +ex);
                    clear();
                }

            }
        });
    }


    public boolean validasiNull() {
        if (total.isEmpty() || id_customer.isEmpty() || id_ruangan.isEmpty() || hari.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private String autoID() {
        String maxID = null;
        int idmm = 0;
        String id = null;

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_IDTrsRuangan3";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                maxID = connection.result.getString("ID");
            }

            connection.stat.close();
            connection.result.close();

            if (maxID != null) {
                //Substring digunakan untuk memecah string id_ jadi JM + 005
                //Substring(2, 5) maksudnya, mulai dari digit ke 3 diambil 3 karakter (0, 0 dan 5)
                //Kemudian di convert ke int, sehingga 5 + 1 = 6
                idmm = Integer.parseInt(maxID.substring(3, 6)) + 1;
                //Jika id nya kurang dari 10
                if (idmm < 10) {
                    id = "TRU00" + idmm;
                }
                //Jika id nya lebih dari sama dengan 10 dan kurang dari 100 (10 - 99)
                else if (idmm >= 10 && idmm < 100) {
                    id = "TRU0" + idmm;
                } else {
                    id = "TRU" + idmm;
                }
            }
            //Jika maxid kosong
            else {
                id = "FS001";
            }
        } catch (Exception e) {
            JOptionPane.showInputDialog("Terjadi error saat load data id otomatis transaksi kamar: " + e);
        }

        return id;
    }

    public void addColumnCustomer() {
        modelCustomer.addColumn("ID Customer");
        modelCustomer.addColumn("Nama Customer");
        modelCustomer.addColumn("Email Customer");
    }

    public void addColumnRuangan() {
        modelRuangan.addColumn("ID Ruangan");
        modelRuangan.addColumn("Nama Ruangan");
        modelRuangan.addColumn("Harga Ruangan");
        modelRuangan.addColumn("Status Ruangan");
    }

    public void addColumnCheckIn() {
        modelCheckin.addColumn("ID Transaksi Ruangan");
        modelCheckin.addColumn("Customer");
        modelCheckin.addColumn("Resepsionis");
        modelCheckin.addColumn("Ruangan");
        modelCheckin.addColumn("Nama Ruangan");
        modelCheckin.addColumn("Tanggal Transaksi");
        modelCheckin.addColumn("Check In");
        modelCheckin.addColumn("Check Out");
        modelCheckin.addColumn("Total Harga");
        modelCheckin.addColumn("Status");
    }

    public void clear() {
        tbTrsRuangan.setText(null);
        tbCustomer.setText(null);
        tbRuangan.setText(null);
        tbTotalHarga.setText(null);

        tbCustomer.setEnabled(true);
        tbRuangan.setEnabled(true);
        tbTrsRuangan.setEnabled(true);

        total_harga = 0;
        differenceInDays = 0;
        isEdit = 0;

        datechoos.setDate(date);
        datechooss.setDate(date);

        simpanButton.setEnabled(true);
        checkOutButton.setEnabled(false);

        tblRuangan.setEnabled(true);
        tblCustomer.setEnabled(true);
        tblTrsRuangan.setEnabled(true);

        loadDataCustomer();
        loadDataRuangan();
        loadDataTransaksiRuangan();
    }

    public void loadDataCustomer() {
        modelCustomer = new DefaultTableModel();
        tblCustomer.setModel(modelCustomer);
        addColumnCustomer();

        modelCustomer.getDataVector().removeAllElements();
        modelCustomer.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadCustomer";

            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                Object[] obj = new Object[3];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                modelCustomer.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data customer: " + ex);
        }
    }

    public void loadDataRuangan() {
        modelRuangan = new DefaultTableModel();
        tblRuangan.setModel(modelRuangan);
        addColumnRuangan();

        modelRuangan.getDataVector().removeAllElements();
        modelRuangan.fireTableDataChanged();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadRuanganTransaksi";

            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = kursIndonesia.format(connection.result.getInt(3));
                obj[3] = connection.result.getString(4);
                modelRuangan.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data kamar: " + ex);
        }
    }

    public void loadDataTransaksiRuangan() {
        modelCheckin = new DefaultTableModel();
        tblTrsRuangan.setModel(modelCheckin);
        addColumnCheckIn();

        modelCheckin.getDataVector().removeAllElements();
        modelCheckin.fireTableDataChanged();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadTransaksiRuangan";

            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                Object[] obj = new Object[10];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = connection.result.getString(5);
                obj[5] = connection.result.getString(6);
                obj[6] = connection.result.getString(7);
                obj[7] = connection.result.getString(8);
                obj[8] = kursIndonesia.format(connection.result.getInt(9));
                obj[9] = connection.result.getString(10);

                modelCheckin.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data transaksi kamar: " + ex);
        }

    }
}

