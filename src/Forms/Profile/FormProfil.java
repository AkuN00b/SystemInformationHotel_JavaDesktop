package Forms.Profile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormProfil {
    public JPanel MainPanel;
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Tengah;
    private JPanel Panel_Tengahh;
    private JRadioButton editProfileRadioButton;
    private JRadioButton editPasswordRadioButton;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Konten;

    public FormProfil(String[] data) {
        editProfileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormEditProfile show = new FormEditProfile(data);
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        editPasswordRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormEditPassword show = new FormEditPassword(data);
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });
    }
}
