package comnieu.dao;

import comnieu.entity.Promotion;
import java.util.Date;
import java.util.List;

/**
 * DAO interface cho Promotion
 * 
 * @author Admin
 */
public interface PromotionDAO extends CrudDAO<Promotion, Integer> {
    List<Promotion> findByDateRange(Date start, Date end);
    Promotion findByName(String name);
}
