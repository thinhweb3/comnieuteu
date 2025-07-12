package comnieu.dao.impl;

import comnieu.dao.MaterialDAO;
import comnieu.entity.Material;
import comnieu.util.XJdbc;

import java.util.List;

public class MaterialDAOImpl implements MaterialDAO {

    @Override
    public Material create(Material m) {
        String sql = """
            INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC)
            VALUES (?, ?, ?, ?, ?)
        """;
        int rows = XJdbc.executeUpdate(sql,
                m.getMaterialId(), m.getMaterialName(), m.getUnit(), m.getStatus(), m.getSupplierId());
        return rows > 0 ? m : null;
    }

    @Override
    public void update(Material m) {
        String sql = """
            UPDATE NguyenLieu SET TenNL = ?, DonVi = ?, TrangThai = ?, MaNCC = ?
            WHERE MaNL = ?
        """;
        XJdbc.executeUpdate(sql,
                m.getMaterialName(), m.getUnit(), m.getStatus(), m.getSupplierId(), m.getMaterialId());
    }

    @Override
    public void deleteById(String materialId) {
        String sql = "DELETE FROM NguyenLieu WHERE MaNL = ?";
        XJdbc.executeUpdate(sql, materialId);
    }

    @Override
    public List<Material> findAll() {
        String sql = "SELECT * FROM NguyenLieu";
        return XJdbc.getBeanList(Material.class, sql);
    }

    @Override
    public Material findById(String materialId) {
        String sql = "SELECT * FROM NguyenLieu WHERE MaNL = ?";
        return XJdbc.getSingleBean(Material.class, sql, materialId);
    }

    @Override
    public List<Material> findByStatus(String status) {
        String sql = "SELECT * FROM NguyenLieu WHERE TrangThai = ?";
        return XJdbc.getBeanList(Material.class, sql, status);
    }

    @Override
    public List<Material> findBySupplierId(String supplierId) {
        String sql = "SELECT * FROM NguyenLieu WHERE MaNCC = ?";
        return XJdbc.getBeanList(Material.class, sql, supplierId);
    }
}
