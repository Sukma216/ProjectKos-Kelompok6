package view;

import javax.swing.*;
import java.awt.*;

// VIEW - bagian dari MVC, ini frame utama yang menampung semua panel
public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistem Manajemen Kos - Kelompok 6");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        tabbedPane = new JTabbedPane();

        // Tambahkan setiap panel sebagai tab
        tabbedPane.addTab("Penghuni", new PenghuniPanel());
        tabbedPane.addTab("Kamar", new KamarPanel());
        tabbedPane.addTab("Pembayaran", new PembayaranPanel());
        tabbedPane.addTab("Fasilitas", new FasilitasPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
