package dao;

import model.KondisiFasilitas;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
public class KondisiFasilitasDao implements GenerialDAO<KondisiFasilitas> { // inheritance
    // Semua DAO mewarisi kontrak dari GenerialDAO 
    // wajib punya CRUD DASAAR insert, update, delete, read (findById, findAll)
    private Connection conn() {
        return DatabaseConnection.getConnection();
    }

    // PILAR - polymorphism
    // Method insert, delete, update, read (CRUD) dipanggil sama, tapi tiap DAO implementasinya berbeda
    
    // INSERT
    @Override
    public void insert(KondisiFasilitas fk) {

        String sql = "INSERT INTO fasilitas_kondisi "
                + "(id_kamar, id_fasilitas, kondisi, keterangan_rusak, terakhir_diperbarui) "
                + "VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = conn().prepareStatement(sql)) {

            ps.setInt(1, fk.getIdKamar());
            ps.setInt(2, fk.getIdFasilitas());
            ps.setString(3, fk.getKondisi());
            ps.setString(4, fk.getKeteranganRusak());
            ps.setString(5, fk.getTerakhirDiperbarui());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // UPDATE
    @Override
    public void update(KondisiFasilitas fk) {

        String sql = "UPDATE fasilitas_kondisi SET "
                + "id_fasilitas=?, "
                + "kondisi=?, "
                + "keterangan_rusak=?, "
                + "terakhir_diperbarui=? "
                + "WHERE id_kamar=?";

        try (PreparedStatement ps = conn().prepareStatement(sql)) {

            ps.setInt(1, fk.getIdFasilitas());
            ps.setString(2, fk.getKondisi());
            ps.setString(3, fk.getKeteranganRusak());
            ps.setString(4, fk.getTerakhirDiperbarui());
            ps.setInt(5, fk.getIdKamar());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE
    @Override
    public void delete(int id) {

        try (PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM fasilitas_kondisi WHERE id_kamar=?")) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // READ (findById, findAll)
    @Override
    public KondisiFasilitas findById(int id) {

        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT * FROM fasilitas_kondisi WHERE id_kamar=?")) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<KondisiFasilitas> findAll() {

        List<KondisiFasilitas> list = new ArrayList<>();

        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT * FROM fasilitas_kondisi ORDER BY terakhir_diperbarui DESC")) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private KondisiFasilitas map(ResultSet rs) throws SQLException {

        return new KondisiFasilitas(
                rs.getInt("id_kamar"),
                rs.getInt("id_fasilitas"),
                rs.getString("kondisi"),
                rs.getString("keterangan_rusak"),
                rs.getString("terakhir_diperbarui")
        );
    }
}