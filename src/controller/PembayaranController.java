package controller;

import dao.PembayaranDao;
import model.Pembayaran;
import java.util.List;

// CONTROLLER - jembatan antara View dan Model/DAO (bagian dari MVC)
public class PembayaranController {

    private final PembayaranDao dao = new PembayaranDao();

    public List<Pembayaran> getAll() {
        return dao.findAll();
    }

    public Pembayaran getById(int id) {
        return dao.findById(id);
    }

    public void tambah(Pembayaran p) {
        dao.insert(p);
    }

    public void ubah(Pembayaran p) {
        dao.update(p);
    }

    public void hapus(int id) {
        dao.delete(id);
    }

    public List<Pembayaran> getByPenghuni(int idPenghuni) {
        return dao.findByPenghuni(idPenghuni);
    }
}
