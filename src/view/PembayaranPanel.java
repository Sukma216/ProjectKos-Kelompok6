package view;

import controller.PembayaranController;
import controller.PenghuniController;
import model.Pembayaran;
import model.Penghuni;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// VIEW - Panel Pembayaran, bagian dari MVC
// Multithreading: SwingWorker digunakan agar operasi DB tidak membekukan GUI
public class PembayaranPanel extends JPanel {

    private final PembayaranController controller = new PembayaranController();
    private final PenghuniController penghuniController = new PenghuniController();

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Penghuni> cmbPenghuni;
    private JTextField txtBulan, txtTanggal, txtJumlah, txtMetode, txtKeterangan;
    private JComboBox<String> cmbStatusBayar;
    private JButton btnTambah, btnUbah, btnHapus, btnBersih;
    private JLabel lblStatus;
    private int selectedId = -1;

    public PembayaranPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Pembayaran"));
        formPanel.setPreferredSize(new Dimension(290, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbPenghuni     = new JComboBox<>();
        txtBulan        = new JTextField(15);
        txtTanggal      = new JTextField(15);
        txtJumlah       = new JTextField(15);
        txtMetode       = new JTextField(15);
        cmbStatusBayar  = new JComboBox<>(new String[]{"lunas", "tunggak"});
        txtKeterangan   = new JTextField(15);

        String[] labels = {"Penghuni", "Bulan Bayar", "Tanggal (YYYY-MM-DD)", "Jumlah (Rp)", "Metode", "Status", "Keterangan"};
        Component[] fields = {cmbPenghuni, txtBulan, txtTanggal, txtJumlah, txtMetode, cmbStatusBayar, txtKeterangan};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            formPanel.add(new JLabel(labels[i] + ":"), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            formPanel.add(fields[i], gbc);
        }

        loadPenghuniCombo();

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        btnTambah = new JButton("Tambah");
        btnUbah   = new JButton("Ubah");
        btnHapus  = new JButton("Hapus");
        btnBersih = new JButton("Bersih");
        btnPanel.add(btnTambah); btnPanel.add(btnUbah);
        btnPanel.add(btnHapus); btnPanel.add(btnBersih);

        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 4, 4, 4);
        formPanel.add(btnPanel, gbc);

        String[] kolom = {"ID", "Penghuni", "Kamar", "Bulan", "Tanggal", "Jumlah", "Metode", "Status"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        JScrollPane scrollPane = new JScrollPane(table);

        lblStatus = new JLabel(" Siap");
        lblStatus.setBorder(BorderFactory.createEtchedBorder());

        add(formPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> aksiTambah());
        btnUbah.addActionListener(e -> aksiUbah());
        btnHapus.addActionListener(e -> aksiHapus());
        btnBersih.addActionListener(e -> bersihForm());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariTabel();
        });
    }

    private void loadPenghuniCombo() {
        new SwingWorker<List<Penghuni>, Void>() {
            @Override protected List<Penghuni> doInBackground() { return penghuniController.getAll(); }
            @Override protected void done() {
                try { cmbPenghuni.removeAllItems(); for (Penghuni p : get()) cmbPenghuni.addItem(p); }
                catch (Exception ex) { lblStatus.setText(" Gagal load penghuni"); }
            }
        }.execute();
    }

    // MULTITHREADING - SwingWorker
    private void loadData() {
        lblStatus.setText(" Memuat data...");
        new SwingWorker<List<Pembayaran>, Void>() {
            @Override protected List<Pembayaran> doInBackground() { return controller.getAll(); }
            @Override protected void done() {
                try {
                    List<Pembayaran> list = get();
                    tableModel.setRowCount(0);
                    for (Pembayaran p : list)
                        tableModel.addRow(new Object[]{p.getIdPembayaran(), p.getNamaPenghuni(),
                            p.getIdKamar(), p.getBulan(), p.getTanggal(),
                            String.format("Rp %,.0f", p.getJumlah()), p.getMetodeBayar(), p.getStatus()});
                    lblStatus.setText(" Total: " + list.size() + " transaksi");
                } catch (Exception ex) { lblStatus.setText(" Gagal: " + ex.getMessage()); }
            }
        }.execute();
    }

    private void aksiTambah() {
        Pembayaran p = buatObjek();
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.tambah(p); return null; }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Pembayaran berhasil ditambahkan!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiUbah() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(null, "Pilih data yang ingin diubah!"); return; }
        Pembayaran p = buatObjek(); p.setIdPembayaran(selectedId);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.ubah(p); return null; }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Data berhasil diubah!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiHapus() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!"); return; }
        if (JOptionPane.showConfirmDialog(null, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        int idHapus = selectedId;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.hapus(idHapus); return null; }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Data berhasil dihapus!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void isiFormDariTabel() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtBulan.setText((String) tableModel.getValueAt(row, 3));
        Object tgl = tableModel.getValueAt(row, 4);
        txtTanggal.setText(tgl != null ? tgl.toString() : "");
        String jumlah = tableModel.getValueAt(row, 5).toString().replaceAll("[^0-9]", "");
        txtJumlah.setText(jumlah);
        Object metode = tableModel.getValueAt(row, 6);
        txtMetode.setText(metode != null ? metode.toString() : "");
        cmbStatusBayar.setSelectedItem(tableModel.getValueAt(row, 7));
    }

    private Pembayaran buatObjek() {
        Penghuni penghuni = (Penghuni) cmbPenghuni.getSelectedItem();
        int idPenghuni = penghuni != null ? penghuni.getIdPenghuni() : 0;
        String namaPenghuni = penghuni != null ? penghuni.getNama() : "";
        int idKamar = penghuni != null ? penghuni.getIdKamar() : 0;
        double jumlah = 0;
        try { jumlah = Double.parseDouble(txtJumlah.getText().trim().replaceAll("[^0-9]", "")); } catch (Exception ignored) {}
        String tanggal = txtTanggal.getText().trim().isEmpty() ? null : txtTanggal.getText().trim();
        String metode = txtMetode.getText().trim().isEmpty() ? null : txtMetode.getText().trim();
        return new Pembayaran(0, idPenghuni, namaPenghuni, idKamar,
            txtBulan.getText().trim(), tanggal, jumlah,
            (String) cmbStatusBayar.getSelectedItem(), metode, txtKeterangan.getText().trim());
    }

    private void bersihForm() {
        selectedId = -1;
        txtBulan.setText(""); txtTanggal.setText(""); txtJumlah.setText("");
        txtMetode.setText(""); txtKeterangan.setText("");
        cmbStatusBayar.setSelectedIndex(0); table.clearSelection();
    }
}
