package comnieu.dao.impl;

import comnieu.dao.SupplierDAO;
import comnieu.entity.Supplier;

import java.util.List;
import comnieu.util.XJdbc;

public class SupplierDAOImpl implements SupplierDAO {

    @Override
    public Supplier create(Supplier s) {
        String sql = """
            INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai)
            VALUES (?, ?, ?, ?)
        """;
        int rows = XJdbc.executeUpdate(sql,
                s.getMaNCC(), s.getTenNCC(), s.getDiaChi(), s.getDienThoai());
        return rows > 0 ? s : null;
    }

    @Override
    public void update(Supplier s) {
        String sql = """
            UPDATE NhaCungCap
            SET TenNCC = ?, DiaChi = ?, DienThoai = ?
            WHERE MaNCC = ?
        """;
        XJdbc.executeUpdate(sql,
                s.getTenNCC(), s.getDiaChi(), s.getDienThoai(), s.getMaNCC());
    }

    @Override
    public void deleteById(String maNCC) {
        String sql = "DELETE FROM NhaCungCap WHERE MaNCC = ?";
        XJdbc.executeUpdate(sql, maNCC);
    }

    @Override
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM NhaCungCap";
        return XJdbc.getBeanList(Supplier.class, sql);
    }

    @Override
    public Supplier findById(String maNCC) {
        String sql = "SELECT * FROM NhaCungCap WHERE MaNCC = ?";
        return XJdbc.getSingleBean(Supplier.class, sql, maNCC);
    }
    
    @Override
    public List<Supplier> findBySupplierName(String keyword) {
        String sql = "SELECT * FROM NhaCungCap WHERE TenNCC LIKE ?";
        return XJdbc.getBeanList(Supplier.class, sql, "%" + keyword + "%");
    }

    @Override
    public List<Supplier> findByPhonePrefix(String prefix) {
        String sql = "SELECT * FROM NhaCungCap WHERE DienThoai LIKE ?";
        return XJdbc.getBeanList(Supplier.class, sql, prefix + "%");
    }
}
