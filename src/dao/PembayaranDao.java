package dao;

import model.Pembayaran;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PembayaranDao implements GenerialDAO<Pembayaran> { // inheritance
    // Semua DAO mewarisi kontrak dari GenerialDAO 
    // wajib punya CRUD DASAAR insert, update, delete, read (findById, findAll)
    private Connection conn() {
        return DatabaseConnection.getConnection();
    }

    // PILAR - polymorphism
    // Method insert, delete, update, read (CRUD) dipanggil sama, tapi tiap DAO implementasinya berbeda
    
    
    // INSERT ATAU TAMBAH DATA
    @Override
    public void insert(Pembayaran p) {

    // jangan lupa ini harus sama kaya nama tabel database
    String sql = "INSERT INTO pembayaran "
            + "(id_penghuni, nama_penghuni, id_kamar, bulan_bayar, "
            + "tanggal_bayar, jumlah_bayar, metode_bayar, status_bayar, keterangan) "
            + "VALUES (?,?,?,?,?,?,?,?,?)";

    try (PreparedStatement ps = conn().prepareStatement(sql)) {

        ps.setInt(1, p.getIdPenghuni());
        ps.setString(2, p.getNamaPenghuni());
        ps.setInt(3, p.getIdKamar());
        ps.setString(4, p.getBulan());
        ps.setString(5, p.getTanggal());
        ps.setDouble(6, p.getJumlah());
        ps.setString(7, p.getMetodeBayar());
        ps.setString(8, p.getStatus());
        ps.setString(9, p.getKeterangan());

        ps.executeUpdate();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

    // UPDATE
    @Override
    public void update(Pembayaran p) {

        // yg ini nama nama nya harus sesuai sama nama kolom di tabel database 
        String sql = "UPDATE pembayaran SET "
                + "id_penghuni=?, "
                + "nama_penghuni=?, "
                + "id_kamar=?, "
                + "bulan_bayar=?, "
                + "tanggal_bayar=?, "
                + "jumlah_bayar=?, "
                + "metode_bayar=?, "
                + "status_bayar=?, "
                + "keterangan=? "
                + "WHERE id_pembayaran=?";

        try (PreparedStatement ps = conn().prepareStatement(sql)) {

            ps.setInt(1, p.getIdPenghuni());
            ps.setString(2, p.getNamaPenghuni());
            ps.setInt(3, p.getIdKamar());
            ps.setString(4, p.getBulan());
            ps.setString(5, p.getTanggal());
            ps.setDouble(6, p.getJumlah());
            ps.setString(7, p.getMetodeBayar());
            ps.setString(8, p.getStatus());
            ps.setString(9, p.getKeterangan());
            ps.setInt(10, p.getIdPembayaran());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE
    @Override
    public void delete(int id) {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM pembayaran WHERE id_pembayaran=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // READ (findById, findAll)
    @Override
    public Pembayaran findById(int id) {
        try (PreparedStatement ps = conn().prepareStatement("SELECT * FROM pembayaran WHERE id_pembayaran=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public List<Pembayaran> findAll() {
        List<Pembayaran> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM pembayaran ORDER BY tanggal_bayar DESC")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public List<Pembayaran> findByPenghuni(int idPenghuni) {
        List<Pembayaran> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT * FROM pembayaran WHERE id_penghuni=? ORDER BY tanggal_bayar DESC")) {
            ps.setInt(1, idPenghuni);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    private Pembayaran map(ResultSet rs) throws SQLException {
        return new Pembayaran(
            rs.getInt("id_pembayaran"),
            rs.getInt("id_penghuni"),
            rs.getString("nama_penghuni"),
            rs.getInt("id_kamar"),
            rs.getString("bulan_bayar"),
            rs.getString("tanggal_bayar"),
            rs.getDouble("jumlah_bayar"),
            rs.getString("metode_bayar"),
            rs.getString("status_bayar"),
            rs.getString("keterangan")
        );
    }
}