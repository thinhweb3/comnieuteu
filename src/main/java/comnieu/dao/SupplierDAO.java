package comnieu.dao;

import comnieu.entity.Supplier;
import java.util.List;

public interface SupplierDAO extends CrudDAO<Supplier, String> {
    List<Supplier> findBySupplierName(String keyword);
    List<Supplier> findByPhonePrefix(String prefix);
}
