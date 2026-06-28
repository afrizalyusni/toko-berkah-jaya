package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import config.koneksi;

public class formcustomer extends JFrame {

    private JTextField txtIdCustomer;
    private JTextField txtNama;
    private JTextField txtTelepon;
    private JTextField txtAlamat;
    private JTextField txtCari;

    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnRefresh;

    private JTable tableCustomer;
    private DefaultTableModel tableModel;
    private Connection conn;

    public formcustomer() {
        conn = koneksi.getConnection();
        initComponents();
        loadData();
        generateIdCustomer();
    }

    private void initComponents() {
        setTitle("Data Customer");
        setSize(950, 700);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        // ==================== PANEL FORM (KIRI) ====================
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBounds(15, 20, 320, 600);
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel title = new JLabel("FORM CUSTOMER");
        title.setBounds(65, 20, 220, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelForm.add(title);

        JLabel lblId = new JLabel("ID Customer");
        lblId.setBounds(20, 80, 100, 25);
        panelForm.add(lblId);

        txtIdCustomer = new JTextField();
        txtIdCustomer.setEditable(false);
        txtIdCustomer.setBounds(20, 105, 250, 35);
        panelForm.add(txtIdCustomer);

        JLabel lblNama = new JLabel("Nama Customer");
        lblNama.setBounds(20, 150, 120, 25);
        panelForm.add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(20, 175, 250, 35);
        panelForm.add(txtNama);

        JLabel lblTelepon = new JLabel("No. Telepon");
        lblTelepon.setBounds(20, 220, 120, 25);
        panelForm.add(lblTelepon);

        txtTelepon = new JTextField();
        txtTelepon.setBounds(20, 245, 250, 35);
        panelForm.add(txtTelepon);

        JLabel lblAlamat = new JLabel("Alamat");
        lblAlamat.setBounds(20, 290, 120, 25);
        panelForm.add(lblAlamat);

        txtAlamat = new JTextField();
        txtAlamat.setBounds(20, 315, 250, 35);
        panelForm.add(txtAlamat);

        btnSimpan = new JButton("SIMPAN");
        btnSimpan.setBounds(20, 460, 120, 40);
        panelForm.add(btnSimpan);

        btnUbah = new JButton("UBAH");
        btnUbah.setBounds(150, 460, 120, 40);
        panelForm.add(btnUbah);

        btnHapus = new JButton("HAPUS");
        btnHapus.setBounds(20, 520, 120, 40);
        panelForm.add(btnHapus);

        btnRefresh = new JButton("REFRESH");
        btnRefresh.setBounds(150, 520, 120, 40);
        panelForm.add(btnRefresh);

        add(panelForm);

        // ==================== PANEL TABEL (KANAN) ====================
        JPanel panelTable = new JPanel();
        panelTable.setLayout(null);
        panelTable.setBounds(350, 20, 570, 600);
        panelTable.setBackground(Color.WHITE);
        panelTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lblCari = new JLabel("Cari Customer");
        lblCari.setBounds(20, 20, 100, 25);
        panelTable.add(lblCari);

        txtCari = new JTextField();
        txtCari.setBounds(130, 20, 240, 35);
        panelTable.add(txtCari);

        tableCustomer = new JTable();
        JScrollPane scroll = new JScrollPane(tableCustomer);
        scroll.setBounds(20, 80, 530, 480);
        panelTable.add(scroll);

        add(panelTable);

        // ==================== EVENT LOGIC HANDLERS ====================

        tableCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableCustomer.getSelectedRow();
                if (row != -1) {
                    txtIdCustomer.setText(tableCustomer.getValueAt(row, 0).toString());
                    txtNama.setText(tableCustomer.getValueAt(row, 1).toString());
                    txtAlamat.setText(tableCustomer.getValueAt(row, 2).toString());
                    txtTelepon.setText(tableCustomer.getValueAt(row, 3).toString());
                }
            }
        });

        txtTelepon.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });

        btnSimpan.addActionListener(e -> {
            if (txtNama.getText().trim().isEmpty() || txtTelepon.getText().trim().isEmpty() || txtAlamat.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seluruh form input wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Menggunakan query alternatif dinamis untuk mendeteksi kolom 'telepon' guna menghindari error database
                String sql = "INSERT INTO tb_customer (id_customer, nama_customer, alamat, telepon) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, txtIdCustomer.getText().trim());
                ps.setString(2, txtNama.getText().trim());
                ps.setString(3, txtAlamat.getText().trim());
                ps.setString(4, txtTelepon.getText().trim());

                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Data Customer Berhasil Disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                }
            } catch (Exception ex) {
                try {
                    // Fallback alternatif kedua jika nama kolom pada tabel database Anda adalah 'no_telp'
                    String sqlFallback = "INSERT INTO tb_customer (id_customer, nama_customer, alamat, no_telp) VALUES (?, ?, ?, ?)";
                    PreparedStatement psF = conn.prepareStatement(sqlFallback);
                    psF.setString(1, txtIdCustomer.getText().trim());
                    psF.setString(2, txtNama.getText().trim());
                    psF.setString(3, txtAlamat.getText().trim());
                    psF.setString(4, txtTelepon.getText().trim());
                    
                    if (psF.executeUpdate() > 0) {
                        JOptionPane.showMessageDialog(this, "Data Customer Berhasil Disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                    }
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex2.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUbah.addActionListener(e -> {
            int selectedRow = tableCustomer.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data pada tabel yang ingin diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String sql = "UPDATE tb_customer SET nama_customer=?, alamat=?, telepon=? WHERE id_customer=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, txtNama.getText().trim());
                ps.setString(2, txtAlamat.getText().trim());
                ps.setString(3, txtTelepon.getText().trim());
                ps.setString(4, txtIdCustomer.getText().trim());

                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Data Customer Berhasil Diperbarui!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                }
            } catch (Exception ex) {
                try {
                    String sqlFallback = "UPDATE tb_customer SET nama_customer=?, alamat=?, no_telp=? WHERE id_customer=?";
                    PreparedStatement psF = conn.prepareStatement(sqlFallback);
                    psF.setString(1, txtNama.getText().trim());
                    psF.setString(2, txtAlamat.getText().trim());
                    psF.setString(3, txtTelepon.getText().trim());
                    psF.setString(4, txtIdCustomer.getText().trim());
                    
                    if (psF.executeUpdate() > 0) {
                        JOptionPane.showMessageDialog(this, "Data Customer Berhasil Diperbarui!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                    }
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(this, "Gagal memperbarui data: " + ex2.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnHapus.addActionListener(e -> {
            String id = txtIdCustomer.getText().trim();
            if (tableCustomer.getSelectedRow() == -1 || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data pada tabel yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int konfirmasi = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus customer " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM tb_customer WHERE id_customer=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, id);

                    if (ps.executeUpdate() > 0) {
                        JOptionPane.showMessageDialog(this, "Data Customer Berhasil Dihapus!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRefresh.addActionListener(e -> clearForm());

        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txtCari.getText().trim();
                if (!keyword.isEmpty()) {
                    cariData(keyword);
                } else {
                    loadData();
                }
            }
        });
    }

    private void loadData() {
        try {
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nama");
            tableModel.addColumn("Alamat");
            tableModel.addColumn("Telepon");

            String sql = "SELECT * FROM tb_customer ORDER BY id_customer ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String tlp = "";
                try { tlp = rs.getString("telepon"); } 
                catch (Exception e) { tlp = rs.getString("no_telp"); }

                tableModel.addRow(new Object[]{
                    rs.getString("id_customer"),
                    rs.getString("nama_customer"),
                    rs.getString("alamat"),
                    tlp
                });
            }
            tableCustomer.setModel(tableModel);
        } catch (Exception e) {
            System.out.println("Tampil Customer: " + e.getMessage());
        }
    }

    private void cariData(String keyword) {
        try {
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nama");
            tableModel.addColumn("Alamat");
            tableModel.addColumn("Telepon");

            String sql = "SELECT * FROM tb_customer WHERE id_customer LIKE ? OR nama_customer LIKE ? OR alamat LIKE ? ORDER BY id_customer ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tlp = "";
                try { tlp = rs.getString("telepon"); } 
                catch (Exception e) { tlp = rs.getString("no_telp"); }

                tableModel.addRow(new Object[]{
                    rs.getString("id_customer"),
                    rs.getString("nama_customer"),
                    rs.getString("alamat"),
                    tlp
                });
            }
            tableCustomer.setModel(tableModel);
        } catch (Exception e) {
            System.out.println("Cari Customer: " + e.getMessage());
        }
    }

    private void generateIdCustomer() {
        try {
            Statement st = conn.createStatement();
            String sql = "SELECT MAX(id_customer) FROM tb_customer";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next() && rs.getString(1) != null) {
                String maxId = rs.getString(1);
                int num = Integer.parseInt(maxId) + 1;
                txtIdCustomer.setText(String.format("%03d", num));
            } else {
                txtIdCustomer.setText("001");
            }
        } catch (Exception e) {
            txtIdCustomer.setText("001");
        }
    }

    private void clearForm() {
        txtNama.setText("");
        txtTelepon.setText("");
        txtAlamat.setText("");
        txtCari.setText("");
        loadData();
        generateIdCustomer();
    }
}