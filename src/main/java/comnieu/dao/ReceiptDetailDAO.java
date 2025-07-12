package comnieu.dao;

import comnieu.entity.ReceiptDetail;
import java.util.List;

public interface ReceiptDetailDAO extends CrudDAO<ReceiptDetail, String> {
    List<ReceiptDetail> findByReceiptId(String receiptId);
}
