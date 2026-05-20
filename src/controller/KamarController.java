package controller;

import dao.KamarDAO;
import model.Kamar;
import java.util.List;

// CONTROLLER - jembatan antara View dan Model/DAO (bagian dari MVC)
public class KamarController {

    private final KamarDAO dao = new KamarDAO();

    public List<Kamar> getAll() {
        return dao.findAll();
    }

    public Kamar getById(int id) {
        return dao.findById(id);
    }

    public void tambah(Kamar k) {
        dao.insert(k);
    }

    public void ubah(Kamar k) {
        dao.update(k);
    }

    public void hapus(int id) {
        dao.delete(id);
    }

    public List<Kamar> getByStatus(String status) {
        return dao.findByStatus(status);
    }
}
