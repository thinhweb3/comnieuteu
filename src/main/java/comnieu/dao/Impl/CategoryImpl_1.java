package comnieu.dao.Impl;

public class CategoryImpl_1 {
    private String maMA;
    private String tenMon;
    private String size;
    private double donGia;
    private String maLoaiMA;
    private String tinhTrang;

    public CategoryImpl_1(String maMA, String tenMon, String size, double donGia, String maLoaiMA, String tinhTrang) {
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

    // GETTERS
    public String getMaMA() { return maMA; }
    public String getTenMon() { return tenMon; }
    public String getSize() { return size; }
    public double getDonGia() { return donGia; }
    public String getMaLoaiMA() { return maLoaiMA; }
    public String getTinhTrang() { return tinhTrang; }

    // SETTERS
    public void setMaMA(String maMA) { this.maMA = maMA; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }
    public void setSize(String size) { this.size = size; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
    public void setMaLoaiMA(String maLoaiMA) { this.maLoaiMA = maLoaiMA; }
    public void setTinhTrang(String tinhTrang) { this.tinhTrang = tinhTrang; }
}
