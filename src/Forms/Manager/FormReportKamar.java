package Forms.Manager;

import Database.DBConnect;
import com.toedter.calendar.JDateChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class FormReportKamar {
    private JButton lihatLaporanButton;
    private JPanel jCalendarFrom;
    private JPanel jCalendarTo;
    public JPanel MainPanel;

    DBConnect connection = new DBConnect();
    JDateChooser datechoos = new JDateChooser();
    JDateChooser datechooss = new JDateChooser();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    public FormReportKamar() {
        jCalendarFrom.add(datechoos);
        jCalendarTo.add(datechooss);

        lihatLaporanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JasperPrint jPrint;

                String dariTanggal = formatter.format(datechoos.getDate());
                String dariSampai = formatter.format(datechooss.getDate());

                Map param = new HashMap();
                param.put("dari", dariTanggal);
                param.put("sampai", dariSampai);

                try {
                    jPrint = JasperFillManager.fillReport("jasper/ReportTrsKamar.jasper", param, connection.conn);
                    JasperViewer viewer = new JasperViewer(jPrint, false);
                    viewer.setTitle("Laporan Transaksi Kamar");
                    viewer.setVisible(true);
                } catch (JRException e2) {
                    System.out.println("Error: " + e2.getMessage());
                }
            }
        });
    }
}
