package comnieu.dao;

import comnieu.entity.Receipt;
import java.util.List;

public interface ReceiptDAO extends CrudDAO<Receipt, Void> {
    List<Receipt> findByReceiptId(String receiptId);
    List<Receipt> findByMaterialId(String materialId);
}
