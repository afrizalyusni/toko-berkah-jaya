package view;

import config.koneksi;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class loginform extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnExit;

    private Connection conn;

    public loginform() {

        conn = koneksi.getConnection();

        initComponents();

    }

    private void initComponents() {

        setTitle("Login");

        setSize(500, 400);

        setLocationRelativeTo(null);

        setResizable(false);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        JPanel panel =
                new JPanel();

        panel.setLayout(null);

        panel.setBackground(
                Color.WHITE
        );

        JLabel lblTitle =
                new JLabel(
                        "TOKO BERKAH JAYA"
                );

        lblTitle.setBounds(
                95,
                30,
                300,
                40
        );

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        JLabel lblUser =
                new JLabel(
                        "Username"
                );

        lblUser.setBounds(
                60,
                110,
                100,
                25
        );

        txtUsername =
                new JTextField();

        txtUsername.setBounds(
                60,
                135,
                350,
                40
        );

        JLabel lblPass =
                new JLabel(
                        "Password"
                );

        lblPass.setBounds(
                60,
                190,
                100,
                25
        );

        txtPassword =
                new JPasswordField();

        txtPassword.setBounds(
                60,
                215,
                350,
                40
        );

        btnLogin =
                new JButton(
                        "LOGIN"
                );

        btnLogin.setBounds(
                60,
                290,
                160,
                45
        );

        btnLogin.setBackground(
                new Color(
                        37,
                        99,
                        235
                )
        );

        btnLogin.setForeground(
                Color.WHITE
        );

        btnExit =
                new JButton(
                        "EXIT"
                );

        btnExit.setBounds(
                250,
                290,
                160,
                45
        );

        panel.add(lblTitle);

        panel.add(lblUser);

        panel.add(txtUsername);

        panel.add(lblPass);

        panel.add(txtPassword);

        panel.add(btnLogin);

        panel.add(btnExit);

        add(panel);

        btnLogin.addActionListener(
                e -> login()
        );

        btnExit.addActionListener(
                e -> System.exit(0)
        );

    }

    private void login() {

        try {

            String sql =
                    "SELECT * FROM tb_user "
                    + "WHERE username=? "
                    + "AND password=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(
                    1,
                    txtUsername.getText()
            );

            ps.setString(
                    2,
                    txtPassword.getText()
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login Berhasil"
                );

                new dashboard().setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Username atau Password Salah"
                );

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        }

    }

}