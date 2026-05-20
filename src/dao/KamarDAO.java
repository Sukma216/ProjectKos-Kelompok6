package dao;

import model.Kamar;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// INHERITANCE + POLYMORPHYISM
// KamarDAO mengimplementasikan GenerialDAO, tp setiap DAO punya implementasi berbeda sesuai tabel masing masing

public class KamarDAO implements GenerialDAO<Kamar> { //inheritance
    // Semua DAO mewarisi kontrak dari GenerialDAO 
    // wajib punya CRUD DASAAR insert, update, delete, read (findById, findAll)
    private Connection conn() {
        return DatabaseConnection.getConnection();
    }
    
    // PILAR - polymorphism
    // Method insert, delete, update, read (CRUD) dipanggil sama, tapi tiap DAO implementasinya berbeda
    
    // INSERT
    @Override
    public void insert(Kamar k) {
        String sql = "INSERT INTO kamar (nama_kamar, harga, status_kamar) VALUES (?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, k.getNamaKamar());
            ps.setDouble(2, k.getHarga());
            ps.setString(3, k.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // UPDATE
    @Override
    public void update(Kamar k) {
        String sql = "UPDATE kamar SET nama_kamar=?,harga=?,status_kamar=? WHERE id_kamar=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, k.getNamaKamar());
            ps.setDouble(2, k.getHarga());
            ps.setString(3, k.getStatus());
            ps.setInt   (4, k.getIdKamar());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // DELETE
    @Override
    public void delete(int id) {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM kamar WHERE id_kamar=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // READ (findById, findAll)
    @Override
    public Kamar findById(int id) {
        try (PreparedStatement ps = conn().prepareStatement("SELECT * FROM kamar WHERE id_kamar=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public List<Kamar> findAll() {
        List<Kamar> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM kamar ORDER BY nama_kamar")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public List<Kamar> findByStatus(String status) {
        List<Kamar> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement("SELECT * FROM kamar WHERE status_kamar=?")) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    private Kamar map(ResultSet rs) throws SQLException {
        return new Kamar(
            rs.getInt("id_kamar"), rs.getString("nama_kamar"),
            rs.getDouble("harga"), rs.getString("status_kamar")
        );
    }
}
