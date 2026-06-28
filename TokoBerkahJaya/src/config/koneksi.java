package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {

    private static Connection conn;

    public static Connection getConnection() {

        try {

            if (conn == null || conn.isClosed()) {

                String url =
                        "jdbc:mysql://localhost:3306/db_toko_berkah";

                String user = "root";

                String password = "";

                Class.forName(
                        "com.mysql.cj.jdbc.Driver"
                );

                conn =
                        DriverManager.getConnection(
                                url,
                                user,
                                password
                        );

                System.out.println(
                        "Database Terkoneksi"
                );

            }

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println(
                    "Gagal Koneksi : "
                    + e.getMessage()
            );

        }

        return conn;
    }

}