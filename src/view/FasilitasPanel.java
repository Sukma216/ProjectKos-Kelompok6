package view;

import controller.FasilitasController;
import model.Fasilitas;
import model.KondisiFasilitas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// VIEW - Panel Fasilitas, bagian dari MVC
// Multithreading: SwingWorker digunakan agar operasi DB tidak membekukan GUI
public class FasilitasPanel extends JPanel {

    private final FasilitasController controller = new FasilitasController();

    // Tab fasilitas
    private JTable tblFasilitas;
    private DefaultTableModel modelFasilitas;
    private JTextField txtNamaFasilitas;
    private JButton btnTambahF, btnUbahF, btnHapusF, btnBersihF;
    private int selectedFasilitasId = -1;

    // Tab kondisi
    private JTable tblKondisi;
    private DefaultTableModel modelKondisi;
    private JLabel lblStatus;

    public FasilitasPanel() {
        initComponents();
        loadFasilitas();
        loadKondisi();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Data Fasilitas", buatPanelFasilitas());
        tabs.addTab("Kondisi Fasilitas", buatPanelKondisi());

        lblStatus = new JLabel(" Siap");
        lblStatus.setBorder(BorderFactory.createEtchedBorder());

        add(tabs, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
    }

    private JPanel buatPanelFasilitas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Fasilitas"));
        formPanel.setPreferredSize(new Dimension(220, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNamaFasilitas = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Nama Fasilitas:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(txtNamaFasilitas, gbc);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        btnTambahF = new JButton("Tambah");
        btnUbahF   = new JButton("Ubah");
        btnHapusF  = new JButton("Hapus");
        btnBersihF = new JButton("Bersih");
        btnPanel.add(btnTambahF); btnPanel.add(btnUbahF);
        btnPanel.add(btnHapusF); btnPanel.add(btnBersihF);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 4, 4, 4);
        formPanel.add(btnPanel, gbc);

        modelFasilitas = new DefaultTableModel(new String[]{"ID", "Nama Fasilitas"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblFasilitas = new JTable(modelFasilitas);
        tblFasilitas.getColumnModel().getColumn(0).setMaxWidth(40);

        panel.add(formPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(tblFasilitas), BorderLayout.CENTER);

        btnTambahF.addActionListener(e -> aksiTambahFasilitas());
        btnUbahF.addActionListener(e -> aksiUbahFasilitas());
        btnHapusF.addActionListener(e -> aksiHapusFasilitas());
        btnBersihF.addActionListener(e -> { selectedFasilitasId = -1; txtNamaFasilitas.setText(""); tblFasilitas.clearSelection(); });
        tblFasilitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblFasilitas.getSelectedRow();
                if (row != -1) {
                    selectedFasilitasId = (int) modelFasilitas.getValueAt(row, 0);
                    txtNamaFasilitas.setText((String) modelFasilitas.getValueAt(row, 1));
                }
            }
        });

        return panel;
    }

    private JPanel buatPanelKondisi() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Kondisi Fasilitas per Kamar"));

        String[] kolom = {"Kamar", "Fasilitas", "Kondisi", "Keterangan", "Terakhir Diperbarui"};
        modelKondisi = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblKondisi = new JTable(modelKondisi);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadKondisi());

        panel.add(new JScrollPane(tblKondisi), BorderLayout.CENTER);
        panel.add(btnRefresh, BorderLayout.SOUTH);
        return panel;
    }

    // MULTITHREADING - SwingWorker
    private void loadFasilitas() {
        lblStatus.setText(" Memuat fasilitas...");
        new SwingWorker<List<Fasilitas>, Void>() {
            @Override protected List<Fasilitas> doInBackground() { return controller.getAll(); }
            @Override protected void done() {
                try {
                    List<Fasilitas> list = get();
                    modelFasilitas.setRowCount(0);
                    for (Fasilitas f : list) modelFasilitas.addRow(new Object[]{f.getIdFasilitas(), f.getNamaFasilitas()});
                    lblStatus.setText(" Fasilitas: " + list.size());
                } catch (Exception ex) { lblStatus.setText(" Gagal: " + ex.getMessage()); }
            }
        }.execute();
    }

    private void loadKondisi() {
        new SwingWorker<List<KondisiFasilitas>, Void>() {
            @Override protected List<KondisiFasilitas> doInBackground() { return controller.getAllKondisi(); }
            @Override protected void done() {
                try {
                    List<KondisiFasilitas> list = get();
                    modelKondisi.setRowCount(0);
                    for (KondisiFasilitas kf : list)
                        modelKondisi.addRow(new Object[]{"Kamar " + kf.getIdKamar(), "Fasilitas " + kf.getIdFasilitas(),
                            kf.getKondisi(), kf.getKeteranganRusak(), kf.getTerakhirDiperbarui()});
                } catch (Exception ex) { lblStatus.setText(" Gagal load kondisi: " + ex.getMessage()); }
            }
        }.execute();
    }

    private void aksiTambahFasilitas() {
        String nama = txtNamaFasilitas.getText().trim();
        if (nama.isEmpty()) { JOptionPane.showMessageDialog(null, "Nama fasilitas tidak boleh kosong!"); return; }
        Fasilitas f = new Fasilitas(0, nama);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.tambah(f); return null; }
            @Override protected void done() {
                try { get(); txtNamaFasilitas.setText(""); loadFasilitas(); JOptionPane.showMessageDialog(null, "Fasilitas berhasil ditambahkan!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiUbahFasilitas() {
        if (selectedFasilitasId == -1) { JOptionPane.showMessageDialog(null, "Pilih fasilitas yang ingin diubah!"); return; }
        Fasilitas f = new Fasilitas(selectedFasilitasId, txtNamaFasilitas.getText().trim());
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.ubah(f); return null; }
            @Override protected void done() {
                try { get(); txtNamaFasilitas.setText(""); selectedFasilitasId = -1; loadFasilitas(); JOptionPane.showMessageDialog(null, "Fasilitas berhasil diubah!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void aksiHapusFasilitas() {
        if (selectedFasilitasId == -1) { JOptionPane.showMessageDialog(null, "Pilih fasilitas yang ingin dihapus!"); return; }
        if (JOptionPane.showConfirmDialog(null, "Yakin hapus fasilitas ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        int idHapus = selectedFasilitasId;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { controller.hapus(idHapus); return null; }
            @Override protected void done() {
                try { get(); txtNamaFasilitas.setText(""); selectedFasilitasId = -1; loadFasilitas(); JOptionPane.showMessageDialog(null, "Fasilitas berhasil dihapus!"); }
                catch (Exception ex) { JOptionPane.showMessageDialog(null, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }
}
