package controller;

import dao.PenghuniDAO;
import model.Penghuni;
import java.util.List;

// CONTROLLER - jembatan antara View dan Model/DAO (bagian dari MVC)
public class PenghuniController {

    private final PenghuniDAO dao = new PenghuniDAO();

    public List<Penghuni> getAll() {
        return dao.findAll();
    }

    public Penghuni getById(int id) {
        return dao.findById(id);
    }

    public void tambah(Penghuni p) {
        dao.insert(p);
    }

    public void ubah(Penghuni p) {
        dao.update(p);
    }

    public void hapus(int id) {
        dao.delete(id);
    }

    public List<Penghuni> cari(String keyword) {
        return dao.search(keyword);
    }
}
