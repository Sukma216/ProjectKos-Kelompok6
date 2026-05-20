package dao;

import model.Penghuni;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenghuniDAO implements GenerialDAO<Penghuni> { // inheritance
    // Semua DAO mewarisi kontrak dari GenerialDAO 
    // wajib punya CRUD DASAAR insert, update, delete, read (findById, findAll)
    private Connection conn() {
        return DatabaseConnection.getConnection();
    }

    // PILAR - polymorphism
    // Method insert, delete, update, read (CRUD) dipanggil sama, tapi tiap DAO implementasinya berbeda
    
    // INSERT
    @Override
    public void insert(Penghuni p) {
        String sql = "INSERT INTO penghuni "
                + "(nama, no_hp, id_kamar, asal_daerah, tanggal_masuk, status_penghuni, nama_ortu, no_hp_ortu) "
                + "VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getNoTelepon());
            ps.setInt(3, p.getIdKamar());
            ps.setString(4, p.getAsal());
            ps.setString(5, p.getTglMasuk());
            ps.setString(6, p.getStatusPenghuni());
            ps.setString(7, p.getNamaOrtu());
            ps.setString(8, p.getTelpOrtu());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // UPDATE
    @Override
    public void update(Penghuni p) {
        String sql = "UPDATE penghuni SET "
                + "nama=?, "
                + "no_hp=?, "
                + "id_kamar=?, "
                + "asal_daerah=?, "
                + "tanggal_masuk=?, "
                + "status_penghuni=?, "
                + "nama_ortu=?, "
                + "no_hp_ortu=? "
                + "WHERE id_penghuni=?";

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getNoTelepon());
            ps.setInt(3, p.getIdKamar());
            ps.setString(4, p.getAsal());
            ps.setString(5, p.getTglMasuk());
            ps.setString(6, p.getStatusPenghuni());
            ps.setString(7, p.getNamaOrtu());
            ps.setString(8, p.getTelpOrtu());
            ps.setInt(9, p.getIdPenghuni());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE
    @Override
    public void delete(int id) {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM penghuni WHERE id_penghuni=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    
    // READ (findById, findAll)
    @Override
    public Penghuni findById(int id) {
        try (PreparedStatement ps = conn().prepareStatement("SELECT * FROM penghuni WHERE id_penghuni=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public List<Penghuni> findAll() {
        List<Penghuni> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM penghuni ORDER BY nama")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public List<Penghuni> search(String keyword) {
        List<Penghuni> list = new ArrayList<>();
        String sql = "SELECT * FROM penghuni WHERE nama LIKE ? OR no_hp LIKE ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    private Penghuni map(ResultSet rs) throws SQLException {
        return new Penghuni(
            rs.getInt("id_penghuni"),
            rs.getString("nama"),
            rs.getString("no_hp"),
            rs.getInt("id_kamar"),
            rs.getString("asal_daerah"),
            rs.getString("tanggal_masuk"),
            rs.getString("status_penghuni"),
            rs.getString("nama_ortu"),
            rs.getString("no_hp_ortu")
        );
    }
}
