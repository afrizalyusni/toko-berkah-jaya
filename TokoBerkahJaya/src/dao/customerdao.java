package dao;

import config.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.customer;

public class customerdao {

    Connection conn = koneksi.getConnection();

    public boolean simpan(customer c) {

        try {

            String sql =
                    "INSERT INTO tb_customer "
                    + "(id_customer,nama_customer,alamat,telepon) "
                    + "VALUES(?,?,?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, c.getId_customer());
            ps.setString(2, c.getNama_customer());
            ps.setString(3, c.getAlamat());
            ps.setString(4, c.getTelepon());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return false;
    }

    public boolean ubah(customer c) {

        try {

            String sql =
                    "UPDATE tb_customer SET "
                    + "nama_customer=?,"
                    + "alamat=?,"
                    + "telepon=? "
                    + "WHERE id_customer=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, c.getNama_customer());
            ps.setString(2, c.getAlamat());
            ps.setString(3, c.getTelepon());
            ps.setString(4, c.getId_customer());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return false;
    }

    public boolean hapus(String idCustomer) {

        try {

            String sql =
                    "DELETE FROM tb_customer "
                    + "WHERE id_customer=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, idCustomer);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return false;
    }

    public void tampilData(JTable table) {

        try {

            DefaultTableModel model =
                    new DefaultTableModel();

            model.addColumn("ID");
            model.addColumn("Nama");
            model.addColumn("Alamat");
            model.addColumn("Telepon");

            String sql =
                    "SELECT * FROM tb_customer";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                model.addRow(
                        new Object[]{
                            rs.getString("id_customer"),
                            rs.getString("nama_customer"),
                            rs.getString("alamat"),
                            rs.getString("telepon")
                        }
                );

            }

            table.setModel(model);

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

}