package Forms.Resepsionis;

import Database.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormUser {
    public JPanel MainPanel;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Tabel;
    private JPanel Panel_Form;
    private JPanel Panel_Tombol;
    private JTextField tbNama;
    private JTextField tbEmail;
    private JPasswordField tbPassword;
    private JComboBox cbRole;
    private JCheckBox cbVisible;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JTextField tbPencarian;
    private JButton btnCari;
    private JButton btnRefresh;
    private JTable tblMasterUser;
    private JComboBox cbNamaUser;
    private DefaultTableModel model;
    DBConnect connection = new DBConnect();

    String id;
    String idRole;
    String idUser;
    String nama;
    String email;
    String password;

    public void addColumn(){
        model.addColumn("ID User");
        model.addColumn("Username");
        model.addColumn("Email");
        model.addColumn("Password");
        model.addColumn("Password");
        model.addColumn("Nama Role");
        model.addColumn("Nama User");
    }

    public void tampilRole() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxRole";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbRole.addItem(connection.result.getString("nama_role"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data combo box role: " + ex);
        }
    }

    public void tampilIdRole() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxGetIDRole @cari=?";

            connection.pstat = connection.conn.prepareStatement(sql1);
            connection.pstat.setString(1, String.valueOf(cbRole.getSelectedItem()));
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                idRole = (connection.result.getString("id_role"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mendapatkan Id Role: " + ex);
        }
    }

    public void tampilUser() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxDetailUser";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbNamaUser.addItem(connection.result.getString("nama"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data combo box detail user: " + ex);
        }
    }

    public void tampilIdUser() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "EXEC sp_ComboBoxGetIDUser @cari=?";

            connection.pstat = connection.conn.prepareStatement(sql1);
            connection.pstat.setString(1, String.valueOf(cbNamaUser.getSelectedItem()));
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                idUser = (connection.result.getString("id"));
            }

            connection.stat.close();
            connection.result.close();

        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mendapatkan Id Detail User: " + ex);
        }
    }

    private String mask (int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i< length; i++) {
            sb.append('*');
        }

        return new String(sb);
    }

    public void loadData() {
        model = new DefaultTableModel();
        tblMasterUser.setModel(model);
        addColumn();

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadUser";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                String a = connection.result.getString(4);
                Object[] obj = new Object[7];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = mask(a.length());
                obj[5] = connection.result.getString(5);
                obj[6] = connection.result.getString(6);
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnTambah.setEnabled(true);
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data user: " +ex);
        }
    }

    public void clear(){
        tbNama.setText(null);
        tbEmail.setText(null);
        tbPassword.setText(null);
        tbPencarian.setText(null);
        cbVisible.setSelected(false);
        cbRole.setSelectedItem(null);
        cbNamaUser.setSelectedItem(null);
        loadData();

        tblMasterUser.getColumnModel().getColumn(3).setMaxWidth(0);
        tblMasterUser.getColumnModel().getColumn(3).setMinWidth(0);
        tblMasterUser.getColumnModel().getColumn(3).setWidth(0);
        tblMasterUser.getColumnModel().getColumn(3).setPreferredWidth(0);

        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    //validasi Jika Kosong
    public boolean validasiNull(){
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || idRole.isEmpty()) {
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

    private String autoID()
    {
        String maxID = null;
        int idmm = 0;
        String id = null;

        try
        {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_IDRole3";
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
                idmm = Integer.parseInt(maxID.substring(3, 6))+1;
                //Jika id nya kurang dari 10
                if (idmm < 10)
                {
                    id = "USR00" + idmm;
                }
                //Jika id nya lebih dari sama dengan 10 dan kurang dari 100 (10 - 99)
                else if (idmm >= 10 && idmm < 100)
                {
                    id = "USR0" + idmm;
                }
                else
                {
                    id = "USR" + idmm;
                }
            }
            //Jika maxid kosong
            else
            {
                id = "MM001";
            }
        } catch(Exception e)
        {
            JOptionPane.showInputDialog("Terjadi error saat load id data User: " + e);
        }
        return id;
    }

    public FormUser() {
        tampilRole();
        tampilUser();
        clear();

        String.valueOf(cbRole.getSelectedItem());
        String.valueOf(cbNamaUser.getSelectedItem());
        tampilIdRole();
        tampilIdUser();

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String.valueOf(cbRole.getSelectedItem());
                String.valueOf(cbNamaUser.getSelectedItem());
                tampilIdRole();
                tampilIdUser();

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

                email = tbEmail.getText();
                password = tbPassword.getText();

                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(tbEmail.getText());

                if (tbNama.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(MainPanel, "Mohon untuk mengisi seluruh data", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
                else if (!matcher.matches()) {
                    JOptionPane.showMessageDialog(MainPanel, "Email Tidak Sesuai Format !", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
                else if (tbPassword.getText().length() < 7) {
                    JOptionPane.showMessageDialog(MainPanel, "Password anda kurang dari 8 karakter !", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_InsertUserAutoID @id_user=?, @nama_user=?, @email_user=?, @password=?, @id_role=? " ;
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, idUser);
                                connection.pstat.setString(2, nama);
                                connection.pstat.setString(3, email);
                                connection.pstat.setString(4, password);
                                connection.pstat.setString(5, idRole);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat insert data user ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Insert data user berhasil");
                            clear();

                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }

            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String.valueOf(cbRole.getSelectedItem());
                String.valueOf(cbNamaUser.getSelectedItem());
                tampilIdRole();
                tampilIdUser();

                nama = tbNama.getText();
                email = tbEmail.getText();
                password = tbPassword.getText();

                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(tbEmail.getText());

                if (tbNama.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(MainPanel, "Mohon untuk mengisi seluruh data", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }else if (!matcher.matches()) {
                    JOptionPane.showMessageDialog(MainPanel, "Email Tidak Sesuai Format !", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
                else if (tbPassword.getText().length() < 7) {
                    JOptionPane.showMessageDialog(MainPanel, "Password anda kurang dari 8 karakter !", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Mohon untuk mengisi seluruh data");
                        } else {
                            try {
                                String query = "EXEC sp_UpdateUser @nama_user=?, @email_user=?, @password=?, @id_role=?, @id_user=?, @id_dt_user=? ";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, nama);
                                connection.pstat.setString(2, email);
                                connection.pstat.setString(3, password);
                                connection.pstat.setString(4, idRole);
                                connection.pstat.setString(5, id);
                                connection.pstat.setString(6, idUser);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                            } catch (Exception ex) {
                                System.out.println("Error saat update data user ke database: " + ex);
                            }
                            JOptionPane.showMessageDialog(null, "Update data user berhasil");
                            clear();

                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null,ex.getMessage());
                    }
                }

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
                        JOptionPane.showMessageDialog(null, "Data User Tidak Berhasil Dihapus");
                    }else {
                        String query = "EXEC sp_DeleteUser @id_user =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data User Berhasil Dihapus");
                    }
                } catch (Exception ex) {
                    System.out.println("Error pada saat delete data user: " + ex);
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
                    String query =  "EXEC sp_CariUser @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
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
                        model.addRow(obj);
                    }

                    if(model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Data user tidak ditemukan");
                    }

                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("Error saat pencarian data user: " +ex);
                    clear();
                }
            }
        });

        tblMasterUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMasterUser.getSelectedRow();
                id = ((String) model.getValueAt(i, 0));
                tbNama.setText((String) model.getValueAt(i, 1));
                tbEmail.setText((String) model.getValueAt(i, 2));
                tbPassword.setText((String) model.getValueAt(i, 3));
                cbRole.setSelectedItem(model.getValueAt(i, 5));
                cbNamaUser.setSelectedItem(model.getValueAt(i, 6));
                cbVisible.setSelected(false);

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

        cbVisible.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbVisible.isSelected() == true) {
                    tbPassword.setEchoChar('\0');
                } else {
                    tbPassword.setEchoChar('*');
                }
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
                    String query =  "EXEC sp_CariUser @cari=?";

                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, tbPencarian.getText());
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
                        model.addRow(obj);
                    }

                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("Error saat pencarian data user: " +ex);
                    clear();
                }
            }
        });
    }
}
