package comnieu.dao.impl;

import comnieu.entity.Receipt;
import comnieu.util.XJdbc;
import java.util.List;
import comnieu.dao.ReceiptDAO;

public class ReceiptDAOImpl implements ReceiptDAO {

    @Override
    public Receipt create(Receipt detail) {
        String sql = "INSERT INTO ReceiptDetails (MaPN, MaNL, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        int rows = XJdbc.executeUpdate(sql,
                detail.getReceiptId(),
                detail.getMaterialId(),
                detail.getQuantity(),
                detail.getPrice());
        return rows > 0 ? detail : null;
    }

    @Override
    public void update(Receipt detail) {
        String sql = "UPDATE ReceiptDetails SET SoLuong = ?, DonGia = ? WHERE MaPN = ? AND MaNL = ?";
        XJdbc.executeUpdate(sql,
                detail.getQuantity(),
                detail.getPrice(),
                detail.getReceiptId(),
                detail.getMaterialId());
    }

    @Override
    public void deleteById(Void id) {
        throw new UnsupportedOperationException("deleteById() không hỗ trợ vì không có ID đơn lẻ.");
    }

    public void deleteByCompositeKey(String receiptId, String materialId) {
        String sql = "DELETE FROM ReceiptDetails WHERE MaPN = ? AND MaNL = ?";
        XJdbc.executeUpdate(sql, receiptId, materialId);
    }

    @Override
    public List<Receipt> findAll() {
        String sql = "SELECT * FROM ReceiptDetails";
        return XJdbc.getBeanList(Receipt.class, sql);
    }

    @Override
    public Receipt findById(Void id) {
        throw new UnsupportedOperationException("findById() không hỗ trợ vì không có ID đơn lẻ.");
    }

    @Override
    public List<Receipt> findByReceiptId(String receiptId) {
        String sql = "SELECT * FROM ReceiptDetails WHERE MaPN = ?";
        return XJdbc.getBeanList(Receipt.class, sql, receiptId);
    }

    @Override
    public List<Receipt> findByMaterialId(String materialId) {
        String sql = "SELECT * FROM ReceiptDetails WHERE MaNL = ?";
        return XJdbc.getBeanList(Receipt.class, sql, materialId);
    }
}
