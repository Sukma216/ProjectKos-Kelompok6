package controller;

import dao.FasilitasDao;
import dao.KondisiFasilitasDao;
import model.Fasilitas;
import model.KondisiFasilitas;
import java.util.List;

// CONTROLLER - jembatan antara View dan Model/DAO (bagian dari MVC)
public class FasilitasController {

    private final FasilitasDao fasilitasDao = new FasilitasDao();
    private final KondisiFasilitasDao kondisiDao = new KondisiFasilitasDao();

    public List<Fasilitas> getAll() {
        return fasilitasDao.findAll();
    }

    public void tambah(Fasilitas f) {
        fasilitasDao.insert(f);
    }

    public void ubah(Fasilitas f) {
        fasilitasDao.update(f);
    }

    public void hapus(int id) {
        fasilitasDao.delete(id);
    }

    public List<KondisiFasilitas> getAllKondisi() {
        return kondisiDao.findAll();
    }

    public void ubahKondisi(KondisiFasilitas kf) {
        kondisiDao.update(kf);
    }
}
