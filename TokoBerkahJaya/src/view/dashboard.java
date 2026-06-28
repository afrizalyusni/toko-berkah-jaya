package view;

import config.koneksi;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class dashboard extends JFrame {

    private JLabel lblBarang;
    private JLabel lblCustomer;
    private JLabel lblUser;
    private JLabel lblPenjualan;

    // Tambahan panel container utama untuk menampung fitur secara dinamis
    private JPanel mainContentPanel;
    // Panel untuk menyimpan tampilan card statistik bawaan dashboard
    private JPanel homeDashboardPanel;

    private Connection conn;

    public dashboard() {
        conn = koneksi.getConnection();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Dashboard - Toko Berkah Jaya");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(new Color(248, 250, 252));

        // ==================== SIDEBAR MENU ====================
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setBounds(0, 0, 250, 700);

        JLabel title = new JLabel("TOKO BERKAH");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(35, 25, 200, 40);
        sidebar.add(title);

        // Tombol Dashboard / Home (Untuk kembali ke tampilan statistik awal)
        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.setBounds(25, 100, 180, 40);
        btnDashboard.addActionListener(e -> showHomeDashboard());
        sidebar.add(btnDashboard);

        JButton btnBarang = new JButton("Barang");
        btnBarang.setBounds(25, 150, 180, 40);
        btnBarang.addActionListener(e -> {
            // Memanggil isi komponen dari formbarang langsung ke halaman utama
            formbarang fb = new formbarang();
            showSubMenu(fb.getContentPane(), "Data Barang");
        });
        sidebar.add(btnBarang);

        JButton btnCustomer = new JButton("Customer");
        btnCustomer.setBounds(25, 200, 180, 40);
        btnCustomer.addActionListener(e -> {
            formcustomer fc = new formcustomer();
            showSubMenu(fc.getContentPane(), "Data Customer");
        });
        sidebar.add(btnCustomer);

        JButton btnKategori = new JButton("Kategori");
        btnKategori.setBounds(25, 250, 180, 40);
        btnKategori.addActionListener(e -> {
            formkategori fk = new formkategori();
            showSubMenu(fk.getContentPane(), "Data Kategori");
        });
        sidebar.add(btnKategori);

        JButton btnTransaksi = new JButton("Transaksi");
        btnTransaksi.setBounds(25, 300, 180, 40);
        btnTransaksi.addActionListener(e -> {
            formtransaksi ft = new formtransaksi();
            showSubMenu(ft.getContentPane(), "Data Transaksi");
        });
        sidebar.add(btnTransaksi);

        JButton btnLaporan = new JButton("Laporan");
        btnLaporan.setBounds(25, 350, 180, 40);
        btnLaporan.addActionListener(e -> {
            formlaporan fl = new formlaporan();
            showSubMenu(fl.getContentPane(), "Laporan");
        });
        sidebar.add(btnLaporan);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(25, 580, 180, 40);
        btnLogout.addActionListener(e -> logout());
        sidebar.add(btnLogout);

        add(sidebar);

        // ==================== MAIN CONTENT CONTAINER ====================
        // Wadah ini diletakkan di sebelah kanan sidebar (X: 250, Lebar: 950)
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(null);
        mainContentPanel.setBounds(250, 0, 950, 700);
        mainContentPanel.setBackground(new Color(248, 250, 252));
        add(mainContentPanel);

        // ==================== HOME DASHBOARD PANEL ====================
        // Membungkus card statistik bawaan agar bisa disembunyikan/ditampilkan dinamis
        homeDashboardPanel = new JPanel();
        homeDashboardPanel.setLayout(null);
        homeDashboardPanel.setBounds(0, 0, 950, 700);
        homeDashboardPanel.setBackground(new Color(248, 250, 252));
        mainContentPanel.add(homeDashboardPanel);

        // Membuat card di dalam homeDashboardPanel (koordinat X disesuaikan karena relatif terhadap panel ini)
        lblBarang = createCard("TOTAL BARANG", 70, 100);
        lblCustomer = createCard("TOTAL CUSTOMER", 400, 100);
        lblUser = createCard("TOTAL USER", 70, 320);
        lblPenjualan = createCard("TOTAL PENJUALAN", 400, 320);
    }

    private JLabel createCard(String title, int x, int y) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(x, y, 250, 150);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setBounds(20, 20, 200, 25);

        JLabel lblValue = new JLabel("0");
        lblValue.setBounds(20, 60, 200, 50);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 30));

        card.add(lblTitle);
        card.add(lblValue);

        // Dimasukkan ke dalam homeDashboardPanel, bukan frame utama langsung
        homeDashboardPanel.add(card);

        return lblValue;
    }

    // Fungsi transisi untuk membersihkan halaman kanan dan memasukkan menu baru
    private void showSubMenu(java.awt.Container content, String menuTitle) {
        mainContentPanel.removeAll(); // Bersihkan halaman kanan
        
        // Buat panel pembungkus baru untuk mengambil isi UI dari form file eksternal
        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setLayout(null);
        subMenuPanel.setBounds(0, 0, 950, 700);
        subMenuPanel.setBackground(new Color(245, 247, 250));

        // Pindahkan semua komponen dari objek Form ke subMenuPanel
        for (java.awt.Component comp : content.getComponents()) {
            subMenuPanel.add(comp);
        }

        mainContentPanel.add(subMenuPanel);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    // Fungsi untuk kembali ke halaman awal dashboard statistik
    private void showHomeDashboard() {
        mainContentPanel.removeAll();
        loadData(); // Memperbarui angka statistik real-time dari database
        mainContentPanel.add(homeDashboardPanel);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    public void loadData() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM tb_barang");
            if (rs1.next()) {
                lblBarang.setText(rs1.getString(1));
            }

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM tb_customer");
            if (rs2.next()) {
                lblCustomer.setText(rs2.getString(1));
            }

            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) FROM tb_user");
            if (rs3.next()) {
                lblUser.setText(rs3.getString(1));
            }

            ResultSet rs4 = st.executeQuery("SELECT COUNT(*) FROM tb_penjualan");
            if (rs4.next()) {
                lblPenjualan.setText(rs4.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void logout() {
        int pilih = JOptionPane.showConfirmDialog(this, "Logout ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilih == JOptionPane.YES_OPTION) {
            new loginform().setVisible(true);
            dispose();
        }
    }
}