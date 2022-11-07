package Forms.Resepsionis;

import Database.DBConnect;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.text.*;
import java.time.*;
import java.util.Date;

import static java.time.LocalTime.from;

public class FormTransaksiKamar {
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
    private JPanel Panel_Data;
    private JPanel Panel_Customer;
    private JTextField tbCustomer;
    private JTable tblCustomer;
    private JTextField tbKamar;
    private JTable tblKamar;
    private JTable tblTrsKamar;
    private JTextField tbTrsKamar;
    private JButton checkOutButton;
    private JPanel Panel_Kamar;
    private JPanel Pabel_Transaksi;
    private JPanel Panel_Data_CheckIn;

    private DefaultTableModel modelCustomer;
    private DefaultTableModel modelKamar;
    private DefaultTableModel modelCheckIn;
    DBConnect connection = new DBConnect();
    JDateChooser datechoos = new JDateChooser();
    JDateChooser datechooss = new JDateChooser();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();

    String id, id_kamar, id_customer, hari, id_receptionist, harga_kelas, harga_jenis, id_trs_kamar, total, tagihan;
    double total_tagihan, total_harga;
    long differenceInDays;
    int isEdit;
    String[] user;

    public FormTransaksiKamar(String[] data) {
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
                id_customer = ((String) modelCustomer.getValueAt(i,0));
                tbCustomer.setText((String) modelCustomer.getValueAt(i,1));

                tblCustomer.setEnabled(false);
                tbCustomer.setEnabled(false);
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

        tblTrsKamar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                simpanButton.setEnabled(false);
                tblCustomer.setEnabled(false);
                tblKamar.setEnabled(false);
                tbCustomer.setEnabled(false);
                tbKamar.setEnabled(false);
                checkOutButton.setEnabled(true);
                isEdit = 1;

                int i = tblTrsKamar.getSelectedRow();
                id_trs_kamar = ((String) modelCheckIn.getValueAt(i, 0));
                tbCustomer.setText((String) modelCheckIn.getValueAt(i, 1));
                id_kamar = (String) modelCheckIn.getValueAt(i, 3);
                tbKamar.setText((String) modelCheckIn.getValueAt(i, 3) + " - " + (String) modelCheckIn.getValueAt(i, 4) + " - " + (String) modelCheckIn.getValueAt(i, 5));
                tbTotalHarga.setText((String) modelCheckIn.getValueAt(i, 9));

                try {
                    Date chkIn = new SimpleDateFormat("yyyy-MM-dd").parse((String) modelCheckIn.getValueAt(i, 7));
                    datechooss.setDate(chkIn);

                    Date chkOut = new SimpleDateFormat("yyyy-MM-dd").parse((String) modelCheckIn.getValueAt(i, 8));
                    datechoos.setDate(chkOut);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String queryy = "EXEC sp_CekTagihanKamar @id_transaksi=?" ;
                    connection.pstat = connection.conn.prepareStatement(queryy);
                    connection.pstat.setString(1, id_trs_kamar);
                    connection.result = connection.pstat.executeQuery();

                    String a = null;
                    //lakukan baris perbari data
                    while (connection.result.next()) {
                            if (connection.result.getString(1) != null) {
                                tagihan = connection.result.getString(1);
                                a = String.valueOf(tagihan);
                            }
                    }

                    if (a == null) {
                        try {
                            String query = "EXEC sp_UpdateTrsKamar @id_tr_kamar=?, @id_kamar=?" ;
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id_trs_kamar);
                            connection.pstat.setString(2, id_kamar);
                            connection.pstat.executeUpdate();
                        } catch (Exception ex) {
                            System.out.println("Error saat check out ke database: " + ex);
                        }
                        JOptionPane.showMessageDialog(null, "Data transaksi berhasil berubah menjadi check out");
                    } else {
                        int opsi;
                        total_tagihan = Double.parseDouble(tagihan);
                        DecimalFormat df = new DecimalFormat("#,###,###");
                        if (total_tagihan > 999) {
                            tagihan = df.format(total_tagihan);
                        }

                        try {
                            opsi = JOptionPane.showConfirmDialog(null,"Ada tagihan sebesar Rp. " + tagihan + ". Customer sudah membayar?",
                                    "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                            if (opsi != 0){
                                JOptionPane.showMessageDialog(null, "Data transaksi tidak jadi checkout, bayar tagihan terlebih dahulu!");
                            } else {
                                String queryyy = "EXEC sp_BayarTagihan @id_transaksi=?" ;
                                connection.pstat = connection.conn.prepareStatement(queryyy);
                                connection.pstat.setString(1, id_trs_kamar);
                                connection.pstat.executeUpdate();

                                String query = "EXEC sp_UpdateTrsKamar @id_tr_kamar=?, @id_kamar=?" ;
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id_trs_kamar);
                                connection.pstat.setString(2, id_kamar);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                                JOptionPane.showMessageDialog(null, "Data transaksi berubah menjadi checkout, tagihan berhasil dibayar!");
                            }
                        } catch (Exception ex) {
                            System.out.println("Error pada saat checkout ke database: " + ex);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error saat get tagihan dr database: " + ex);
                }

                clear();
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

        tbKamar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                modelKamar.getDataVector().removeAllElements(); //menghapus semua data ditamp
                modelKamar.fireTableDataChanged(); // memberitahu data telah ksong

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariKamarTransaksi @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbKamar.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[6];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        obj[4] = connection.result.getString(5);
                        obj[5] = connection.result.getString(6);
                        modelKamar.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data kamar: " +ex);
                    clear();
                }
            }
        });
        tbTrsKamar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                modelCheckIn.getDataVector().removeAllElements(); //menghapus semua data ditamp
                modelCheckIn.fireTableDataChanged(); // memberitahu data telah ksong

                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query =  "EXEC sp_CariTransaksiKamar @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbTrsKamar.getText());
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbaris data
                    while (connection.result.next()) {
                        Object[] obj = new Object[11];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        obj[4] = connection.result.getString(5);
                        obj[5] = connection.result.getString(6);
                        obj[6] = connection.result.getString(7);
                        obj[7] = connection.result.getString(8);
                        obj[8] = connection.result.getString(9);
                        obj[9] = kursIndonesia.format(connection.result.getInt(10));
                        obj[10] = connection.result.getString(11);
                        modelCheckIn.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data transaksi kamar: " +ex);
                    clear();
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
                                String query = "EXEC sp_InsertTransaksiKamar @id_transaksi_kamar=?, @id_customer=?, @id_receptionist=?, @id_kamar=?, @tgl_transaksi=?, @check_in=?, @check_out=?, @total_harga=?, @status_transaksi=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, autoID());
                                connection.pstat.setString(2, id_customer);
                                connection.pstat.setString(3, id_receptionist);
                                connection.pstat.setString(4, id_kamar);
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

                            JOptionPane.showMessageDialog(null, "Data transaksi kamar berhasil disimpan !");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }

                clear();
            }
        });

        tblKamar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int i = tblKamar.getSelectedRow();
                id_kamar = ((String) modelKamar.getValueAt(i, 0));
                tbKamar.setText((String) modelKamar.getValueAt(i, 0) + " - " + (String) modelKamar.getValueAt(i, 1) + " - " + (String) modelKamar.getValueAt(i, 2));
                harga_jenis = (String) modelKamar.getValueAt(i, 4);
                harga_kelas = (String) modelKamar.getValueAt(i, 5);
                total_harga = Double.parseDouble(harga_jenis) + Double.parseDouble(harga_kelas);

                tblKamar.setEnabled(false);
                tbKamar.setEnabled(false);

                if (differenceInDays == 0) {

                } else {
                    if (simpanButton.isEnabled()) {
                        double aa = total_harga * differenceInDays;
                        long ab = new Double(aa).longValue();
                        String nilai_ribuan = String.valueOf(ab);
                        try {
                            String sbyr = nilai_ribuan.replaceAll("\\,", "");
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
    }

    public boolean validasiNull(){
        if (total.isEmpty() || id_customer.isEmpty() || id_kamar.isEmpty() || hari.isEmpty()) {
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
            String query = "EXEC sp_IDTrsKamar3";
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
                    id = "TR00" + idmm;
                }
                //Jika id nya lebih dari sama dengan 10 dan kurang dari 100 (10 - 99)
                else if (idmm >= 10 && idmm < 100)
                {
                    id = "TR0" + idmm;
                }
                else
                {
                    id = "TR" + idmm;
                }
            }
            //Jika maxid kosong
            else
            {
                id = "FS001";
            }
        } catch(Exception e)
        {
            JOptionPane.showInputDialog("Terjadi error saat load data id otomatis transaksi kamar: " + e);
        }

        return id;
    }

    public void addColumnCustomer() {
        modelCustomer.addColumn("ID Customer");
        modelCustomer.addColumn("Nama Customer");
        modelCustomer.addColumn("Email Customer");
    }

    public void addColumnKamar() {
        modelKamar.addColumn("ID Kamar");
        modelKamar.addColumn("Kelas Kamar");
        modelKamar.addColumn("Jenis Kamar");
        modelKamar.addColumn("Status Kamar");
        modelKamar.addColumn("Harga Jenis Kamar");
        modelKamar.addColumn("Harga Kelas Kamar");

        tblKamar.getColumnModel().getColumn(4).setMinWidth(0);
        tblKamar.getColumnModel().getColumn(4).setMaxWidth(0);
        tblKamar.getColumnModel().getColumn(4).setWidth(0);

        tblKamar.getColumnModel().getColumn(5).setMinWidth(0);
        tblKamar.getColumnModel().getColumn(5).setMaxWidth(0);
        tblKamar.getColumnModel().getColumn(5).setWidth(0);
    }

    public void addColumnCheckIn() {
        modelCheckIn.addColumn("ID Transaksi Kamar");
        modelCheckIn.addColumn("Customer");
        modelCheckIn.addColumn("Resepsionis");
        modelCheckIn.addColumn("Kamar");
        modelCheckIn.addColumn("Kelas Kamar");
        modelCheckIn.addColumn("Jenis Kamar");
        modelCheckIn.addColumn("Tanggal Transaksi");
        modelCheckIn.addColumn("Check In");
        modelCheckIn.addColumn("Check Out");
        modelCheckIn.addColumn("Total Harga");
        modelCheckIn.addColumn("Status");
    }

    public void clear() {
        tbTrsKamar.setText(null);
        tbCustomer.setText(null);
        tbKamar.setText(null);
        tbTotalHarga.setText(null);

        total_harga = 0;
        differenceInDays = 0;
        isEdit = 0;

        datechoos.setDate(date);
        datechooss.setDate(date);

        simpanButton.setEnabled(true);
        checkOutButton.setEnabled(false);

        tblKamar.setEnabled(true);
        tblCustomer.setEnabled(true);
        tblTrsKamar.setEnabled(true);

        loadDataCustomer();
        loadDataKamar();
        loadDataTransaksiKamar();
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
            while (connection.result.next()){
                Object[] obj = new Object[3];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                modelCustomer.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data customer: " +ex);
        }
    }

    public void loadDataKamar() {
        modelKamar = new DefaultTableModel();
        tblKamar.setModel(modelKamar);
        addColumnKamar();

        modelKamar.getDataVector().removeAllElements();
        modelKamar.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadKamarTransaksi";

            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[6];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = connection.result.getString(5);
                obj[5] = connection.result.getString(6);
                modelKamar.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data kamar: " +ex);
        }
    }

    public void loadDataTransaksiKamar() {
        modelCheckIn = new DefaultTableModel();
        tblTrsKamar.setModel(modelCheckIn);
        addColumnCheckIn();

        modelCheckIn.getDataVector().removeAllElements();
        modelCheckIn.fireTableDataChanged();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadTransaksiKamar";

            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[11];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = connection.result.getString(5);
                obj[5] = connection.result.getString(6);
                obj[6] = connection.result.getString(7);
                obj[7] = connection.result.getString(8);
                obj[8] = connection.result.getString(9);
                obj[9] = kursIndonesia.format(connection.result.getInt(10));
                obj[10] = connection.result.getString(11);
                modelCheckIn.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data transaksi kamar: " +ex);
        }
    }
}
