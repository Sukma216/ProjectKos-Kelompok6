package model;

public class KondisiFasilitas {

    // PILAR - encapsulation
    private int idKamar;
    private int idFasilitas;
    private String kondisi;
    private String keteranganRusak;
    private String terakhirDiperbarui;

    public KondisiFasilitas() {}

    public KondisiFasilitas(
            int idKamar,
            int idFasilitas,
            String kondisi,
            String keteranganRusak,
            String terakhirDiperbarui
    ) {

        this.idKamar = idKamar;
        this.idFasilitas = idFasilitas;
        this.kondisi = kondisi;
        this.keteranganRusak = keteranganRusak;
        this.terakhirDiperbarui = terakhirDiperbarui;
    }

    // getter
    public int getIdKamar() {
        return idKamar;
    }

    public int getIdFasilitas() {
        return idFasilitas;
    }

    public String getKondisi() {
        return kondisi;
    }

    public String getKeteranganRusak() {
        return keteranganRusak;
    }

    public String getTerakhirDiperbarui() {
        return terakhirDiperbarui;
    }

    // setter
    public void setIdKamar(int idKamar) {
        this.idKamar = idKamar;
    }

    public void setIdFasilitas(int idFasilitas) {
        this.idFasilitas = idFasilitas;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public void setKeteranganRusak(String keteranganRusak) {
        this.keteranganRusak = keteranganRusak;
    }

    public void setTerakhirDiperbarui(String terakhirDiperbarui) {
        this.terakhirDiperbarui = terakhirDiperbarui;
    }
}