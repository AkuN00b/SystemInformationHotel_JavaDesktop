package Forms.Resepsionis;

import Forms.Profile.FormEditProfile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormUserMenu {
    private JPanel MainPanell;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Tengah;
    private JPanel Panel_Tengahh;
    private JPanel Panel_Kontrol;
    private JRadioButton editUserRadioButton;
    private JRadioButton editDetailUserRadioButton;
    private JPanel Panel_Konten;
    public JPanel MainPanel;

    public FormUserMenu() {
        editUserRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormUser show = new FormUser();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });

        editDetailUserRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Konten.removeAll();
                Panel_Konten.revalidate();
                Panel_Konten.repaint();
                FormDetailUser show = new FormDetailUser();
                show.MainPanel.setVisible(true);
                Panel_Konten.revalidate();
                Panel_Konten.setLayout(new java.awt.BorderLayout());
                Panel_Konten.add(show.MainPanel);
            }
        });
    }
}
