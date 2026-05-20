package view;

import controller.KamarController;
import model.Kamar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// VIEW - Panel Kamar, bagian dari MVC
// Multithreading: SwingWorker digunakan agar operasi DB tidak membekukan GUI
public class KamarPanel extends JPanel {

    private final KamarController controller = new KamarController();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNamaKamar, txtHarga;
    private JComboBox<String> cmbStatus;
    private JButton btnTambah, btnUbah, btnHapus, btnBersih;
    private JLabel lblStatus;
    private int selectedId = -1;

    public KamarPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kamar"));
        formPanel.setPreferredSize(new Dimension(250, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNamaKamar = new JTextField(15);
        txtHarga     = new JTextField(15);
        cmbStatus    = new JComboBox<>(new String[]{"kosong", "terisi"});

        String[] labels = {"Nama Kamar", "Harga (Rp)", "Status"};
        Component[] fields = {txtNamaKamar, txtHarga, cmbStatus};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            formPanel.add(new JLabel(labels[i] + ":"), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            formPanel.add(fields[i], gbc);
        }

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

        // Tabel
        String[] kolom = {"ID", "Nama Kamar", "Harga", "Status"};
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

    // MULTITHREADING - SwingWorker
    private void loadData() {
        lblStatus.setText(" Memuat data...");
        new SwingWorker<List<Kamar>, Void>() {
            @Override protected List<Kamar> doInBackground() { return controller.getAll(); }
            @Override protected void done() {
                try {
                    List<Kamar> list = get();
                    tableModel.setRowCount(0);
                    for (Kamar k : list)
                        tableModel.addRow(new Object[]{k.getIdKamar(), k.getNamaKamar(),
                            String.format("Rp %,.0f", k.getHarga()), k.getStatus()});
                    lblStatus.setText(" Total: " + list.size() + " kamar");
                } catch (Exception ex) { lblStatus.setText(" Gagal: " + ex.getMessage()); }
            }
        }.execute();
    }

    private void aksiTambah() {
        if (txtNamaKamar.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(null, "Nama kamar tidak boleh kosong!"); return; }
        Kamar k = buatObjek();
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.tambah(k); return null; }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Kamar berhasil ditambahkan!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiUbah() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(null, "Pilih kamar yang ingin diubah!"); return; }
        Kamar k = buatObjek(); k.setIdKamar(selectedId);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.ubah(k); return null; }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Kamar berhasil diubah!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiHapus() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(null, "Pilih kamar yang ingin dihapus!"); return; }
        if (JOptionPane.showConfirmDialog(null, "Yakin hapus kamar ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        int idHapus = selectedId;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.hapus(idHapus); return null; }
            @Override protected void done() {
                try { get(); bersihForm(); loadData(); JOptionPane.showMessageDialog(null, "Kamar berhasil dihapus!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void isiFormDariTabel() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtNamaKamar.setText((String) tableModel.getValueAt(row, 1));
        String harga = tableModel.getValueAt(row, 2).toString().replaceAll("[^0-9]", "");
        txtHarga.setText(harga);
        cmbStatus.setSelectedItem(tableModel.getValueAt(row, 3));
    }

    private Kamar buatObjek() {
        double harga = 0;
        try { harga = Double.parseDouble(txtHarga.getText().trim().replaceAll("[^0-9]", "")); } catch (Exception ignored) {}
        return new Kamar(0, txtNamaKamar.getText().trim(), harga, (String) cmbStatus.getSelectedItem());
    }

    private void bersihForm() {
        selectedId = -1;
        txtNamaKamar.setText(""); txtHarga.setText("");
        cmbStatus.setSelectedIndex(0); table.clearSelection();
    }
}
