package view;

import controller.PenghuniController;
import controller.KamarController;
import model.Penghuni;
import model.Kamar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// VIEW - Panel Penghuni, bagian dari MVC
// Multithreading: SwingWorker digunakan agar operasi DB tidak membekukan GUI
public class PenghuniPanel extends JPanel {

    private final PenghuniController controller = new PenghuniController();
    private final KamarController kamarController = new KamarController();

    // Komponen tabel
    private JTable table;
    private DefaultTableModel tableModel;

    // Komponen form
    private JTextField txtNama, txtNoHp, txtAsal, txtTglMasuk, txtNamaOrtu, txtNoHpOrtu;
    private JComboBox<Kamar> cmbKamar;
    private JComboBox<String> cmbStatus;
    private JTextField txtCari;

    // Tombol
    private JButton btnTambah, btnUbah, btnHapus, btnBersih, btnCari;
    private JLabel lblStatus;

    // Simpan id penghuni yang sedang dipilih
    private int selectedId = -1;

    public PenghuniPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== PANEL FORM (KIRI) =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Penghuni"));
        formPanel.setPreferredSize(new Dimension(280, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nama", "No. HP", "Kamar", "Asal Daerah", "Tgl Masuk (YYYY-MM-DD)",
                           "Status", "Nama Ortu", "No. HP Ortu"};
        txtNama     = new JTextField(15);
        txtNoHp     = new JTextField(15);
        cmbKamar    = new JComboBox<>();
        txtAsal     = new JTextField(15);
        txtTglMasuk = new JTextField(15);
        cmbStatus   = new JComboBox<>(new String[]{"aktif", "keluar"});
        txtNamaOrtu = new JTextField(15);
        txtNoHpOrtu = new JTextField(15);

        Component[] fields = {txtNama, txtNoHp, cmbKamar, txtAsal, txtTglMasuk,
                              cmbStatus, txtNamaOrtu, txtNoHpOrtu};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            formPanel.add(new JLabel(labels[i] + ":"), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            formPanel.add(fields[i], gbc);
        }

        // Load data kamar ke combobox
        loadKamarCombo();

        // Tombol CRUD
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        btnTambah = new JButton("Tambah");
        btnUbah   = new JButton("Ubah");
        btnHapus  = new JButton("Hapus");
        btnBersih = new JButton("Bersih");
        btnPanel.add(btnTambah);
        btnPanel.add(btnUbah);
        btnPanel.add(btnHapus);
        btnPanel.add(btnBersih);

        gbc.gridx = 0; gbc.gridy = labels.length;
        gbc.gridwidth = 2; gbc.weightx = 1;
        gbc.insets = new Insets(10, 4, 4, 4);
        formPanel.add(btnPanel, gbc);

        // ===== PANEL TABEL (KANAN) =====
        String[] kolom = {"ID", "Nama", "No. HP", "Kamar", "Asal", "Tgl Masuk", "Status"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel cari
        JPanel cariPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cariPanel.add(new JLabel("Cari:"));
        txtCari = new JTextField(20);
        btnCari = new JButton("Cari");
        cariPanel.add(txtCari);
        cariPanel.add(btnCari);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(cariPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Status bar
        lblStatus = new JLabel(" Siap");
        lblStatus.setBorder(BorderFactory.createEtchedBorder());

        add(formPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);

        // ===== EVENT LISTENERS =====
        btnTambah.addActionListener(e -> aksiTambah());
        btnUbah.addActionListener(e -> aksiUbah());
        btnHapus.addActionListener(e -> aksiHapus());
        btnBersih.addActionListener(e -> bersihForm());
        btnCari.addActionListener(e -> aksiCari());

        // Klik baris tabel -> isi form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariTabel();
        });
    }

    // ===== MULTITHREADING: Load data pakai SwingWorker agar GUI tidak freeze =====
    private void loadData() {
        lblStatus.setText(" Memuat data...");
        btnTambah.setEnabled(false);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);

