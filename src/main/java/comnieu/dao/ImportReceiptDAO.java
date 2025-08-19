package comnieu.dao;

import comnieu.entity.ImportReceipt;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ImportReceiptDAO extends CrudDAO<ImportReceipt, Long> {
    List<ImportReceipt> findBySupplierId(Integer supplierId);
    List<ImportReceipt> findByEmployeeId(Integer employeeId);
    List<ImportReceipt> findByDateRange(LocalDate from, LocalDate to);
}
