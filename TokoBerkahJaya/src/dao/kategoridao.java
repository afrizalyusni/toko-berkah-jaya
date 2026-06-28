package dao;

import config.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import model.kategori;

public class kategoridao {

    Connection conn =
            koneksi.getConnection();

    public boolean simpan(
            kategori k
    ) {

        try {

            String sql =
                    "INSERT INTO tb_kategori "
                    + "(nama_kategori) "
                    + "VALUES(?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(
                    1,
                    k.getNama_kategori()
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    e.getMessage()
            );

        }

        return false;
    }

    public void loadKategori(
            JComboBox<String> combo
    ) {

        try {

            combo.removeAllItems();

            String sql =
                    "SELECT * "
                    + "FROM tb_kategori";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                combo.addItem(
                        rs.getString(
                                "nama_kategori"
                        )
                );

            }

        } catch (Exception e) {

            System.out.println(
                    e.getMessage()
            );

        }

    }

    public int getIdKategori(
            String namaKategori
    ) {

        try {

            String sql =
                    "SELECT id_kategori "
                    + "FROM tb_kategori "
                    + "WHERE nama_kategori=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(
                    1,
                    namaKategori
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getInt(
                        "id_kategori"
                );

            }

        } catch (Exception e) {

            System.out.println(
                    e.getMessage()
            );

        }

        return 0;
    }

}