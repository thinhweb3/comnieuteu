package comnieu.dao;

import comnieu.entity.ImportDetail;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ImportDetailDAO extends CrudDAO<ImportDetail, Long> {
    List<ImportDetail> findByImportReceiptId(Long importReceiptId);
    List<ImportDetail> findByIngredientId(Integer ingredientId);
}
