package dao;

import config.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.barang;

public class barangdao {

    Connection conn = koneksi.getConnection();

    public boolean simpan(barang b) {
        try {
            String sql = "INSERT INTO tb_barang (id_barang, id_kategori, nama_barang, satuan, harga_jual, stok) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, b.getId_barang());
            ps.setInt(2, b.getId_kategori());
            ps.setString(3, b.getNama_barang());
            ps.setString(4, b.getSatuan());
            ps.setDouble(5, b.getHarga_jual());
            ps.setInt(6, b.getStok());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Simpan Barang : " + e.getMessage());
        }
        return false;
    }

    public boolean ubah(barang b) {
        try {
            String sql = "UPDATE tb_barang SET id_kategori=?, nama_barang=?, satuan=?, harga_jual=?, stok=? WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, b.getId_kategori());
            ps.setString(2, b.getNama_barang());
            ps.setString(3, b.getSatuan());
            ps.setDouble(4, b.getHarga_jual());
            ps.setInt(5, b.getStok());
            ps.setString(6, b.getId_barang());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Update Barang : " + e.getMessage());
        }
        return false;
    }

    public boolean hapus(String idBarang) {
        try {
            String sql = "DELETE FROM tb_barang WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idBarang);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Hapus Barang : " + e.getMessage());
        }
        return false;
    }

    public void tampilData(JTable table) {
        try {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Barang");
            model.addColumn("ID Kategori");
            model.addColumn("Nama Barang");
            model.addColumn("Satuan");
            model.addColumn("Harga");
            model.addColumn("Stok");

            String sql = "SELECT * FROM tb_barang ORDER BY id_barang ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getInt("id_kategori"),
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_jual"),
                    rs.getInt("stok")
                });
            }
            table.setModel(model);
        } catch (Exception e) {
            System.out.println("Tampil Barang : " + e.getMessage());
        }
    }

    public void cari(JTable table, String keyword) {
        try {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Barang");
            model.addColumn("ID Kategori");
            model.addColumn("Nama Barang");
            model.addColumn("Satuan");
            model.addColumn("Harga");
            model.addColumn("Stok");

            String sql = "SELECT * FROM tb_barang WHERE id_barang LIKE ? OR nama_barang LIKE ? ORDER BY id_barang ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getInt("id_kategori"),
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_jual"),
                    rs.getInt("stok")
                });
            }
            table.setModel(model);
        } catch (Exception e) {
            System.out.println("Cari Barang : " + e.getMessage());
        }
    }

    public ResultSet getBarangById(String idBarang) {
        try {
            String sql = "SELECT * FROM tb_barang WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idBarang);
            return ps.executeQuery();
        } catch (Exception e) {
            System.out.println("Get Barang : " + e.getMessage());
        }
        return null;
    }
}