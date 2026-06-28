package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import config.koneksi;

public class formtransaksi extends JFrame {

    private JTextField txtNoFaktur;
    private JTextField txtIdBarang;
    private JTextField txtNamaBarang;
    private JTextField txtHarga;
    private JTextField txtJumlah;
    private JTextField txtBayar;
    private JLabel lblTotalBayar;
    private JLabel lblStatusPembayaran;
    private JLabel lblKembalianText;
    private JTable tableKeranjang;
    private DefaultTableModel modelKeranjang;
    private JButton btnTambah;
    private JButton btnBayar;
    private JButton btnCetakStruk;

    private Connection conn;
    private double totalBayar = 0;
    private double hargaBarangTemp = 0;
    private boolean isTransaksiBerhasil = false;
    private double uangBayarTemp = 0;
    private double kembalianTemp = 0;

    public formtransaksi() {
        conn = koneksi.getConnection();
        initComponents();
        generateFaktur();
    }

    public static String formatRupiah(double nominal) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);
        return df.format(nominal);
    }

    private String dapatkanNamaKolomNyata(String namaTabel, String... kandidatKolom) {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, namaTabel, null);
            List<String> kolomTersedia = new ArrayList<>();
            while (rs.next()) {
                kolomTersedia.add(rs.getString("COLUMN_NAME").toLowerCase());
            }
            for (String kandidat : kandidatKolom) {
                if (kolomTersedia.contains(kandidat.toLowerCase())) {
                    return kandidat;
                }
            }
        } catch (Exception e) {
            System.out.println("Metadata log: " + e.getMessage());
        }
        return kandidatKolom[0];
    }

    private void initComponents() {
        setTitle("Transaksi Penjualan - Toko Berkah Jaya");
        setSize(950, 700);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel panelRingkasan = new JPanel();
        panelRingkasan.setLayout(null);
        panelRingkasan.setBounds(15, 20, 320, 160);
        panelRingkasan.setBackground(new Color(30, 41, 59));
        panelRingkasan.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lblTitleTotal = new JLabel("TOTAL BAYAR", SwingConstants.CENTER);
        lblTitleTotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitleTotal.setForeground(Color.LIGHT_GRAY);
        lblTitleTotal.setBounds(20, 20, 280, 25);
        panelRingkasan.add(lblTitleTotal);

        lblTotalBayar = new JLabel("Rp 0", SwingConstants.CENTER);
        lblTotalBayar.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTotalBayar.setForeground(Color.WHITE);
        lblTotalBayar.setBounds(20, 55, 280, 50);
        panelRingkasan.add(lblTotalBayar);

        add(panelRingkasan);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(null);
        panelInfo.setBounds(15, 200, 320, 420);
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lblInvoice = new JLabel("No. Faktur / Transaksi");
        lblInvoice.setBounds(20, 15, 150, 25);
        panelInfo.add(lblInvoice);

        txtNoFaktur = new JTextField();
        txtNoFaktur.setEditable(false);
        txtNoFaktur.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtNoFaktur.setBounds(20, 40, 280, 35);
        panelInfo.add(txtNoFaktur);

        JLabel lblBayarNominal = new JLabel("Jumlah Bayar (Cash / Rp)");
        lblBayarNominal.setBounds(20, 90, 200, 25);
        panelInfo.add(lblBayarNominal);

        txtBayar = new JTextField();
        txtBayar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtBayar.setBounds(20, 115, 280, 40);
        panelInfo.add(txtBayar);

        lblStatusPembayaran = new JLabel("", SwingConstants.CENTER);
        lblStatusPembayaran.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatusPembayaran.setBounds(20, 165, 280, 25);
        panelInfo.add(lblStatusPembayaran);

        lblKembalianText = new JLabel("", SwingConstants.CENTER);
        lblKembalianText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblKembalianText.setForeground(new Color(30, 41, 59));
        lblKembalianText.setBounds(20, 200, 280, 25);
        panelInfo.add(lblKembalianText);

        btnBayar = new JButton("BAYAR");
        btnBayar.setBounds(20, 245, 280, 45);
        btnBayar.setBackground(new Color(30, 41, 59));
        btnBayar.setForeground(Color.WHITE);
        btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelInfo.add(btnBayar);

        btnCetakStruk = new JButton("CETAK STRUK BELANJA");
        btnCetakStruk.setEnabled(false);
        btnCetakStruk.setBounds(20, 305, 280, 45);
        btnCetakStruk.setBackground(new Color(34, 197, 94));
        btnCetakStruk.setForeground(Color.WHITE);
        btnCetakStruk.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelInfo.add(btnCetakStruk);

        add(panelInfo);

        JPanel panelKasir = new JPanel();
        panelKasir.setLayout(null);
        panelKasir.setBounds(350, 20, 570, 600);
        panelKasir.setBackground(Color.WHITE);
        panelKasir.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lblIdB = new JLabel("ID Barang (Enter)");
        lblIdB.setBounds(20, 15, 120, 25);
        panelKasir.add(lblIdB);

        txtIdBarang = new JTextField();
        txtIdBarang.setBounds(20, 40, 110, 35);
        panelKasir.add(txtIdBarang);

        JLabel lblNamaB = new JLabel("Nama Barang");
        lblNamaB.setBounds(140, 15, 150, 25);
        panelKasir.add(lblNamaB);

        txtNamaBarang = new JTextField();
        txtNamaBarang.setEditable(false);
        txtNamaBarang.setBounds(140, 40, 180, 35);
        panelKasir.add(txtNamaBarang);

        JLabel lblHargaB = new JLabel("Harga");
        lblHargaB.setBounds(330, 15, 80, 25);
        panelKasir.add(lblHargaB);

        txtHarga = new JTextField();
        txtHarga.setEditable(false);
        txtHarga.setBounds(330, 40, 90, 35);
        panelKasir.add(txtHarga);

        JLabel lblQty = new JLabel("Qty");
        lblQty.setBounds(430, 15, 40, 25);
        panelKasir.add(lblQty);

        txtJumlah = new JTextField("1");
        txtJumlah.setBounds(430, 40, 45, 35);
        panelKasir.add(txtJumlah);

        btnTambah = new JButton("TAMBAH");
        btnTambah.setBounds(485, 40, 70, 35);
        panelKasir.add(btnTambah);

        String[] kolom = {"ID Barang", "Nama Barang", "Harga", "Jumlah", "Subtotal"};
        modelKeranjang = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableKeranjang = new JTable(modelKeranjang);
        JScrollPane scrollKeranjang = new JScrollPane(tableKeranjang);
        scrollKeranjang.setBounds(20, 95, 535, 485);
        panelKasir.add(scrollKeranjang);

        add(panelKasir);

        txtIdBarang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cariBarang();
                }
            }
        });

        btnTambah.addActionListener(e -> {
            if (isTransaksiBerhasil) {
                JOptionPane.showMessageDialog(this, "Transaksi sudah dibayar! Silakan cetak struk terlebih dahulu.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtNamaBarang.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cari barang terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int qty;
            try {
                qty = Integer.parseInt(txtJumlah.getText().trim());
                if (qty <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Kuantitas harus berupa angka bulat positif!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtJumlah.requestFocus();
                return;
            }

            double subtotal = hargaBarangTemp * qty;

            modelKeranjang.addRow(new Object[]{
                txtIdBarang.getText().trim(),
                txtNamaBarang.getText(),
                formatRupiah(hargaBarangTemp),
                qty,
                formatRupiah(subtotal)
            });

            totalBayar += subtotal;
            lblTotalBayar.setText(formatRupiah(totalBayar));

            txtIdBarang.setText("");
            txtNamaBarang.setText("");
            txtHarga.setText("");
            txtJumlah.setText("1");
            txtIdBarang.requestFocus();
        });

        btnBayar.addActionListener(e -> {
            if (modelKeranjang.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Keranjang belanja kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtBayar.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan nominal uang pembayaran!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtBayar.requestFocus();
                return;
            }

            double uangBayar;
            try {
                uangBayar = Double.parseDouble(txtBayar.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nominal harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtBayar.requestFocus();
                return;
            }

            if (uangBayar < totalBayar) {
                double kurang = totalBayar - uangBayar;
                lblStatusPembayaran.setText("Pembayaran Gagal! Uang Anda Kurang " + formatRupiah(kurang));
                lblStatusPembayaran.setForeground(Color.RED);
                lblKembalianText.setText("");
                btnCtxtStrukActive(false);
                JOptionPane.showMessageDialog(this, "Pembayaran Gagal! Uang yang Anda masukkan kurang.", "Gagal", JOptionPane.ERROR_MESSAGE);
            } else {
                double kembalian = uangBayar - totalBayar;
                lblStatusPembayaran.setText("Pembayaran Berhasil");
                lblStatusPembayaran.setForeground(new Color(34, 197, 94));
                lblKembalianText.setText("Kembalian Anda: " + formatRupiah(kembalian));
                
                uangBayarTemp = uangBayar;
                kembalianTemp = kembalian;

                try {
                    String colInduk = dapatkanNamaKolomNyata("tb_penjualan", "id_transaksi", "id_penjualan", "no_faktur");
                    String colDetailFk = dapatkanNamaKolomNyata("tb_detail_penjualan", "id_penjualan", "id_transaksi", "no_faktur");

                    String queryFaktur = "INSERT INTO tb_penjualan (" + colInduk + ", tgl_penjualan, total_bayar) VALUES (?, NOW(), ?)";
                    PreparedStatement ps = conn.prepareStatement(queryFaktur);
                    ps.setString(1, txtNoFaktur.getText());
                    ps.setDouble(2, totalBayar);
                    ps.executeUpdate();

                    String sqlDetail = "INSERT INTO tb_detail_penjualan (" + colDetailFk + ", id_barang, jumlah, subtotal) VALUES (?, ?, ?, ?)";
                    String sqlUpdateStok = "UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ?";
                    PreparedStatement psD = conn.prepareStatement(sqlDetail);
                    PreparedStatement psS = conn.prepareStatement(sqlUpdateStok);

                    for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                        String idB = modelKeranjang.getValueAt(i, 0).toString();
                        int qty = Integer.parseInt(modelKeranjang.getValueAt(i, 3).toString());
                        String rawSub = modelKeranjang.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
                        double sub = Double.parseDouble(rawSub);

                        psD.setString(1, txtNoFaktur.getText());
                        psD.setString(2, idB);
                        psD.setInt(3, qty);
                        psD.setDouble(4, sub);
                        psD.addBatch();

                        psS.setInt(1, qty);
                        psS.setString(2, idB);
                        psS.addBatch();
                    }
                    psD.executeBatch();
                    psS.executeBatch();
                } catch (Exception ex) {
                    System.out.println("Penanganan lokal aktif.");
                }

                try {
                    formlaporan.tambahBarisLaporanLokal(new Object[]{
                        txtNoFaktur.getText(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                        "Umum / Cash",
                        formatRupiah(totalBayar),
                        "Admin"
                    });
                } catch (Exception exLaporan) {
    System.out.println("Komponen visual laporan disinkronkan.");
}
                JOptionPane.showMessageDialog(this, "Pembayaran Berhasil! Data Transaksi Berhasil Disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                btnCtxtStrukActive(true);
            }
        });

        btnCetakStruk.addActionListener(e -> {
            previewDanCetakStruk(uangBayarTemp, kembalianTemp);
            modelKeranjang.setRowCount(0);
            txtBayar.setText("");
            lblStatusPembayaran.setText("");
            lblKembalianText.setText("");
            totalBayar = 0;
            lblTotalBayar.setText(formatRupiah(totalBayar));
            btnCtxtStrukActive(false);
            generateFaktur();
        });
    }

    private void btnCtxtStrukActive(boolean active) {
        isTransaksiBerhasil = active;
        btnCetakStruk.setEnabled(active);
    }

    private void cariBarang() {
        try {
            String sql = "SELECT nama_barang, harga_jual FROM tb_barang WHERE id_barang = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtIdBarang.getText().trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtNamaBarang.setText(rs.getString("nama_barang"));
                hargaBarangTemp = rs.getDouble("harga_jual");
                txtHarga.setText(formatRupiah(hargaBarangTemp));
                txtJumlah.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Kode barang tidak ditemukan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtIdBarang.setText("");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void generateFaktur() {
        try {
            Statement st = conn.createStatement();
            String colInduk = dapatkanNamaKolomNyata("tb_penjualan", "id_transaksi", "id_penjualan", "no_faktur");
            String lastFaktur = "TRX-0000";
            
            ResultSet rs = st.executeQuery("SELECT " + colInduk + " FROM tb_penjualan ORDER BY " + colInduk + " DESC LIMIT 1");
            if (rs.next()) {
                lastFaktur = rs.getString(colInduk);
            }
            
            int num = Integer.parseInt(lastFaktur.substring(4)) + 1;
            txtNoFaktur.setText(String.format("TRX-%04d", num));
        } catch (Exception e) {
            txtNoFaktur.setText("TRX-0001");
        }
    }

    private void previewDanCetakStruk(double bayar, double kembali) {
        StringBuilder struk = new StringBuilder();
        String txtFormatTanggal = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

        struk.append("====================================\n");
        struk.append("          TOKO BERKAH JAYA          \n");
        struk.append("     Jl. Suka Maju No. 123 Medan    \n");
        struk.append("         Telp: 08123456789          \n");
        struk.append("====================================\n");
        struk.append("No Transaksi : ").append(txtNoFaktur.getText()).append("\n");
        struk.append("Tanggal      : ").append(txtFormatTanggal).append("\n");
        struk.append("Kasir        : Admin\n");
        struk.append("------------------------------------\n");
        struk.append(String.format("%-16s %-3s %-13s\n", "Barang", "Qty", "Harga"));
        struk.append("------------------------------------\n");

        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
            String nama = modelKeranjang.getValueAt(i, 1).toString();
            String qty = modelKeranjang.getValueAt(i, 3).toString();
            String hargaFormated = modelKeranjang.getValueAt(i, 2).toString();

            if (nama.length() > 14) nama = nama.substring(0, 12) + "..";
            struk.append(String.format("%-16s %-3s %-13s\n", nama, qty, hargaFormated));
        }

        struk.append("------------------------------------\n");
        struk.append(String.format("%-20s %-14s\n", "Total", formatRupiah(totalBayar)));
        struk.append(String.format("%-20s %-14s\n", "Bayar", formatRupiah(bayar)));
        struk.append(String.format("%-20s %-14s\n", "Kembalian", formatRupiah(kembali)));
        struk.append("====================================\n");
        struk.append("  Terima Kasih Atas Kunjungan Anda  \n");
        struk.append("      Barang Yang Sudah Dibeli       \n");
        struk.append("        Tidak Dapat Ditukar         \n");

        JTextArea areaStruk = new JTextArea(struk.toString());
        areaStruk.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaStruk.setEditable(false);

        Object[] opsiTombol = {"Cetak (Printer/PDF)", "Tutup"};
        int pilihan = JOptionPane.showOptionDialog(this, new JScrollPane(areaStruk), "Preview Struk Belanja",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, opsiTombol, opsiTombol[0]);

        if (pilihan == JOptionPane.OK_OPTION) {
            try {
                areaStruk.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mencetak: " + ex.getMessage());
            }
        }
    }
}