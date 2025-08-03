package comnieu.dao;

import comnieu.entity.ImportReceipt;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ImportReceiptDAO extends CrudDAO<ImportReceipt, Long> {
    List<ImportReceipt> findBySupplierId(Integer supplierId);
    List<ImportReceipt> findByEmployeeId(Integer employeeId);
}
