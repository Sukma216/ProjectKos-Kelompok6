package model;

public class Fasilitas {
    // PILAR - encapsulation 
    private int    idFasilitas;
    private String namaFasilitas;

    public Fasilitas() {}

    public Fasilitas(int idFasilitas, String namaFasilitas) {
        this.idFasilitas   = idFasilitas;
        this.namaFasilitas = namaFasilitas;
    }

    public int    getIdFasilitas()   { return idFasilitas; }
    public String getNamaFasilitas() { return namaFasilitas; }

    public void setIdFasilitas(int idFasilitas)       { this.idFasilitas   = idFasilitas; }
    public void setNamaFasilitas(String namaFasilitas)  { this.namaFasilitas = namaFasilitas; }

    @Override
    public String toString() { return namaFasilitas; }
}