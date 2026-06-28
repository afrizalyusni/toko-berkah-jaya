package view;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import config.koneksi;

public class formlaporan extends JFrame {

    private JTable tableLaporan;
    private DefaultTableModel modelLaporan;
    private JButton btnCetakLaporan;
    private JButton btnRefreshLaporan;
    private Connection conn;

    private static DefaultTableModel modelLaporanLokal;

    public formlaporan() {
        conn = koneksi.getConnection();
        initComponents();
        loadLaporanData();
    }

    public static void tambahBarisLaporanLokal(Object[] barisData) {
        if (modelLaporanLokal != null) {
            modelLaporanLokal.insertRow(0, barisData);
        }
    }

    private void initComponents() {
        setTitle("Laporan Transaksi Penjualan");
        setSize(950, 700);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(null);
        panelUtama.setBounds(15, 20, 905, 600);
        panelUtama.setBackground(Color.WHITE);
        panelUtama.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel title = new JLabel("LAPORAN PENJUALAN TOKO");
        title.setBounds(20, 20, 400, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelUtama.add(title);

        btnRefreshLaporan = new JButton("REFRESH DATA");
        btnRefreshLaporan.setBounds(620, 20, 130, 35);
        panelUtama.add(btnRefreshLaporan);

        btnCetakLaporan = new JButton("CETAK PDF/PRINT");
        btnCetakLaporan.setBounds(760, 20, 130, 35);
        btnCetakLaporan.setBackground(new Color(30, 41, 59));
        btnCetakLaporan.setForeground(Color.WHITE);
        panelUtama.add(btnCetakLaporan);

        tableLaporan = new JTable();
        JScrollPane scroll = new JScrollPane(tableLaporan);
        scroll.setBounds(20, 80, 865, 490);
        panelUtama.add(scroll);

        add(panelUtama);

        btnRefreshLaporan.addActionListener(e -> loadLaporanData());

        btnCetakLaporan.addActionListener(e -> {
            if (tableLaporan.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data transaksi yang dapat dicetak!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                boolean lengkap = tableLaporan.print(JTable.PrintMode.FIT_WIDTH, 
                        new java.text.MessageFormat("LAPORAN PENJUALAN - TOKO BERKAH JAYA"), 
                        new java.text.MessageFormat("Halaman {0}"));
                if (lengkap) {
                    JOptionPane.showMessageDialog(this, "Dokumen Laporan Berhasil Dicetak/Disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menjalankan fungsi cetak: " + ex.getMessage(), "Error Dokumen", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadLaporanData() {
        try {
            modelLaporanLokal = new DefaultTableModel();
            modelLaporanLokal.addColumn("No. Faktur");
            modelLaporanLokal.addColumn("Tanggal Transaksi");
            modelLaporanLokal.addColumn("ID Customer");
            modelLaporanLokal.addColumn("Total Transaksi");
            modelLaporanLokal.addColumn("User Kasir");

            modelLaporan = modelLaporanLokal;

            Statement st = conn.createStatement();
            String colFaktur = "id_transaksi";
            try {
                ResultSet testRs = st.executeQuery("SELECT id_transaksi FROM tb_penjualan LIMIT 1");
                testRs.close();
            } catch (Exception e) {
                colFaktur = "id_penjualan";
            }

            ResultSet rs = st.executeQuery("SELECT * FROM tb_penjualan ORDER BY " + colFaktur + " DESC");
            while (rs.next()) {
                modelLaporanLokal.addRow(new Object[]{
                    rs.getString(colFaktur),
                    rs.getTimestamp("tgl_penjualan").toString(),
                    "Umum / Cash",
                    formtransaksi.formatRupiah(rs.getDouble("total_bayar")),
                    "Admin"
                });
            }
            tableLaporan.setModel(modelLaporan);
        } catch (Exception e) {
            System.out.println("Error Load Laporan: " + e.getMessage());
            tableLaporan.setModel(modelLaporanLokal);
        }
    }
}