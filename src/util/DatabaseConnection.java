/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Gunakan static agar koneksi bisa dipanggil tanpa bikin objek baru terus-menerus
    private static Connection conn;
    
    private static final String jdbc_driver = "com.mysql.cj.jdbc.Driver";
    private static final String nama_db = "projectkos";
    private static final String url_db = "jdbc:mysql://localhost/" + nama_db;
    private static final String username_db = "root";
    private static final String password_db = "";

    // Method untuk mendapatkan koneksi (akan dipanggil di CRUD)
    public static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName(jdbc_driver);
                conn = DriverManager.getConnection(url_db, username_db, password_db);
                System.out.println("Connection Success");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed: " + e.getMessage());
            }
        }
        return conn;
    }
}