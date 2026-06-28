package dao;

import config.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class userdao {

    Connection conn =
            koneksi.getConnection();

    public boolean login(
            String username,
            String password
    ) {

        try {

            String sql =
                    "SELECT * FROM tb_user "
                    + "WHERE username=? "
                    + "AND password=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, username);

            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            System.out.println(
                    e.getMessage()
            );

        }

        return false;

    }

}