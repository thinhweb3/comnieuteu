package comnieu.dao;

import comnieu.entity.Material;
import java.util.List;

public interface MaterialDAO extends CrudDAO<Material, String> {
    List<Material> findByStatus(String status);
    List<Material> findBySupplierId(String supplierId);
}
