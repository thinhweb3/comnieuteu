
package comnieu.dao;

import comnieu.entity.Dish;
import java.util.List;



/**
 *
 * @author Admin
 */
public interface DishDAO extends CrudDAO<Dish, Integer> {
    List<Dish> findByCategoryId(Integer categoryId);
}
