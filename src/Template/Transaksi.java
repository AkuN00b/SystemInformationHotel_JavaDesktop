package Template;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;

public class Transaksi {
    private JPanel MainPanell;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Pembatas_Tengah;
    private JTextField tbTotalHarga;
    private JPanel JCalendarCheckIn;
    private JPanel JCalendarCheckOut;
    private JPanel TransaksiPanel;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Data;
    private JPanel Panel_Customer;
    private JPanel Panel_Kamar;
    private JTextField tbCustomer;
    private JTable tblCustomer;
    private JTextField tbKamar;
    private JTable tblKamar;
    private JPanel Panel_Tombol;
    private JButton simpanButton;
    private JButton editButton;
    private JButton hapusButton;
    private JButton batalButton;

    JDateChooser datechos = new JDateChooser();

    public Transaksi() {
        JCalendarCheckIn.add(datechos);
        JCalendarCheckOut.add(datechos);
    }
}
