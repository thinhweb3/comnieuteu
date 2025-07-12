package comnieu.dao.impl;

import comnieu.dao.ReceiptDetailDAO;
import comnieu.entity.ReceiptDetail;
import comnieu.util.XJdbc;

import java.util.List;

public class ReceiptDetailDAOImpl implements ReceiptDetailDAO {

    @Override
    public ReceiptDetail create(ReceiptDetail detail) {
        String sql = "INSERT INTO ChiTietPhieuNhap (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (?, ?, ?, ?, ?)";
        int rows = XJdbc.executeUpdate(sql,
                detail.getReceiptDetailId(),
                detail.getReceiptId(),
                detail.getMaterialId(),
                detail.getQuantity(),
                detail.getPrice());
        return rows > 0 ? detail : null;
    }

    @Override
    public void update(ReceiptDetail detail) {
        String sql = "UPDATE ChiTietPhieuNhap SET MaPN = ?, MaNL = ?, SL = ?, DonGia = ? WHERE MaCTPN = ?";
        XJdbc.executeUpdate(sql,
                detail.getReceiptId(),
                detail.getMaterialId(),
                detail.getQuantity(),
                detail.getPrice(),
                detail.getReceiptDetailId());
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE MaCTPN = ?";
        XJdbc.executeUpdate(sql, id);
    }

    @Override
    public List<ReceiptDetail> findAll() {
        String sql = "SELECT * FROM ChiTietPhieuNhap";
        return XJdbc.getBeanList(ReceiptDetail.class, sql);
    }

    @Override
    public ReceiptDetail findById(String id) {
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaCTPN = ?";
        return XJdbc.getSingleBean(ReceiptDetail.class, sql, id);
    }

    @Override
    public List<ReceiptDetail> findByReceiptId(String receiptId) {
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN = ?";
        return XJdbc.getBeanList(ReceiptDetail.class, sql, receiptId);
    }
}
