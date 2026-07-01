package object;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Karyawan {

    @BsonProperty("uidRfid")
    private String uidRfid;

    @BsonProperty("idKaryawan")
    private String idKaryawan;

    @BsonProperty("namaLengkap")
    private String namaLengkap;

    @BsonProperty("jabatan")
    private String jabatan;

    public Karyawan() {
    }

    public Karyawan(String uidRfid, String idKaryawan, String namaLengkap, String jabatan) {
        this.uidRfid = uidRfid;
        this.idKaryawan = idKaryawan;
        this.namaLengkap = namaLengkap;
        this.jabatan = jabatan;
    }

    @Override
    public String toString() {
        return "Karyawan{" +
                "uidRfid='" + uidRfid + '\'' +
                ", idKaryawan='" + idKaryawan + '\'' +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", jabatan='" + jabatan + '\'' +
                '}';
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }
}