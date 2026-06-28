package view;

import java.awt.Color;
import java.awt.Font;
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

public class formkategori extends JFrame {

    private JTextField txtIdKategori;
    private JTextField txtNamaKategori;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnRefresh;
    private JTable tableKategori;
    private Connection conn;

    public formkategori() {
        conn = koneksi.getConnection();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Data Kategori");
        setSize(950, 700);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        // ==================== PANEL FORM (KIRI) ====================
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBounds(15, 20, 320, 600);
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel title = new JLabel("FORM KATEGORI");
        title.setBounds(70, 20, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelForm.add(title);

        JLabel lblId = new JLabel("ID Kategori");
        lblId.setBounds(20, 90, 100, 25);
        panelForm.add(lblId);

        txtIdKategori = new JTextField();
        txtIdKategori.setBounds(20, 115, 250, 35);
        panelForm.add(txtIdKategori);

        JLabel lblNama = new JLabel("Nama Kategori");
        lblNama.setBounds(20, 170, 120, 25);
        panelForm.add(lblNama);

        txtNamaKategori = new JTextField();
        txtNamaKategori.setBounds(20, 195, 250, 35);
        panelForm.add(txtNamaKategori);

        btnSimpan = new JButton("SIMPAN");
        btnSimpan.setBounds(20, 300, 120, 40);
        panelForm.add(btnSimpan);

        btnUbah = new JButton("UBAH");
        btnUbah.setBounds(150, 300, 120, 40);
        panelForm.add(btnUbah);

        btnHapus = new JButton("HAPUS");
        btnHapus.setBounds(20, 360, 120, 40);
        panelForm.add(btnHapus);

        btnRefresh = new JButton("REFRESH");
        btnRefresh.setBounds(150, 360, 120, 40);
        panelForm.add(btnRefresh);

        add(panelForm);

        // ==================== PANEL TABEL (KANAN) ====================
        JPanel panelTable = new JPanel();
        panelTable.setLayout(null);
        panelTable.setBounds(350, 20, 570, 600);
        panelTable.setBackground(Color.WHITE);
        panelTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        tableKategori = new JTable();
        JScrollPane scroll = new JScrollPane(tableKategori);
        scroll.setBounds(20, 30, 530, 530);
        panelTable.add(scroll);

        add(panelTable);

        // ==================== EVENT LOGIC HANDLERS ====================

        // 1. Sinkronisasi Klik Baris Tabel ke Form
        tableKategori.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableKategori.getSelectedRow();
                if (row != -1) {
                    txtIdKategori.setText(tableKategori.getValueAt(row, 0).toString());
                    txtNamaKategori.setText(tableKategori.getValueAt(row, 1).toString());
                }
            }
        });

        // 2. Aksi Tombol Simpan (Create)
        btnSimpan.addActionListener(e -> {
            if (txtIdKategori.getText().trim().isEmpty() || txtNamaKategori.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID Kategori dan Nama Kategori wajib diisi!");
                return;
            }
            try {
                String sql = "INSERT INTO tb_kategori (id_kategori, nama_kategori) VALUES (?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, txtIdKategori.getText().trim());
                ps.setString(2, txtNamaKategori.getText().trim());
                ps.executeUpdate();
                
                clearForm();
                JOptionPane.showMessageDialog(this, "Kategori Berhasil Disimpan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan kategori: " + ex.getMessage());
            }
        });

        // 3. Aksi Tombol Ubah (Update)
        btnUbah.addActionListener(e -> {
            if (txtIdKategori.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih kategori pada tabel yang ingin diubah!");
                return;
            }
            try {
                String sql = "UPDATE tb_kategori SET nama_kategori = ? WHERE id_kategori = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, txtNamaKategori.getText().trim());
                ps.setString(2, txtIdKategori.getText().trim());
                ps.executeUpdate();
                
                clearForm();
                JOptionPane.showMessageDialog(this, "Kategori Berhasil Diperbarui!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui kategori: " + ex.getMessage());
            }
        });

        // 4. Aksi Tombol Hapus (Delete)
        btnHapus.addActionListener(e -> {
            String id = txtIdKategori.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih kategori pada tabel yang ingin dihapus!");
                return;
            }
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Hapus kategori " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM tb_kategori WHERE id_kategori = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, id);
                    ps.executeUpdate();
                    
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Kategori Berhasil Dihapus!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus kategori: " + ex.getMessage());
                }
            }
        });

        // 5. Aksi Tombol Refresh
        btnRefresh.addActionListener(e -> clearForm());
    }

    private void loadData() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Kategori");
            model.addColumn("Nama Kategori");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tb_kategori ORDER BY id_kategori ASC");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_kategori"),
                    rs.getString("nama_kategori")
                });
            }
            tableKategori.setModel(model);
        } catch (Exception e) {
            System.out.println("Error Kategori: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtIdKategori.setText("");
        txtNamaKategori.setText("");
        loadData();
    }
}