        new SwingWorker<List<Penghuni>, Void>() {
            @Override
            protected List<Penghuni> doInBackground() {
                // Berjalan di background thread - query DB di sini
                return controller.getAll();
            }

            @Override
            protected void done() {
                // Berjalan di EDT - update UI di sini
                try {
                    List<Penghuni> list = get();
                    tableModel.setRowCount(0);
                    for (Penghuni p : list) {
                        tableModel.addRow(new Object[]{
                            p.getIdPenghuni(), p.getNama(), p.getNoTelepon(),
                            p.getIdKamar(), p.getAsal(), p.getTglMasuk(), p.getStatusPenghuni()
                        });
                    }
                    lblStatus.setText(" Total: " + list.size() + " penghuni");
                } catch (Exception ex) {
                    lblStatus.setText(" Gagal memuat data: " + ex.getMessage());
                } finally {
                    btnTambah.setEnabled(true);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                }
            }
        }.execute();
    }

    private void loadKamarCombo() {
        new SwingWorker<List<Kamar>, Void>() {
            @Override
            protected List<Kamar> doInBackground() {
                return kamarController.getAll();
            }
            @Override
            protected void done() {
                try {
                    cmbKamar.removeAllItems();
                    for (Kamar k : get()) cmbKamar.addItem(k);
                } catch (Exception ex) {
                    lblStatus.setText(" Gagal load kamar: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void aksiTambah() {
        if (!validasiForm()) return;
        Penghuni p = buatObjekDariForm();
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() {
                controller.tambah(p);
                return null;
            }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Penghuni berhasil ditambahkan!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiUbah() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(null, "Pilih penghuni yang ingin diubah!"); return; }
        if (!validasiForm()) return;
        Penghuni p = buatObjekDariForm();
        p.setIdPenghuni(selectedId);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.ubah(p); return null; }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Data berhasil diubah!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiHapus() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(null, "Pilih penghuni yang ingin dihapus!"); return; }
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Yakin hapus penghuni ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi != JOptionPane.YES_OPTION) return;
        int idHapus = selectedId;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.hapus(idHapus); return null; }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Penghuni berhasil dihapus!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiCari() {
        String keyword = txtCari.getText().trim();
        if (keyword.isEmpty()) { loadData(); return; }
        new SwingWorker<List<Penghuni>, Void>() {
            @Override protected List<Penghuni> doInBackground() { return controller.cari(keyword); }
            @Override protected void done() {
                try {
                    List<Penghuni> list = get();
                    tableModel.setRowCount(0);
                    for (Penghuni p : list) {
                        tableModel.addRow(new Object[]{
                            p.getIdPenghuni(), p.getNama(), p.getNoTelepon(),
                            p.getIdKamar(), p.getAsal(), p.getTglMasuk(), p.getStatusPenghuni()
                        });
                    }
                    lblStatus.setText(" Ditemukan: " + list.size() + " penghuni");
                } catch (Exception ex) { lblStatus.setText(" Gagal: " + ex.getMessage()); }
            }
        }.execute();
    }

    private void isiFormDariTabel() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtNama.setText((String) tableModel.getValueAt(row, 1));
        txtNoHp.setText((String) tableModel.getValueAt(row, 2));
        txtAsal.setText((String) tableModel.getValueAt(row, 4));
        Object tgl = tableModel.getValueAt(row, 5);
        txtTglMasuk.setText(tgl != null ? tgl.toString() : "");
        cmbStatus.setSelectedItem(tableModel.getValueAt(row, 6));
        // Load detail dari DB untuk ortu
        new SwingWorker<Penghuni, Void>() {
            @Override protected Penghuni doInBackground() { return controller.getById(selectedId); }
            @Override protected void done() {
                try {
                    Penghuni p = get();
                    if (p != null) {
                        txtNamaOrtu.setText(p.getNamaOrtu());
                        txtNoHpOrtu.setText(p.getTelpOrtu());
                    }
                } catch (Exception ex) { /* abaikan */ }
            }
        }.execute();
    }

    private Penghuni buatObjekDariForm() {
        Kamar kamarDipilih = (Kamar) cmbKamar.getSelectedItem();
        int idKamar = kamarDipilih != null ? kamarDipilih.getIdKamar() : 0;
        return new Penghuni(
            0,
            txtNama.getText().trim(),
            txtNoHp.getText().trim(),
            idKamar,
            txtAsal.getText().trim(),
            txtTglMasuk.getText().trim(),
            (String) cmbStatus.getSelectedItem(),
            txtNamaOrtu.getText().trim(),
            txtNoHpOrtu.getText().trim()
        );
    }

    private boolean validasiForm() {
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama tidak boleh kosong!");
            return false;
        }
        if (txtNoHp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No. HP tidak boleh kosong!");
            return false;
        }
        return true;
    }

    private void bersihForm() {
        selectedId = -1;
        txtNama.setText("");
        txtNoHp.setText("");
        txtAsal.setText("");
        txtTglMasuk.setText("");
        txtNamaOrtu.setText("");
        txtNoHpOrtu.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }
}
