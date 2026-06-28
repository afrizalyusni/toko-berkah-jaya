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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import config.koneksi;
import dao.barangdao;
import dao.kategoridao;
import model.barang;

public class formbarang extends JFrame {

    private JTextField txtIdBarang;
    private JTextField txtNamaBarang;
    private JTextField txtHarga;
    private JTextField txtStok;
    private JTextField txtCari;

    private JComboBox<String> cbKategori;

    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnRefresh;

    private JTable tableBarang;

    private barangdao daoBarang;
    private kategoridao daoKategori;
    private Connection conn;

    public formbarang() {
        conn = koneksi.getConnection();
        daoBarang = new barangdao();
        daoKategori = new kategoridao();

        initComponents();
        loadKategori();
        insertDefaultData(); 
        loadData();
    }

    private void initComponents() {
        setTitle("Data Barang");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBounds(15, 20, 320, 600);
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel title = new JLabel("FORM BARANG");
        title.setBounds(80, 20, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelForm.add(title);

        JLabel lblId = new JLabel("ID Barang");
        lblId.setBounds(20, 80, 100, 25);
        panelForm.add(lblId);

        txtIdBarang = new JTextField();
        txtIdBarang.setBounds(20, 105, 250, 35);
        panelForm.add(txtIdBarang);

        JLabel lblKategori = new JLabel("Kategori");
        lblKategori.setBounds(20, 150, 100, 25);
        panelForm.add(lblKategori);

        cbKategori = new JComboBox<>();
        cbKategori.setBounds(20, 175, 250, 35);
        panelForm.add(cbKategori);

        JLabel lblNama = new JLabel("Nama Barang");
        lblNama.setBounds(20, 220, 120, 25);
        panelForm.add(lblNama);

        txtNamaBarang = new JTextField();
        txtNamaBarang.setBounds(20, 245, 250, 35);
        panelForm.add(txtNamaBarang);

        JLabel lblHarga = new JLabel("Harga");
        lblHarga.setBounds(20, 290, 120, 25);
        panelForm.add(lblHarga);

        txtHarga = new JTextField();
        txtHarga.setBounds(20, 315, 250, 35);
        panelForm.add(txtHarga);

        JLabel lblStok = new JLabel("Stok");
        lblStok.setBounds(20, 360, 120, 25);
        panelForm.add(lblStok);

        txtStok = new JTextField();
        txtStok.setBounds(20, 385, 250, 35);
        panelForm.add(txtStok);

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

        JPanel panelTable = new JPanel();
        panelTable.setLayout(null);
        panelTable.setBounds(350, 20, 570, 600);
        panelTable.setBackground(Color.WHITE);
        panelTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lblCari = new JLabel("Cari Barang");
        lblCari.setBounds(20, 20, 100, 25);
        panelTable.add(lblCari);

        txtCari = new JTextField();
        txtCari.setBounds(120, 20, 250, 35);
        panelTable.add(txtCari);

        tableBarang = new JTable();
        JScrollPane scroll = new JScrollPane(tableBarang);
        scroll.setBounds(20, 80, 530, 480);
        panelTable.add(scroll);

        add(panelTable);

        tableBarang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableBarang.getSelectedRow();
                if (row != -1) {
                    txtIdBarang.setText(tableBarang.getValueAt(row, 0).toString());
                    String idKategori = tableBarang.getValueAt(row, 1).toString();
                    if(idKategori.equals("1")) cbKategori.setSelectedItem("Makanan");
                    else if(idKategori.equals("2")) cbKategori.setSelectedItem("Minuman");
                    else if(idKategori.equals("3")) cbKategori.setSelectedItem("Elektronik");
                    txtNamaBarang.setText(tableBarang.getValueAt(row, 2).toString());
                    
                    String rawHarga = tableBarang.getValueAt(row, 4).toString().replaceAll("[^0-9]", "");
                    txtHarga.setText(rawHarga);
                    txtStok.setText(tableBarang.getValueAt(row, 5).toString());
                }
            }
        });

        btnSimpan.addActionListener(e -> {
            if (txtIdBarang.getText().trim().isEmpty() || txtNamaBarang.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID Barang dan Nama Barang tidak boleh kosong!");
                return;
            }
            try {
                barang b = new barang();
                b.setId_barang(txtIdBarang.getText());
                String katPilihan = cbKategori.getSelectedItem().toString();
                int idKat = katPilihan.equals("Minuman") ? 2 : (katPilihan.equals("Elektronik") ? 3 : 1);
                b.setId_kategori(idKat);
                b.setNama_barang(txtNamaBarang.getText());
                String satuan = idKat == 2 ? "BOTOL" : (idKat == 3 ? "UNIT" : "PCS");
                b.setSatuan(satuan);
                b.setHarga_jual(Double.parseDouble(txtHarga.getText()));
                b.setStok(Integer.parseInt(txtStok.getText()));

                if (daoBarang.simpan(b)) {
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage());
            }
        });

        btnUbah.addActionListener(e -> {
            if (txtIdBarang.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data pada tabel yang ingin diubah!");
                return;
            }
            try {
                barang b = new barang();
                b.setId_barang(txtIdBarang.getText());
                String katPilihan = cbKategori.getSelectedItem().toString();
                int idKat = katPilihan.equals("Minuman") ? 2 : (katPilihan.equals("Elektronik") ? 3 : 1);
                b.setId_kategori(idKat);
                b.setNama_barang(txtNamaBarang.getText());
                String satuan = idKat == 2 ? "BOTOL" : (idKat == 3 ? "UNIT" : "PCS");
                b.setSatuan(satuan);
                b.setHarga_jual(Double.parseDouble(txtHarga.getText()));
                b.setStok(Integer.parseInt(txtStok.getText()));

                if (daoBarang.ubah(b)) {
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Data Berhasil Diperbarui!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mengubah data: " + ex.getMessage());
            }
        });

        btnHapus.addActionListener(e -> {
            String id = txtIdBarang.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data pada tabel yang ingin dihapus!");
                return;
            }
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus barang " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                if (daoBarang.hapus(id)) {
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus!");
                }
            }
        });

        btnRefresh.addActionListener(e -> clearForm());

        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txtCari.getText().trim();
                if (!keyword.isEmpty()) {
                    daoBarang.cari(tableBarang, keyword);
                } else {
                    loadData();
                }
            }
        });
    }

    private void loadKategori() {
        cbKategori.removeAllItems();
        daoKategori.loadKategori(cbKategori);
        if (cbKategori.getItemCount() == 0) {
            cbKategori.addItem("Makanan");
            cbKategori.addItem("Minuman");
            cbKategori.addItem("Elektronik");
        }
    }

    private void loadData() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Barang");
            model.addColumn("ID Kategori");
            model.addColumn("Nama Barang");
            model.addColumn("Satuan");
            model.addColumn("Harga Jual");
            model.addColumn("Stok");
            model.addColumn("Total Nilai Stok");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tb_barang ORDER BY id_barang ASC");

            while (rs.next()) {
                double harga = rs.getDouble("harga_jual");
                int stok = rs.getInt("stok");
                double totalNilai = harga * stok;

                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getInt("id_kategori"),
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    formtransaksi.formatRupiah(harga),
                    stok,
                    formtransaksi.formatRupiah(totalNilai)
                });
            }
            tableBarang.setModel(model);
        } catch (Exception e) {
            System.out.println("Error Format JTable Barang: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtIdBarang.setText("");
        if (cbKategori.getItemCount() > 0) cbKategori.setSelectedIndex(0);
        txtNamaBarang.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        txtCari.setText("");
        loadData();
    }

    private void insertDefaultData() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT IGNORE INTO tb_kategori (id_kategori, nama_kategori) VALUES ('1', 'Makanan'), ('2', 'Minuman'), ('3', 'Elektronik')");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_barang");
            if (rs.next() && rs.getInt(1) <= 2) { 
                String sql = "INSERT IGNORE INTO tb_barang (id_barang, id_kategori, nama_barang, satuan, harga_jual, stok) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                Object[][] makanan = {
                    {"BR003", 1, "Indomie Goreng", "PCS", 3500, 100},
                    {"BR004", 1, "Mie Sedaap Soto", "PCS", 3400, 120},
                    {"BR005", 1, "Chitato Sapi Panggang", "PCS", 11500, 80},
                    {"BR006", 1, "Qtela Singkong Original", "PCS", 8500, 70},
                    {"BR007", 1, "Tango Wafer Coklat", "PACK", 9000, 95},
                    {"BR008", 1, "Roma Kelapa", "PACK", 10500, 110},
                    {"BR009", 1, "SilverQueen Milk Chocolate", "PCS", 16500, 60},
                    {"BR010", 1, "Oishi Prawn Crackers", "PCS", 7500, 150}
                };
                for (Object[] row : makanan) { insertRow(ps, row); }

                Object[][] minuman = {
                    {"BR011", 2, "Aqua 600ml", "BOTOL", 4000, 200},
                    {"BR012", 2, "Teh Botol Sosro", "BOTOL", 5000, 150},
                    {"BR013", 2, "Pocari Sweat 500ml", "BOTOL", 8500, 90},
                    {"BR014", 2, "Ultra Milk Coklat", "PCS", 6500, 100},
                    {"BR015", 2, "Coca-Cola 390ml", "BOTOL", 6000, 85}
                };
                for (Object[] row : minuman) { insertRow(ps, row); }

                Object[][] elektronik = {
                    {"BR016", 3, "Charger USB 20W", "UNIT", 125000, 30},
                    {"BR017", 3, "Headset Bluetooth", "UNIT", 245000, 20},
                    {"BR018", 3, "Power Bank 10000mAh", "UNIT", 195000, 25},
                    {"BR019", 3, "Kabel Data Type-C", "PCS", 35000, 60},
                    {"BR020", 3, "Lampu LED Emergency", "UNIT", 85000, 40}
                };
                for (Object[] row : elektronik) { insertRow(ps, row); }
                ps.close();
            }
            stmt.close();
        } catch (Exception e) { System.out.println("Inisialisasi: " + e.getMessage()); }
    }

    private void insertRow(PreparedStatement ps, Object[] row) throws java.sql.SQLException {
        ps.setString(1, (String) row[0]);
        ps.setInt(2, (Integer) row[1]);
        ps.setString(3, (String) row[2]);
        ps.setString(4, (String) row[3]);
        ps.setDouble(5, ((Number) row[4]).doubleValue());
        ps.setInt(6, (Integer) row[5]);
        ps.executeUpdate();
    }
}