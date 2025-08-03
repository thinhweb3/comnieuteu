package lmp;

public class Categorylmp {
    private String maMA;
    private String tenMon;
    private String size;
    private double donGia;
    private String maLoaiMA;
    private String tinhTrang;

    public Categorylmp(String maMA, String tenMon, String size, double donGia, String maLoaiMA, String tinhTrang) {
        this.maMA = maMA;
        this.tenMon = tenMon;
        this.size = size;
        this.donGia = donGia;
        this.maLoaiMA = maLoaiMA;
        this.tinhTrang = tinhTrang;
    }

    public Object[] toRow() {
        return new Object[] { maMA, tenMon, size, donGia, tinhTrang };
    }
}
