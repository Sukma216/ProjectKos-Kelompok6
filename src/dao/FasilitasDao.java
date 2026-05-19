package dao;

import model.Fasilitas;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FasilitasDao implements GenerialDAO<Fasilitas> { // PILAR - inheritance
    // Semua DAO mewarisi kontrak dari GenerialDAO 
    // wajib punya CRUD DASAAR insert, update, delete, read (findById, findAll)
    private Connection conn() { return DatabaseConnection.getConnection(); }

    // PILAR - polymorphism
    // Method insert, delete, update, read (CRUD) dipanggil sama, tapi tiap DAO implementasinya berbeda
    
    // INSERT
    @Override
    public void insert(Fasilitas f) {
        try (PreparedStatement ps = conn().prepareStatement(
                "INSERT INTO fasilitas (nama_fasilitas,deskripsi) VALUES (?,?)")) {
            ps.setString(1, f.getNamaFasilitas());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // UPDATE
    @Override
    public void update(Fasilitas f) {
        try (PreparedStatement ps = conn().prepareStatement(
                "UPDATE fasilitas SET nama_fasilitas=?,deskripsi=? WHERE id_fasilitas=?")) {
            ps.setString(1, f.getNamaFasilitas());
            ps.setInt   (3, f.getIdFasilitas());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // DELETE
    @Override
    public void delete(int id) {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM fasilitas WHERE id_fasilitas=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // READ (findById, findAll)
    @Override
    public Fasilitas findById(int id) {
        try (PreparedStatement ps = conn().prepareStatement("SELECT * FROM fasilitas WHERE id_fasilitas=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public List<Fasilitas> findAll() {
        List<Fasilitas> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM fasilitas ORDER BY nama_fasilitas")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    private Fasilitas map(ResultSet rs) throws SQLException {
        return new Fasilitas(rs.getInt("id_fasilitas"), rs.getString("nama_fasilitas"));
    }
}