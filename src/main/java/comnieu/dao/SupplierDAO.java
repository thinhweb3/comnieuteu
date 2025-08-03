package comnieu.dao;

import comnieu.entity.Supplier;
import java.util.List;

/**
 * DAO interface cho Supplier
 * 
 * @author Admin
 */
public interface SupplierDAO extends CrudDAO<Supplier, Integer> {
    Supplier findByPhone(String phone);
    List<Supplier> findByName(String keyword);
}
